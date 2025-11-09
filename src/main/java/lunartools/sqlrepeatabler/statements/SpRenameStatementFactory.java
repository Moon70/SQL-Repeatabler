package lunartools.sqlrepeatabler.statements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.parser.StatementTokenizer;
import lunartools.sqlrepeatabler.parser.Token;

public class SpRenameStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(SpRenameStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().toUpperCase().startsWith(SpRenameStatement.COMMAND);
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
		logger.info("Statement: "+statementTokenizer.toString());

		Token tokenStatement=statementTokenizer.nextToken(SpRenameStatement.COMMAND);
		statementTokenizer.stripWhiteSpaceLeft();
		tokenStatement.setCategory(Category.STATEMENT);

		Token source=statementTokenizer.nextToken();
		Token newName=statementTokenizer.nextToken();
		newName.setCategory(Category.COLUMN);
		Token type=statementTokenizer.nextToken();
		type.setCategory(Category.PARAMETER);

		source.removeEnclosing('\'');
		Token[] subTokens=source.split('.');
		Token tableName=subTokens[0];
		tableName.setCategory(Category.TABLE);
		Token oldName=subTokens[1];
		oldName.setCategory(Category.COLUMN);

		return new SpRenameStatement(tokenStatement,tableName,oldName,newName,type);
	}
}
