package lunartools.sqlrepeatabler.core.model;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.core.processing.StatementTokenizer;

public class InsertStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(InsertStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().toUpperCase().startsWith(InsertStatement.COMMAND);
	}

	@Override
	public Statement createStatement(SqlScript sqlScript) throws SqlParserException{
		if(!match(sqlScript.peekLineAsString())) {
			throw new RuntimeException("Illegal factory call");
		}

		StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
		logger.debug("Statement: "+statementTokenizer.toString());
		statementTokenizer.setBackgroundColor(null);

		Token tokenStatement=statementTokenizer.nextToken(InsertStatement.COMMAND);
		tokenStatement.setCategory(Category.STATEMENT);
		tokenStatement=tokenStatement.toUpperCase();

		statementTokenizer.consumeCommandIgnoreCaseAndSpace("INTO");
		
		TableName tableName=TableName.createInstanceByConsuming(statementTokenizer);
		logger.debug("Table: "+tableName.toString());

		Token tokenColumnNames=statementTokenizer.nextToken('(',')');
		tokenColumnNames.setCategory(Category.COLUMN);

		Token tokenValuesCommand=statementTokenizer.nextToken("VALUES");
		if(tokenValuesCommand==null) {
			throw new SqlParserException("Keyword VALUES not found",statementTokenizer.getLocation());
		}
		tokenValuesCommand.setCategory(Category.COMMAND);

		ArrayList<Token> columnValuesTokensList= new ArrayList<>();
		while(statementTokenizer.hasNext()) {
		    Token tokenValues=statementTokenizer.nextToken('(', ')');
		    tokenValues.setCategory(Category.COLUMNPARAMETER);
		    columnValuesTokensList.add(tokenValues);
		    statementTokenizer.stripWhiteSpaceLeft();
			if(statementTokenizer.charAt(0).getChar()!=',') {
			    break;
			}
			statementTokenizer.deleteCharAt(0);
		}

		return new InsertStatement(tokenStatement,tableName,tokenColumnNames,columnValuesTokensList);
	}

}
