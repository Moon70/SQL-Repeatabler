package lunartools.sqlrepeatabler.commands;

import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lunartools.sqlrepeatabler.services.StringWriterLn;

public class CommandComment extends Command{
    private Pattern patternStart_MinusMinus=Pattern.compile("--.*",Pattern.CASE_INSENSITIVE);
    private Pattern patternStart_SlashAsterisk=Pattern.compile("/\\*.*\\*/.*",Pattern.CASE_INSENSITIVE);

    @Override
    public boolean acceptLine(String line, BufferedReader bufferesReader, StringWriterLn writer) throws Exception {
        Matcher matcher1=patternStart_MinusMinus.matcher(line);
        Matcher matcher2=patternStart_SlashAsterisk.matcher(line);
        if(matcher1.matches() || matcher2.matches()) {
            writer.writeln(line);
            return true;
        }
        return false;
    }

}
