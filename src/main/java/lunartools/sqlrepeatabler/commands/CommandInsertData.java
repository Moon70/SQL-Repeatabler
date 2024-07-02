package lunartools.sqlrepeatabler.commands;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.services.StringWriterLn;

public class CommandInsertData extends Command{
    private static Logger logger = LoggerFactory.getLogger(CommandInsertData.class);
    private Pattern patternStart=Pattern.compile("SET IDENTITY_INSERT (.*) ON;\\s*",Pattern.CASE_INSENSITIVE);
    private Pattern patternEnd=Pattern.compile("SET IDENTITY_INSERT (.*) OFF;\\s*",Pattern.CASE_INSENSITIVE);
    private Pattern patternInsert=Pattern.compile("INSERT INTO .*(\\(.*\\)) VALUES.*",Pattern.CASE_INSENSITIVE);
    private Pattern patternDataInInsertLine=Pattern.compile(".* VALUES (\\(.*\\))[,;]\\s*",Pattern.CASE_INSENSITIVE);
    private Pattern patternDataInDataLine=Pattern.compile("(\\(.*\\)),?+\\s*",Pattern.CASE_INSENSITIVE);

    @Override
    public boolean acceptLine(String line, BufferedReader bufferesReader, StringWriterLn writer) throws Exception {
        String tablename=null;
        String insert=null;
        ArrayList<String> daten=new ArrayList<>();

        Matcher matcher=patternStart.matcher(line);
        if(!matcher.matches()) {
            return false;
        }
        tablename=striptServerNameFromTableName(matcher.group(1));

        while(true) {
            line=bufferesReader.readLine();
            if(line==null) {
                throw new Exception("Unexpected end of file while processing INSERT");
            }

            matcher=patternInsert.matcher(line);
            if(matcher.matches()) {
                if(tablename==null) {
                    throw new Exception("'INSERT INTO ohne 'SET IDENTITY ON'");
                }
                insert=matcher.group(1);
                matcher=patternDataInInsertLine.matcher(line);
                if(matcher.matches()) {
                    daten.add(matcher.group(1));
                }
                continue;
            }

            matcher=patternDataInDataLine.matcher(line);
            if(matcher.matches()) {
                daten.add(matcher.group(1));
                continue;
            }

            matcher=patternEnd.matcher(line);
            if(matcher.matches()) {
                String tablename2=striptServerNameFromTableName(matcher.group(1));

                if(!tablename.equals(tablename2)) {
                    throw new Exception("Unexpected 'SET IDENTIY OFF' for Table >"+tablename+"<");
                }
                break;
            }
        }

        if(daten.size()==0) {
            logger.warn(CommandInsertData.class.getSimpleName()+": Keine Values erkannt");
        }
        for(int i=0;i<daten.size();i++) {
            String data=daten.get(i);
            String number=data.substring(1,data.indexOf(","));

            writer.writeln("if (select COUNT(*) from "+tablename+" where ID="+number+")=0");
            writer.writeln("Begin");
            writer.writeln("SET IDENTITY_INSERT "+tablename+" ON; ");
            writer.writeln("INSERT INTO [dbo].["+tablename+"]"+insert+" VALUES");
            writer.writeln(data);
            writer.writeln("SET IDENTITY_INSERT "+tablename+" OFF;");
            writer.writeln("End");

        }
        return true;
    }

    private String striptServerNameFromTableName(String tablename) {
        int p=tablename.toLowerCase().indexOf("[dbo]");
        if(p>0) {
            tablename=tablename.substring(p+6);
        }
        if(tablename.startsWith("[")) {
            tablename=tablename.substring(1,tablename.length()-2);
        }
        return tablename;
    }

}
