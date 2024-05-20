package lunartools.sqlrepeatabler.commands;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lunartools.sqlrepeatabler.services.StringWriterLn;

public class CommandInsertData extends Command{
	private Pattern patternStart=Pattern.compile("SET IDENTITY_INSERT (.*) ON;\\s*");
	private Pattern patternEnd=Pattern.compile("SET IDENTITY_INSERT (.*) OFF;\\s*");
	private Pattern patternInsert=Pattern.compile("INSERT INTO .*(\\(.*\\)) VALUES");
	private Pattern patternData=Pattern.compile("(\\(.*\\))[,;]\\s*");

	@Override
	public boolean acceptLine(String line, BufferedReader bufferesReader, StringWriterLn writer) throws Exception {
		String tablename=null;
		String insert=null;
		ArrayList<String> daten=new ArrayList<>();

		Matcher matcher=patternStart.matcher(line);
		if(!matcher.matches()) {
			return false;
		}
		tablename=matcher.group(1);
		
		while(true) {
			line=bufferesReader.readLine();
//			writer.println(": "+line);
			if(line==null) {
				throw new Exception("Unexpected emd of file while processing INSERT");
			}
			
			matcher=patternInsert.matcher(line);
			if(matcher.matches()) {
				if(tablename==null) {
					throw new Exception("'INSERT INTO ohne 'SET IDENTITY ON'");
				}
				insert=matcher.group(1);
				continue;
			}

			matcher=patternData.matcher(line);
			if(matcher.matches()) {
				daten.add(matcher.group(1));
				continue;
			}
			
			matcher=patternEnd.matcher(line);
			if(matcher.matches()) {
				if(!tablename.equals(matcher.group(1))) {
					throw new Exception("Unexpected 'SET IDENTIY OFF' for Table >"+tablename+"<");
				}
				break;
			}
		}

		for(int i=0;i<daten.size();i++) {
			String data=daten.get(i);
//			System.out.println(data);
			String number=data.substring(1,data.indexOf(","));
//			System.out.println(number);
			
			writer.writeln("if (select COUNT(*) from "+tablename+" where ID="+number+")=0");
			writer.writeln("Begin");
			writer.writeln("SET IDENTITY_INSERT "+tablename+" ON; ");
			writer.writeln("INSERT INTO [dbo].["+tablename+"]"+insert+" VALUES");
			writer.writeln(data);
			writer.writeln("SET IDENTITY_INSERT "+tablename+" OFF;");
			writer.writeln("End");
			
		}


		return false;
	}

}
