package lunartools.sqlrepeatabler.core.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.core.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.core.model.SqlScript;

public class FileService {
	private static Logger logger = LoggerFactory.getLogger(FileService.class);

	public ArrayList<SqlScript> loadFiles(SqlRepeatablerModel model) throws Exception {
		ArrayList<SqlScript> sqlScripts=new ArrayList<>();
		File file=null;
		try {
			model.clearConvertedSqlScriptBlocks();
			model.clearInputPanel();
			ArrayList<File> files=model.getSqlInputFiles();

			for(int i=0;i<files.size();i++) {
				file=files.get(i);
				logger.debug("Reading: "+file);
				SqlScript sqlScript=createSqlScriptInstance(file);
				sqlScripts.add(sqlScript);
			}
			return sqlScripts;
		} catch (Exception e) {
			logger.error("Error processing sql file: {}",file,e);
			throw e;
		}
	}

	private SqlScript createSqlScriptInstance(File file) throws Exception{
		Path path=file.toPath();
		String s;
		try {
			s=Files.readString(path, StandardCharsets.UTF_8);
		} catch (MalformedInputException e) {
			logger.warn("Input file charset is not UTF-8, trying to read as ISO-8859-15. Please check the converted script manually.");
			s=Files.readString(path, Charset.forName("ISO-8859-15"));
		}

		try(BufferedReader bufferedReader=new BufferedReader(new StringReader(s))){
			return SqlScript.createInstance(bufferedReader);
		}
	}

	public void saveFile(File file,SqlRepeatablerModel model) throws IOException {
		try(FileOutputStream fileOutputStream=new FileOutputStream(file)){
			fileOutputStream.write(model.getConvertedSqlScriptCharactersAsStringBuffer().toString().getBytes(StandardCharsets.UTF_8));
		}
	}

}
