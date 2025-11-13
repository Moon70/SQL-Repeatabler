package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.parser.StatementTokenizer;
import lunartools.sqlrepeatabler.parser.Token;

public class CreateTableStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(CreateTableStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().toUpperCase().startsWith(CreateTableStatement.COMMAND);
	}

	@Override
	public Statement createStatement(SqlScript sqlScript) throws Exception{
		if(!match(sqlScript.peekLineAsString())) {
			throw new Exception("Illegal factory call");
		}

		StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
		logger.info("Statement: "+statementTokenizer.toString());
		statementTokenizer.setBackgroundColor(null);
		
		Token tokenStatement=statementTokenizer.nextToken(CreateTableStatement.COMMAND);
		tokenStatement.setCategory(Category.STATEMENT);
		tokenStatement=tokenStatement.toUpperCase();
		
		TableName tableName=TableName.createInstanceByConsuming(statementTokenizer);
		if(tableName.isMySql()) {
			logger.warn("Script is most likely MySql flavour! Replacing backticks with square brackets!");
		}
		logger.debug("Table: "+tableName.toString());

		ArrayList<Token> tokens=new ArrayList<>();

		Token allCollumnsToken=statementTokenizer.nextToken('(',')');
		allCollumnsToken.removeEnclosing('(',')');
		Token[] columns=allCollumnsToken.split(',');
		for(int i=0;i<columns.length;i++) {
			if(tableName.isMySql()) {
				columns[i].fixMySqlDelimiter();
			}
			tokens.add(columns[i]);
		}

		Token tokenEngineParameters=statementTokenizer.nextTokenUntil(';');
		if(tokenEngineParameters!=null) {
			tokenEngineParameters.setCategory(Category.IGNORED);
		}
		return new CreateTableStatement(tokenStatement,tableName,tokens);
	}

}
