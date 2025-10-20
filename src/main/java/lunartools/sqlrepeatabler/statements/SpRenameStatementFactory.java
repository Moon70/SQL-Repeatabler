package lunartools.sqlrepeatabler.statements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		if(!match(sqlScript.peekLine())) {
			throw new Exception("Illegal factory call");
		}
		if(logger.isTraceEnabled()) {
			logger.trace("parsing statement");
		}

		StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
		logger.info("statement: "+statementTokenizer.toString());

		statementTokenizer.nextToken();//skip 'sp_rename' token	
		Token source=statementTokenizer.nextToken();
		Token newName=statementTokenizer.nextToken();
		Token type=statementTokenizer.nextToken();

		source.removeEnclosing('\'');
		Token[] subTokens=source.split('.');
		Token tableName=subTokens[0];
		Token oldName=subTokens[1];

		return new SpRenameStatement(tableName,oldName,newName,type);
	}
}
