package lunartools.sqlrepeatabler.core.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.core.processing.StatementTokenizer;

public class SetIdentityInsertStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(SetIdentityInsertStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().toUpperCase().startsWith(SetIdentityInsertStatement.COMMAND);
	}

	@Override
	public Statement createStatement(SqlScript sqlScript) throws SqlParserException{
		if(!match(sqlScript.peekLineAsString())) {
			throw new RuntimeException("Illegal factory call");
		}

		StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
		logger.debug("Statement: "+statementTokenizer.toString());

		Token tokenStatement=statementTokenizer.nextToken(SetIdentityInsertStatement.COMMAND);
		tokenStatement.setCategory(Category.STATEMENT);
		tokenStatement=tokenStatement.toUpperCase();

		TableName tableName=TableName.createInstanceByConsuming(statementTokenizer);
		logger.debug("Table: "+tableName.toString());

		statementTokenizer.stripWhiteSpaceRight();
		if(statementTokenizer.charAt(statementTokenizer.length()-1).getChar()==';') {
			statementTokenizer.deleteCharAt(statementTokenizer.length()-1);
		}
		Token parameters=statementTokenizer.toToken();
		parameters.setCategory(Category.PARAMETER);
		return new SetIdentityInsertStatement(tokenStatement,tableName,parameters);
	}

}
