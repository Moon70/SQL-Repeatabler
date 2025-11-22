package lunartools.sqlrepeatabler.statement;

import java.io.EOFException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.parser.StatementTokenizer;

public class UseStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(UseStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().toUpperCase().startsWith(UseStatement.COMMAND);
	}

	@Override
	public Statement createStatement(SqlScript sqlScript) throws EOFException{
		if(!match(sqlScript.peekLineAsString())) {
			throw new RuntimeException("Illegal factory call");
		}
		if(logger.isTraceEnabled()) {
			logger.trace("parsing statement");
		}

		StatementTokenizer statementTokenizer=sqlScript.consumeOneLineStatement();
		logger.debug("Statement: "+statementTokenizer.toString());
		logger.warn(String.format("Ignoring statement %s. %s", UseStatement.COMMAND,statementTokenizer.getLocation()));
		statementTokenizer.setCategory(Category.IGNORED);

		return new UseStatement();
	}

}
