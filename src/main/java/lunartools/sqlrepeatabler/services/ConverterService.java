package lunartools.sqlrepeatabler.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.CommandImplementationFactory;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.commands.Command;

public class ConverterService {
	private static Logger logger = LoggerFactory.getLogger(ConverterService.class);
	private SqlRepeatablerModel model;

	public ConverterService(SqlRepeatablerModel model) {
		this.model=model;
	}

	public void parseFile(File file) throws Exception{
		try {
			ArrayList<Command> commands=CommandImplementationFactory.getCommandImplementations(Command.class.getPackage().getName(),Command.class.getSimpleName());

			try(BufferedReader bufferedReader=new BufferedReader(new FileReader((file)))){
				StringBuffer sb=new StringBuffer();
				while(true) {
					String line=bufferedReader.readLine();
					if(line==null) {
						break;
					}
					line=line.trim();
					sb.append(line);
					sb.append("\n");
				}
				model.addInputData(sb.toString());
			}

			try(BufferedReader bufferedReader=new BufferedReader(new FileReader((file)))){
				StringWriterLn stringWriterLn=new StringWriterLn();
				mainloop:
					while(true) {
						String line=bufferedReader.readLine();
						if(line==null) {
							break;
						}

						for(Command command:commands) {
							if(command.acceptLine(line, bufferedReader,stringWriterLn)) {
								continue mainloop;
							}
						}
						throw new Exception("unexpected line: "+line);

					}
				model.addConvertedSqlScript(stringWriterLn.toString());
			}
		} catch (Exception e) {
			logger.error("error while parsing file: "+file, e);
		}
	}

}
