package lunartools.sqlrepeatabler.core.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.core.processing.StatementTokenizer;

public class WhitespaceLineStatementFactory extends StatementFactory{
    private static Logger logger = LoggerFactory.getLogger(WhitespaceLineStatementFactory.class);

    @Override
    public Statement createStatement(StatementTokenizer statementTokenizer){
        if(statementTokenizer.size()!=0) {
            return null;
        }
        logger.debug("Statement: whitespace line");
        return new WhitespaceLineStatement();
    }

}
