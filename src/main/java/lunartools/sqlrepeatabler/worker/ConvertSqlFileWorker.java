package lunartools.sqlrepeatabler.worker;

import java.io.File;
import java.util.ArrayList;

import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.services.ConverterService;

public class ConvertSqlFileWorker extends SwingWorker<Void, Void> {
	private static Logger logger = LoggerFactory.getLogger(ConvertSqlFileWorker.class);
	private SqlRepeatablerModel model;

	public ConvertSqlFileWorker(SqlRepeatablerModel model) {
		this.model=model;
	}

	@Override
	public Void doInBackground() {
		try {
			model.clearConvertedSqlScript();
			model.clearInputPanel();
			model.addConvertedSqlScript("-- "+SqlRepeatablerModel.PROGRAMNAME+" "+SqlRepeatablerModel.getProgramVersion()+System.lineSeparator());
			model.addConvertedSqlScript(System.lineSeparator());
			ArrayList<File> files=model.getSqlInputFiles();
			for(int i=0;i<files.size();i++) {
				File file=files.get(i);
				logger.info("converting: "+file);
				if(i>0) {
					model.addConvertedSqlScript(System.lineSeparator());
				}
				model.addConvertedSqlScript("-- converting file: "+file.getName()+System.lineSeparator());
				new ConverterService(model).parseFile(file);
			}
		} catch (Exception e) {
			logger.error("converting sql files",e);
		}
		return null;
	}

}
