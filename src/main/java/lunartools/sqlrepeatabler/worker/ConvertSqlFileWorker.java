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
	private ArrayList<StringBuffer> convertedSqlScripts=new ArrayList<>();
	
	public ConvertSqlFileWorker(SqlRepeatablerModel model) {
		this.model=model;
	}

	@Override
	public Void doInBackground() {
		try {
			model.clearConvertedSqlScript();
			model.clearInputPanel();
			ArrayList<File> files=model.getSqlInputFiles();
			
			for(int i=0;i<files.size();i++) {
				File file=files.get(i);
				logger.info("converting: "+file);
				convertedSqlScripts.add(new ConverterService(model).parseFile(file));
			}
		} catch (Exception e) {
			logger.error("converting sql files",e);
		}
		return null;
	}

	@Override
	protected void done() {
		super.done();
		try {
			model.setConvertedSqlScripts(convertedSqlScripts);
		} catch (Exception e) {
			logger.error("buuu",e);
			throw e;
		}
	}
	
}
