package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
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
		if(!match(sqlScript.peekLine())) {
			throw new Exception("Illegal factory call");
		}
		if(logger.isTraceEnabled()) {
			logger.trace("parsing statement");
		}

		StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
		logger.info("statement: "+statementTokenizer.toString());

		statementTokenizer.nextToken();//skip 'INSERT' token	
		statementTokenizer.nextToken();//skip 'INTO' token

		TableName tableName=TableName.createInstanceByConsuming(statementTokenizer);
		logger.debug(tableName.toString());

		Token tokenColumnNames=statementTokenizer.nextToken('(',')');

		if(!statementTokenizer.consumePrefixIgnoreCaseAndSpace("VALUES")) {
			throw new Exception("Keyword VALUES not found");
		}

		ArrayList<Token> columnValuesTokensList= new ArrayList<>();
		while(statementTokenizer.hasNext()) {
			if(statementTokenizer.charAt(0).getChar()==',') {
				statementTokenizer.deleteCharAt(0);
			}
			Token tokenValues=statementTokenizer.nextToken('(', ')');
			columnValuesTokensList.add(tokenValues);
		}

		return new InsertIntoStatement(tableName,tokenColumnNames,columnValuesTokensList);
	}

}
