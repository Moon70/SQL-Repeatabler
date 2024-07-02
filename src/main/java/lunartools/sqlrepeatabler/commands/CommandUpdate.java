package lunartools.sqlrepeatabler.commands;

import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.services.StringWriterLn;

public class CommandUpdate extends Command{
    private static Logger logger = LoggerFactory.getLogger(CommandUpdate.class);
    private Pattern patternStart=Pattern.compile("UPDATE.*;",Pattern.CASE_INSENSITIVE);

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
