package lunartools.sqlrepeatabler.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlParserException;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.parser.StatementTokenizer;
import lunartools.sqlrepeatabler.parser.TableName;
import lunartools.sqlrepeatabler.parser.Token;

public class UpdateStatementFactory extends StatementFactory{
    private static Logger logger = LoggerFactory.getLogger(UpdateStatementFactory.class);

    @Override
    public boolean match(String line) {
        return line.trim().toUpperCase().startsWith(UpdateStatement.COMMAND);
    }

    @Override
    public Statement createStatement(SqlScript sqlScript) throws SqlParserException{
        if(!match(sqlScript.peekLineAsString())) {
            throw new RuntimeException("Illegal factory call");
        }

        StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
        logger.debug("Statement: "+statementTokenizer.toString());

        Token tokenStatement=statementTokenizer.nextToken(UpdateStatement.COMMAND);
        tokenStatement.setCategory(Category.STATEMENT);
        tokenStatement=tokenStatement.toUpperCase();

        TableName tableName=TableName.createInstanceByConsuming(statementTokenizer);
        logger.debug("Table: "+tableName.toString());

        if(!statementTokenizer.consumeCommandIgnoreCaseAndSpace("SET")) {
            try {
                statementTokenizer.nextToken().markError();
            } catch (Exception e) {}
            throw new SqlParserException("'SET' clause not found.",statementTokenizer.getLocation());
        }

        Token tokenSetData=statementTokenizer.nextTokenUntil("WHERE");
        logger.debug("tokenSet: "+tokenSetData);

        if(!statementTokenizer.consumeCommandIgnoreCaseAndSpace("WHERE")) {
            try {
                statementTokenizer.nextToken().markError();
            } catch (Exception e) {}
            throw new SqlParserException("'WHERE' keyword not found.",statementTokenizer.getLocation());
        }
        Token tokenWhereData=statementTokenizer.nextTokenUntil(";");
        logger.debug("tokenWhere: "+tokenWhereData);

        return new UpdateStatement(
                tokenStatement,
                tableName,
                tokenSetData,
                tokenWhereData);
    }

}
