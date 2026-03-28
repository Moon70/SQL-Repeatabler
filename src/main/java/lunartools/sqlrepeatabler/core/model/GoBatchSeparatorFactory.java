package lunartools.sqlrepeatabler.core.model;

import java.io.EOFException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.core.processing.StatementTokenizer;

public class GoBatchSeparatorFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(GoBatchSeparatorFactory.class);

	@Override
	public Statement createStatement(StatementTokenizer statementTokenizer) throws EOFException{
		if(!statementTokenizer.startsWithIgnoreCase(GoBatchSeparator.COMMAND)) {
			return null;
		}
		statementTokenizer.markIgnore();
		Token token=statementTokenizer.nextToken();
		logger.debug("Batch separator: "+token);

		logger.warn(String.format("Ignoring batch separator %s. %s. Check if the script needs to be split!", GoBatchSeparator.COMMAND,token.getLocation()));
		token.setCategory(Category.IGNORED);

		return new GoBatchSeparator();
	}

}
