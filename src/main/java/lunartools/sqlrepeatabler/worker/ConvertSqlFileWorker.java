package lunartools.sqlrepeatabler.worker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.common.SwingWorkerUpdate;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.services.ConverterService;

public class ConvertSqlFileWorker extends SwingWorker<Void, SwingWorkerUpdate<?>> {
	private static Logger logger = LoggerFactory.getLogger(ConvertSqlFileWorker.class);
	private SqlRepeatablerModel model;
	private ArrayList<SqlBlock> convertedSqlScriptBlocks=new ArrayList<>();
	private ArrayList<SqlScript> sqlScripts=new ArrayList<>();
	public enum Step {SQLSCRIPT}

	public ConvertSqlFileWorker(SqlRepeatablerModel model) {
		this.model=model;
	}

	@Override
	public Void doInBackground() {
		File file=null;
		try {
			model.clearConvertedSqlScriptBlocks();
			model.clearInputPanel();
			ArrayList<File> files=model.getSqlInputFiles();

			for(int i=0;i<files.size();i++) {
				file=files.get(i);
				logger.debug("Reading: "+file);
				ConverterService converterService=new ConverterService(model);
				SqlScript sqlScript=converterService.createSqlScript(file);
				sqlScripts.add(sqlScript);
			}

			publish(new SwingWorkerUpdate<>(Step.SQLSCRIPT,sqlScripts));

			for(int i=0;i<files.size();i++) {
				file=files.get(i);
				logger.info("Processing: "+file);
				ConverterService converterService=new ConverterService(model);
				SqlScript sqlScript=sqlScripts.get(i);
				SqlBlock sqlBlock=converterService.parseFile(sqlScript);
				convertedSqlScriptBlocks.add(sqlBlock);
			}
		} catch (Exception e) {
			logger.error(String.format("Error processing sql file: %s",file),e);
		}
		return null;
	}

	@Override
	protected void process(List<SwingWorkerUpdate<?>> chunks) {
		for (SwingWorkerUpdate<?> update : chunks) {
			switch ((Step)update.step){
			case SQLSCRIPT:
				@SuppressWarnings("unchecked")
				ArrayList<SqlScript> sqlScripts = (ArrayList<SqlScript>)update.data;
				model.setSqlScripts(sqlScripts);
				break;
			}
		}
	}

	@Override
	protected void done() {
		super.done();
		try {
			model.setConvertedSqlScriptBlocks(convertedSqlScriptBlocks);
		} catch (Exception e) {
			logger.error("error setting converted data",e);
			throw e;
		}
	}

}
