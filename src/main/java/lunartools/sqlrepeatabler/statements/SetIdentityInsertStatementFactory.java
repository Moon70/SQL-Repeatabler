package lunartools.sqlrepeatabler.statements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.parser.StatementTokenizer;
import lunartools.sqlrepeatabler.parser.Token;

public class SetIdentityInsertStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(SetIdentityInsertStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().toUpperCase().startsWith(SetIdentityInsertStatement.COMMAND);
	}

	@Override
	public Statement createStatement(SqlScript sqlScript) throws Exception{
		if(!match(sqlScript.peekLineAsString())) {
			throw new Exception("Illegal factory call");
		}
		if(logger.isTraceEnabled()) {
			logger.trace("parsing statement");
		}

		StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
		logger.info("statement: "+statementTokenizer.toString());
		
		statementTokenizer.nextToken().setCategory(Category.STATEMENT);//skip 'SET' token	
		statementTokenizer.nextToken().setCategory(Category.COMMAND);//skip 'IDENTITY_INSERT' token

		TableName tableName=TableName.createInstanceByConsuming(statementTokenizer);
		logger.debug(tableName.toString());

		statementTokenizer.stripWhiteSpaceRight();
		if(statementTokenizer.charAt(statementTokenizer.length()-1).getChar()==';') {
			statementTokenizer.deleteCharAt(statementTokenizer.length()-1);
		}
		Token parameters=statementTokenizer.toToken();
		parameters.setCategory(Category.PARAMETER);
		return new SetIdentityInsertStatement(tableName,parameters);
	}

}
