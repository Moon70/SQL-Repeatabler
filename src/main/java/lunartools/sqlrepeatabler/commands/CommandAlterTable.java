package lunartools.sqlrepeatabler.commands;

import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lunartools.sqlrepeatabler.services.StringWriterLn;

public class CommandAlterTable extends Command{
    //https://regex101.com/
    //private Pattern patternStart=Pattern.compile(".*ALTER TABLE (\\[.*\\])[\\s+|$].*",Pattern.CASE_INSENSITIVE);
    private Pattern patternStart=Pattern.compile("ALTER TABLE (\\[[^\\]]+\\])",Pattern.CASE_INSENSITIVE);
    private Pattern patternEndOfAlterTable=Pattern.compile("\\s*",Pattern.CASE_INSENSITIVE);
    private Pattern patternAddConstraint=Pattern.compile(".*ADD CONSTRAINT\\s+(\\S+)\\s+(.*)\\s+\\(\\[(\\S+)\\]\\);.*",Pattern.CASE_INSENSITIVE);
    
    private Pattern patternAddField=Pattern.compile(".*ADD (\\[.*\\]) (.*);",Pattern.CASE_INSENSITIVE);
    private Pattern patternModify=Pattern.compile("\\s*modify column (\\[.*\\]) (.*);",Pattern.CASE_INSENSITIVE);

    public boolean acceptLine(String line,BufferedReader bufferesReader, StringWriterLn writer) throws Exception {
        String tablename=null;

        Matcher matcher=patternStart.matcher(line);
        if(!matcher.find()) {
            return false;
        }
        tablename=matcher.group(1);

        //check for further instruction in first line
        matcher=patternAddConstraint.matcher(line);
        if(matcher.matches()) {
            writeAddConstraint(writer,matcher,tablename);
        }
        matcher=patternAddField.matcher(line);
        if(matcher.matches()) {
            writeAddField(writer,matcher,tablename);
        }
        
        while(true) {
            line=bufferesReader.readLine();
            if(line==null) {
                break;
            }

            matcher=patternEndOfAlterTable.matcher(line);
            if(matcher.matches()) {
                break;
            }

            matcher=patternAddField.matcher(line);
            if(matcher.matches()) {
                writeAddField(writer,matcher,tablename);
                continue;
            }

            matcher=patternAddConstraint.matcher(line);
            if(matcher.matches()) {
                writeAddConstraint(writer,matcher,tablename);
                continue;
            }

            matcher=patternModify.matcher(line);
            if(matcher.matches()) {
                String fieldName=matcher.group(1);
                String fieldType=matcher.group(2);
                writer.writeln("  ALTER TABLE "+tablename);
                writer.writeln("    ALTER COLUMN "+fieldName+" "+fieldType+";");
                writer.writeln("");
                continue;
            }

        }
        return true;
    }

    private void writeAddField(StringWriterLn writer,Matcher matcher,String tablename) {
        String fieldName=matcher.group(1);
        String fieldType=matcher.group(2);
        writer.writeln("IF COL_LENGTH ('"+withoutBrackets(tablename)+"','"+withoutBrackets(fieldName)+"') IS NULL");
        writer.writeln("BEGIN");
        writer.writeln("  ALTER TABLE "+tablename);
        writer.writeln("    ADD "+fieldName+" "+fieldType+";");
        writer.writeln("END;");
        writer.writeln("");
    }

    private void writeAddConstraint(StringWriterLn writer,Matcher matcher,String tablename) {
        String constraintName=matcher.group(1);
        String constraintParameter=matcher.group(2);
        String column=matcher.group(3);
        writer.writeln("IF OBJECT_ID ('"+withoutBrackets(tablename)+"','"+withoutBrackets(constraintName)+"') IS NULL");
        writer.writeln("BEGIN");
        writer.writeln("  ALTER TABLE "+tablename);
        writer.writeln("    ADD CONSTRAINT "+constraintName+" "+constraintParameter+" (["+column+"]);");
        writer.writeln("END;");
        writer.writeln("");
    }
    
    private String withoutBrackets(String s) {
        if(s.charAt(0)=='[') {
            return s.substring(1,s.length()-1);
        }else {
            return s;
        }
    }

}
