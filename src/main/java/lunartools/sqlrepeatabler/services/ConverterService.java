package lunartools.sqlrepeatabler.services;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.parser.SqlParser;

public class ConverterService {
	private static Logger logger = LoggerFactory.getLogger(ConverterService.class);

	public ConverterService(SqlRepeatablerModel model) {}

	public StringBuffer parseFile(File file) throws Exception{
		try {
			StringBuilder sb=SqlParser.parse(file);
			//TODO: migrate from StringBuffer to StringBuilder
			return new StringBuffer(sb);
		} catch (Exception e) {
			logger.error("error while parsing file: "+file, e);
			return new StringBuffer();
		}
	}

}
