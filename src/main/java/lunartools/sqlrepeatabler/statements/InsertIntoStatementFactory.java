package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.parser.StatementTokenizer;
import lunartools.sqlrepeatabler.parser.Token;

public class InsertIntoStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(InsertIntoStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().toUpperCase().startsWith(InsertIntoStatement.COMMAND);
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

		Token tokenStatement=statementTokenizer.nextToken(InsertIntoStatement.COMMAND);
		tokenStatement.setCategory(Category.STATEMENT);
		tokenStatement=tokenStatement.toUpperCase();

		TableName tableName=TableName.createInstanceByConsuming(statementTokenizer);
		logger.debug(tableName.toString());

		Token tokenColumnNames=statementTokenizer.nextToken('(',')');
		tokenColumnNames.setCategory(Category.COLUMN);


		if(!statementTokenizer.consumePrefixIgnoreCaseAndSpace("VALUES")) {
			throw new Exception("Keyword VALUES not found");
		}

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
