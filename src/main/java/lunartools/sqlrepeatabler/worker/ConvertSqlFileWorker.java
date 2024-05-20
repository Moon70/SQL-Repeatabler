package lunartools.sqlrepeatabler.worker;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.SqlRepeatablerController;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.services.ConverterService;

public class ConvertSqlFileWorker extends SwingWorker<Void, Void> {
	private static Logger logger = LoggerFactory.getLogger(ConvertSqlFileWorker.class);
	private SqlRepeatablerModel model;
	private SqlRepeatablerController controller;

	public ConvertSqlFileWorker(SqlRepeatablerModel model,SqlRepeatablerController controller) {
		this.model=model;
		this.controller=controller;
	}

	@Override
	public Void doInBackground() {
		logger.debug("converting sql files...");
		//controller.setBusy(true);
		try {
			model.clearConvertedSqlScript();
			controller.clearInputData();
			model.addConvertedSqlScript("-- "+SqlRepeatablerModel.PROGRAMNAME+" "+SqlRepeatablerModel.determineProgramVersion()+System.lineSeparator());
			model.addConvertedSqlScript(System.lineSeparator());
			ArrayList<File> files=model.getSqlInputFiles();
			for(int i=0;i<files.size();i++) {
				File file=files.get(i);
				logger.debug("converting: "+file);
				if(i>0) {
					model.addConvertedSqlScript(System.lineSeparator());
				}
				model.addConvertedSqlScript("-- converting file: "+file.getName()+System.lineSeparator());
				new ConverterService(model,controller).parseFile(file);
			}
		} catch (Exception e) {
			logger.error("converting sql files",e);
		}
		return null;
	}

}
