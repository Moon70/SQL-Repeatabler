package lunartools.sqlrepeatabler.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.SqlParser;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;

public class ConverterService {
	private static Logger logger = LoggerFactory.getLogger(ConverterService.class);
	private SqlRepeatablerModel model;

	public ConverterService(SqlRepeatablerModel model) {
		this.model=model;
	}

	public StringBuffer parseFile(File file) throws Exception{
		try {
			StringBuilder sb=SqlParser.parse(file);
			//TODO: migrate from StringBuffer to StringBuilder
			return new StringBuffer(sb);
			
//			ArrayList<Command> commands=CommandImplementationFactory.getCommandImplementations(Command.class.getPackage().getName(),Command.class.getSimpleName());
//
//			try(BufferedReader bufferedReader=new BufferedReader(new FileReader((file)))){
//				StringBuffer sb=new StringBuffer();
//				while(true) {
//					String line=bufferedReader.readLine();
//					if(line==null) {
//						break;
//					}
//					line=line.trim();
//					sb.append(line);
//					sb.append("\n");
//				}
//			}
//
//			try(BufferedReader bufferedReader=new BufferedReader(new FileReader((file)))){
//				StringWriterLn stringWriterLn=new StringWriterLn();
//				mainloop:
//					while(true) {
//						String line=bufferedReader.readLine();
//						if(line==null) {
//							break;
//						}
//
//						for(Command command:commands) {
//							if(command.acceptLine(line, bufferedReader,stringWriterLn)) {
//								continue mainloop;
//							}
//						}
//						throw new Exception("unexpected line: "+line);
//
//					}
//				return stringWriterLn.getBuffer();
//			}
		} catch (Exception e) {
			logger.error("error while parsing file: "+file, e);
			return new StringBuffer();
		}
	}

}
