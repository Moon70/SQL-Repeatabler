package lunartools.sqlrepeatabler.worker;

import java.io.File;
import java.util.ArrayList;

import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.services.ConverterService;

public class ConvertSqlFileWorker extends SwingWorker<Void, Void> {
	private static Logger logger = LoggerFactory.getLogger(ConvertSqlFileWorker.class);
	private SqlRepeatablerModel model;
	private ArrayList<StringBuffer> convertedSqlScripts=new ArrayList<>();
	private ArrayList<SqlBlock> convertedSqlScriptBlocks=new ArrayList<>();
	private ArrayList<SqlScript> sqlScripts=new ArrayList<>();
	
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
				ConverterService converterService=new ConverterService(model);
				SqlScript sqlScript=converterService.createSqlScript(file);
				sqlScripts.add(sqlScript);
				SqlBlock sqlBlock=converterService.parseFile(sqlScript);
				convertedSqlScripts.add(new StringBuffer(sqlBlock.toString()));
				convertedSqlScriptBlocks.add(sqlBlock);
			}
		} catch (Exception e) {
			logger.error("error converting sql files",e);
		}
		return null;
	}

	@Override
	protected void done() {
		super.done();
		try {
			model.setSqlScripts(sqlScripts);
			model.setConvertedSqlScripts(convertedSqlScripts);
			model.setConvertedSqlScriptBlocks(convertedSqlScriptBlocks);
		} catch (Exception e) {
			logger.error("error setting converted data",e);
			throw e;
		}
	}
	
}
