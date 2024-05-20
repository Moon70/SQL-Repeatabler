package lunartools.sqlrepeatabler.commands;

import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lunartools.sqlrepeatabler.services.StringWriterLn;

public class CommandComment extends Command{
	private Pattern patternStart=Pattern.compile("--.*");

	@Override
	public boolean acceptLine(String line, BufferedReader bufferesReader, StringWriterLn writer) throws Exception {
		Matcher matcher=patternStart.matcher(line);
		if(!matcher.matches()) {
			return false;
		}
		writer.writeln(line);
		return true;
	}

}
