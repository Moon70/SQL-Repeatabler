package lunartools.sqlrepeatabler.core.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.util.Dialogs;
import lunartools.sqlrepeatabler.common.util.SwingWorkerUpdate;
import lunartools.sqlrepeatabler.core.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.core.model.SqlBlock;
import lunartools.sqlrepeatabler.core.model.SqlScript;
import lunartools.sqlrepeatabler.core.processing.SqlParser;

public class ConvertSqlFileWorker extends SwingWorker<Void, SwingWorkerUpdate<?>> {
	private static Logger logger = LoggerFactory.getLogger(ConvertSqlFileWorker.class);
	private final SqlRepeatablerModel model;
	private final FileService fileService;
	private ArrayList<SqlBlock> convertedSqlScriptBlocks=new ArrayList<>();
	public enum Step {SQLSCRIPT}

	public ConvertSqlFileWorker(SqlRepeatablerModel model,FileService fileService) {
		this.model=model;
		this.fileService=fileService;
	}

	@Override
	public Void doInBackground() throws Exception {
		ArrayList<SqlScript> sqlScripts=fileService.loadFiles(model);

		publish(new SwingWorkerUpdate<>(Step.SQLSCRIPT,sqlScripts));

		ArrayList<File> files=model.getSqlInputFiles();
		for(int i=0;i<files.size();i++) {
			logger.info("Processing: "+files.get(i));
			SqlScript sqlScript=sqlScripts.get(i);
			SqlBlock sqlBlock=SqlParser.parse(sqlScript);
			convertedSqlScriptBlocks.add(sqlBlock);
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
			get();
			model.setConvertedSqlScriptBlocks(convertedSqlScriptBlocks);
		} catch (ExecutionException e) {
			logger.error("Error converting files",e);
		} catch (Exception e) {
			logger.error("error setting converted data",e);
			Dialogs.showErrorMessage("Error processing file:\n" + e.getMessage());
		}
	}

}
