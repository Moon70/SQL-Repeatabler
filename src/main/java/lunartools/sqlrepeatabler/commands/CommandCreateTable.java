package lunartools.sqlrepeatabler.commands;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.services.StringWriterLn;

public class CommandCreateTable extends Command{
    private static Logger logger = LoggerFactory.getLogger(CommandCreateTable.class);
    private Pattern patternStart=Pattern.compile(".*create table [\\[`](.*)[\\]`] \\(\\s*",Pattern.CASE_INSENSITIVE);
    private Pattern patternEndOfCreateTable=Pattern.compile(".*;\\s*",Pattern.CASE_INSENSITIVE);

    public boolean acceptLine(String line,BufferedReader bufferesReader, StringWriterLn writer) throws Exception {
        String tablename=null;
        ArrayList<String> createTableLines=new ArrayList<>();

        Matcher matcher=patternStart.matcher(line);
        if(!matcher.matches()) {
            return false;
        }
        tablename="["+matcher.group(1)+"]";
        if(!tablename.toLowerCase().startsWith("[dbo].")) {
            String newtablename="[dbo]."+tablename;
            line=line.replace(tablename, newtablename);
            tablename=newtablename;
        }

        while(true) {
            line=bufferesReader.readLine();
            if(line==null) {
                throw new Exception("Unexpected end of file while processing CREATE TABLE");
            }

            matcher=patternEndOfCreateTable.matcher(line);
            if(matcher.matches()) {
                if(line.contains("engine")) {
                    logger.info("ignoring parameter 'engine', which is MySQL specific");
                }
                break;
            }
            if(line.contains("auto_increment")) {
                logger.info("replaced 'auto_increment' (MySQL) with 'identity' (MSSQL):");
                logger.info("--- "+line);
                line=line.replace("auto_increment","identity");
                logger.info("+++ "+line);
            }
            line=replaceBackTicksWithSquareBrackets(line);
            if(line.contains("datetime(")) {
                int p=line.indexOf("datetime(");
                int p2=line.indexOf(")",p);
                line=line.substring(0,p+"datetime".length())+line.substring(p2+1);
            }
            createTableLines.add(line);
        }

        writer.writeln("IF OBJECT_ID(N'"+tablename+"', 'U') IS NULL");
        writer.writeln("BEGIN");
        writer.writeln("    create table "+tablename+" (");
        for(String lineX:createTableLines) {
            writer.writeln(lineX);
        }
        writer.writeln("    );");
        writer.writeln("END;");
        return true;
    }

    private String replaceBackTicksWithSquareBrackets(String s) throws Exception {
        StringBuffer sb=new StringBuffer(s);
        int p=0;
        while((p=sb.indexOf("`",p))!=-1) {
            sb.setCharAt(p,'[');
            p=sb.indexOf("`",p);
            if(p==-1) {
                throw new Exception("number of backticks must be even: "+s);
            }
            sb.setCharAt(p,']');
        }
        return sb.toString();
    }
    
}
