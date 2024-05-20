package lunartools.sqlrepeatabler.commands;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lunartools.sqlrepeatabler.services.StringWriterLn;

public class CommandCreateTable extends Command{
	private Pattern patternStart=Pattern.compile(".*create table (\\[.*\\]) \\(\\s*");
	private Pattern patternEndOfCreateTable=Pattern.compile(".*;\\s*");

	public boolean acceptLine(String line,BufferedReader bufferesReader, StringWriterLn writer) throws Exception {
		String tablename=null;
		ArrayList<String> createTableLines=new ArrayList<>();
		
		Matcher matcher=patternStart.matcher(line);
		if(!matcher.matches()) {
			return false;
		}
		tablename=matcher.group(1);
		if(!tablename.toLowerCase().startsWith("[dbo].")) {
			String newtablename="[dbo]."+tablename;
			line=line.replace(tablename, newtablename);
			tablename=newtablename;
		}
		createTableLines.add(line);
		
		while(true) {
			line=bufferesReader.readLine();
//			writer.println(": "+line);
			if(line==null) {
				throw new Exception("Unexpected emd of file while processing CREATE TABLE");
			}
			
			matcher=patternEndOfCreateTable.matcher(line);
			if(matcher.matches()) {
				createTableLines.add(line);
				break;
			}

			createTableLines.add(line);
		}

		writer.writeln("IF OBJECT_ID(N'"+tablename+"', 'U') IS NULL");
		writer.writeln("BEGIN");
		for(String lineX:createTableLines) {
			writer.writeln(lineX);
		}
		writer.writeln("END;");
		return true;
	}
	
}
