package lunartools.sqlrepeatabler.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.SqlRepeatablerController;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.commands.Command;
import lunartools.sqlrepeatabler.commands.CommandAlterTable;
import lunartools.sqlrepeatabler.commands.CommandComment;
import lunartools.sqlrepeatabler.commands.CommandCreateTable;
import lunartools.sqlrepeatabler.commands.CommandEmptyLine;
import lunartools.sqlrepeatabler.commands.CommandInsertData;

public class ConverterService {
	private static Logger logger = LoggerFactory.getLogger(ConverterService.class);
	private SqlRepeatablerModel model;
	private SqlRepeatablerController controller;

	public ConverterService(SqlRepeatablerModel model,SqlRepeatablerController controller) {
		this.model=model;
		this.controller=controller;
	}

	public void parseFile(File file) throws Exception{
		try {
			ArrayList<Command> commands=new ArrayList<>();
			commands.add(new CommandAlterTable());
			commands.add(new CommandCreateTable());
			commands.add(new CommandInsertData());
			commands.add(new CommandComment());
			commands.add(new CommandEmptyLine());


			try(BufferedReader bufferedReader=new BufferedReader(new FileReader((file)))){
				StringBuffer sb=new StringBuffer();
				while(true) {
					String line=bufferedReader.readLine();
					if(line==null) {
						break;
					}
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
