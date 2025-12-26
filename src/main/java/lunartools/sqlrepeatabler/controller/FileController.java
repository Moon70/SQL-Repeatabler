package lunartools.sqlrepeatabler.controller;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.CancellationException;

import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.ui.Dialogs;
import lunartools.sqlrepeatabler.main.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.services.FileService;
import lunartools.sqlrepeatabler.worker.ConvertSqlFileWorker;

public class FileController {
	private static Logger logger = LoggerFactory.getLogger(FileController.class);
	private final SqlRepeatablerModel model;
	private final FileService fileService;

	public FileController(SqlRepeatablerModel model, FileService fileService) {
		this.model=model;
		this.fileService = Objects.requireNonNull(fileService);
	}

	public void loadFiles() {
		ConvertSqlFileWorker worker=new ConvertSqlFileWorker(model,fileService);
		worker.execute();
	}

	public void saveFile(File file) {
		logger.debug("Saving: "+file);
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				fileService.saveFile(file,model);
				return null;
			}

			@Override
			protected void done() {
				try {
					get();
					logger.info("File saved successfully: {}", file.getAbsolutePath());
				} catch (CancellationException e) {
					logger.warn("File saving cancelled: {}", file.getAbsolutePath());
				} catch (Exception e) {
					logger.error("Error saving file {}", file.getAbsolutePath(), e);
					Dialogs.showErrorMessage("Error saving file:\n" + e.getMessage());
				}
			}
		};
		worker.execute();
	}
}
