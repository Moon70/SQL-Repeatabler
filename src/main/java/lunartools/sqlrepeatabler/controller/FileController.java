package lunartools.sqlrepeatabler.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.main.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.services.FileService;

public class FileController {
	private static Logger logger = LoggerFactory.getLogger(FileController.class);
	private final SqlRepeatablerModel model;
	private final FileService fileService;

	public FileController(SqlRepeatablerModel model, FileService fileService) {
		this.model=model;
		this.fileService = Objects.requireNonNull(fileService);
	}

	public void saveFile(File file) {
		//fileService.saveFile(file);
		try {
			try(FileOutputStream fileOutputStream=new FileOutputStream(file)){
				fileOutputStream.write(model.getConvertedSqlScriptCharactersAsStringBuffer().toString().getBytes(StandardCharsets.UTF_8));
			}
		} catch (Exception e) {
			logger.error("Error saving SQL file",e);
		}
	}
}
