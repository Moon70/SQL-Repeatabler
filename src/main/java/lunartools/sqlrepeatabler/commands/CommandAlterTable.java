package lunartools.sqlrepeatabler.commands;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lunartools.sqlrepeatabler.services.StringWriterLn;

public class CommandAlterTable extends Command{
	private Pattern patternStart=Pattern.compile(".*alter table (\\[.*\\])\\s*");
    private Pattern patternEndOfAlterTable=Pattern.compile("\\s*");
    private Pattern patternAdd=Pattern.compile("\\s*add (\\[.*\\]) (.*);");
    private Pattern patternModify=Pattern.compile("\\s*modify column (\\[.*\\]) (.*);");

	public boolean acceptLine(String line,BufferedReader bufferesReader, StringWriterLn writer) throws Exception {
		String tablename=null;
		//ArrayList<String> alterTableLines=new ArrayList<>();
		
		Matcher matcher=patternStart.matcher(line);
		if(!matcher.matches()) {
			return false;
		}
		tablename=matcher.group(1);
//		if(!tablename.toLowerCase().startsWith("[dbo].")) {
//			String newtablename="[dbo]."+tablename;
//			line=line.replace(tablename, newtablename);
//			tablename=newtablename;
//		}
		//alterTableLines.add(line);
		
		while(true) {
			line=bufferesReader.readLine();
			System.out.println(": "+line);
			if(line==null) {
				throw new Exception("Unexpected end of file while processing ALTER TABLE");
			}
			
			matcher=patternEndOfAlterTable.matcher(line);
			if(matcher.matches()) {
				//createTableLines.add(line);
				break;
			}

            matcher=patternAdd.matcher(line);
            if(matcher.matches()) {
                String fieldName=matcher.group(1);
                String fieldType=matcher.group(2);
                System.out.println("fieldName: "+fieldName);
                System.out.println("fieldType: "+fieldType);
                writer.writeln("IF COL_LENGTH ('"+withoutBrackets(tablename)+"','"+withoutBrackets(fieldName)+"') IS NULL");
                writer.writeln("BEGIN");
                writer.writeln("  ALTER TABLE "+tablename);
                writer.writeln("    ADD "+fieldName+" "+fieldType+";");
                writer.writeln("END;");
                break;
            }

            matcher=patternModify.matcher(line);
            if(matcher.matches()) {
                String fieldName=matcher.group(1);
                String fieldType=matcher.group(2);
                System.out.println("fieldName: "+fieldName);
                System.out.println("fieldType: "+fieldType);
//                writer.writeln("IF COL_LENGTH ('"+withoutBrackets(tablename)+"','"+withoutBrackets(fieldName)+"') IS NULL");
                writer.writeln("BEGIN");
                writer.writeln("  ALTER TABLE "+tablename);
                writer.writeln("    ALTER COLUMN "+fieldName+" "+fieldType+";");
//                writer.writeln("END;");
                break;
            }

		}

		return true;
	}
	
	private String withoutBrackets(String s) {
	    return s.substring(1,s.length()-1);
	}
	
}
