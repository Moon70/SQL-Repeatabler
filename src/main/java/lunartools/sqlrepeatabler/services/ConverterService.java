package lunartools.sqlrepeatabler.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.main.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlParser;
import lunartools.sqlrepeatabler.parser.SqlScript;

public class ConverterService {
    private static Logger logger = LoggerFactory.getLogger(ConverterService.class);

    public ConverterService(SqlRepeatablerModel model) {}

    public SqlScript createSqlScript(File file) throws Exception{
        Path path=file.toPath();
        String s;
        try {
            s=Files.readString(path, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            logger.warn("Input file charset is not UTF-8, trying to read as ISO-8859-15. Please check the converted script manually.");
            s=Files.readString(path, Charset.forName("ISO-8859-15"));
        }

        try(BufferedReader bufferedReader=new BufferedReader(new StringReader(s))){
            return SqlScript.createInstance(bufferedReader);
        }
    }

    public SqlBlock parseFile(SqlScript sqlScript) throws Exception{
        return SqlParser.parse(sqlScript);
    }

}
