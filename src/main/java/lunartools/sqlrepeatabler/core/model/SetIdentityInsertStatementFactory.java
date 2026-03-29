package lunartools.sqlrepeatabler.core.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.core.processing.StatementTokenizer;

public class SetIdentityInsertStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(SetIdentityInsertStatementFactory.class);

	@Override
	public Statement createStatement(StatementTokenizer statementTokenizer) throws SqlParserException{
		if(!statementTokenizer.startsWithIgnoreCase(SetIdentityInsertStatement.COMMAND)) {
			return null;
		}
		logger.debug("Statement: "+statementTokenizer.toString());
		statementTokenizer.markIgnore();

		Token tokenStatement=statementTokenizer.nextToken(SetIdentityInsertStatement.COMMAND);
		tokenStatement=tokenStatement.toUpperCase();

		TableName tableName=TableName.createInstanceByConsuming(statementTokenizer);
		tableName.markIgnore();
		logger.debug("Table: "+tableName);

		statementTokenizer.stripWhiteSpaceRight();
		if(statementTokenizer.charAt(statementTokenizer.size()-1).getChar()==';') {
			statementTokenizer.deleteCharAt(statementTokenizer.size()-1);
		}
		Token parameters=statementTokenizer.nextToken();
		logger.debug("Parameters: "+parameters);

		return new SetIdentityInsertStatement(tokenStatement,tableName,parameters);
	}

}
