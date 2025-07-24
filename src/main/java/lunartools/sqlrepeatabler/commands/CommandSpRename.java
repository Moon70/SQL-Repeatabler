package lunartools.sqlrepeatabler.commands;

import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.services.StringWriterLn;

public class CommandSpRename extends Command{
    private static Logger logger = LoggerFactory.getLogger(CommandSpRename.class);
    private Pattern patternStart=Pattern.compile(".*sp_rename\\s+'\\[([^\\]]+)]\\.\\[([^\\]]+)]',\\s*\\[([^\\]]+)],\\s*\\[([^\\]]+)]\\s*;\\s*$",Pattern.CASE_INSENSITIVE);

    public boolean acceptLine(String line,BufferedReader bufferesReader, StringWriterLn writer) throws Exception {
        String tablename=null;
        String columnNameOld=null;
        String columnNameNew=null;
        String objectName=null;

        Matcher matcher=patternStart.matcher(line);
        if(!matcher.matches()) {
            return false;
        }
        if(matcher.groupCount()!=4) {
            System.out.println("not 4 but "+matcher.groupCount());
            return false;
        }
        
        tablename=matcher.group(1);
        columnNameOld=matcher.group(2);
        columnNameNew=matcher.group(3);
        objectName=matcher.group(4);
        
        if(!objectName.equalsIgnoreCase("column")) {
            System.out.println("no column");
            return false;
        }

        writer.writeln("IF COL_LENGTH ('"+tablename+"','"+columnNameNew+"') IS NULL");
        writer.writeln("BEGIN");
        writer.writeln("   exec sp_rename '["+tablename+"].["+columnNameOld+"]', ["+columnNameNew+"], ["+objectName+"];");
        writer.writeln("END;");
        return true;
    }

}
