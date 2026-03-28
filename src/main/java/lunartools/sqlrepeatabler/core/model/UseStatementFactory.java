package lunartools.sqlrepeatabler.core.model;

import java.io.EOFException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.core.processing.StatementTokenizer;

public class UseStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(UseStatementFactory.class);

	@Override
	public Statement createStatement(StatementTokenizer statementTokenizer) throws EOFException{
		if(!statementTokenizer.startsWithIgnoreCase(UseStatement.COMMAND)) {
			return null;
		}
		SqlString sqlString=statementTokenizer.consumeLine();
		logger.debug("Statement: "+sqlString);

		logger.warn(String.format("Ignoring statement %s. %s", UseStatement.COMMAND,statementTokenizer.getLocation()));
		sqlString.setCategory(Category.IGNORED);

		return new UseStatement();
	}

}
