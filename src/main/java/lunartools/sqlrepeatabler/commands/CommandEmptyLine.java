package lunartools.sqlrepeatabler.commands;

import java.io.BufferedReader;

import lunartools.sqlrepeatabler.services.StringWriterLn;

public class CommandEmptyLine extends Command{

	@Override
	public boolean acceptLine(String line, BufferedReader bufferesReader, StringWriterLn writer) throws Exception {
		if(line.trim().length()>0) {
			return false;
		}
		writer.writeln("");
		return true;
	}

}
