package lunartools.sqlrepeatabler.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlParser;
import lunartools.sqlrepeatabler.parser.SqlScript;

public class ConverterService {

	public ConverterService(SqlRepeatablerModel model) {}

	public SqlScript createSqlScript(File file) throws Exception{
		try(BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))){
			return SqlScript.createInstance(bufferedReader);
		}
	}

	public SqlBlock parseFile(SqlScript sqlScript) throws Exception{
		return SqlParser.parse(sqlScript);
	}

}
