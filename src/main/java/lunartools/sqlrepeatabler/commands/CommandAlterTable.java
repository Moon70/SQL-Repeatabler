package lunartools.sqlrepeatabler.commands;

import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lunartools.sqlrepeatabler.services.StringWriterLn;

public class CommandAlterTable extends Command{
    private Pattern patternStart=Pattern.compile(".*alter table (\\[.*\\])\\s*",Pattern.CASE_INSENSITIVE);
    private Pattern patternEndOfAlterTable=Pattern.compile("\\s*",Pattern.CASE_INSENSITIVE);
    private Pattern patternAdd=Pattern.compile("\\s*add (\\[.*\\]) (.*);",Pattern.CASE_INSENSITIVE);
    private Pattern patternModify=Pattern.compile("\\s*modify column (\\[.*\\]) (.*);",Pattern.CASE_INSENSITIVE);

    public boolean acceptLine(String line,BufferedReader bufferesReader, StringWriterLn writer) throws Exception {
        String tablename=null;

        Matcher matcher=patternStart.matcher(line);
        if(!matcher.matches()) {
            return false;
        }
        tablename=matcher.group(1);

        while(true) {
            line=bufferesReader.readLine();
            if(line==null) {
                break;
            }

            matcher=patternEndOfAlterTable.matcher(line);
            if(matcher.matches()) {
                break;
            }

            matcher=patternAdd.matcher(line);
            if(matcher.matches()) {
                String fieldName=matcher.group(1);
                String fieldType=matcher.group(2);
                writer.writeln("IF COL_LENGTH ('"+withoutBrackets(tablename)+"','"+withoutBrackets(fieldName)+"') IS NULL");
                writer.writeln("BEGIN");
                writer.writeln("  ALTER TABLE "+tablename);
                writer.writeln("    ADD "+fieldName+" "+fieldType+";");
                writer.writeln("END;");
                continue;
            }

            matcher=patternModify.matcher(line);
            if(matcher.matches()) {
                String fieldName=matcher.group(1);
                String fieldType=matcher.group(2);
                writer.writeln("  ALTER TABLE "+tablename);
                writer.writeln("    ALTER COLUMN "+fieldName+" "+fieldType+";");
                continue;
            }

        }
        return true;
    }

    private String withoutBrackets(String s) {
        return s.substring(1,s.length()-1);
    }

}
