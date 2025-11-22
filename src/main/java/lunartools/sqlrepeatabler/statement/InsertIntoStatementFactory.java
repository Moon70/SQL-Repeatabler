package lunartools.sqlrepeatabler.statement;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlParserException;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.parser.StatementTokenizer;
import lunartools.sqlrepeatabler.parser.TableName;
import lunartools.sqlrepeatabler.parser.Token;

public class InsertIntoStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(InsertIntoStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().toUpperCase().startsWith(InsertIntoStatement.COMMAND);
	}

	@Override
	public Statement createStatement(SqlScript sqlScript) throws SqlParserException{
		if(!match(sqlScript.peekLineAsString())) {
			throw new RuntimeException("Illegal factory call");
		}

		StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
		logger.info("Statement: "+statementTokenizer.toString());
		statementTokenizer.setBackgroundColor(null);

		Token tokenStatement=statementTokenizer.nextToken(InsertIntoStatement.COMMAND);
		tokenStatement.setCategory(Category.STATEMENT);
		tokenStatement=tokenStatement.toUpperCase();

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
			if(statementTokenizer.charAt(0).getChar()==',') {
				statementTokenizer.deleteCharAt(0);
			}
			Token tokenValues=statementTokenizer.nextToken('(', ')');
			tokenValues.setCategory(Category.COLUMNPARAMETER);
			columnValuesTokensList.add(tokenValues);
		}

		return new InsertIntoStatement(tokenStatement,tableName,tokenColumnNames,columnValuesTokensList);
	}

}
