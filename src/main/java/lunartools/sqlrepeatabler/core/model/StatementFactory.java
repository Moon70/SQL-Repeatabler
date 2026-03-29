package lunartools.sqlrepeatabler.core.model;

import java.io.EOFException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.core.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.core.processing.StatementTokenizer;

public abstract class StatementFactory {
	private static Logger logger = LoggerFactory.getLogger(StatementFactory.class);

	public abstract Statement createStatement(StatementTokenizer statementTokenizer) throws SqlParserException,EOFException;

	void verifyColumnName(Token tokenColumnName) throws SqlParserException {
		String columnNameAsString=tokenColumnName.cloneWithoutDelimiters().toString();

		if(!columnNameAsString.toUpperCase().equals(columnNameAsString)) {
			tokenColumnName.markWarn();
			logger.warn(String.format("Column name not uppercase: %s ! %s",tokenColumnName,tokenColumnName.getLocation()));
		}
		if(columnNameAsString.charAt(columnNameAsString.length()-1)==' ') {
			tokenColumnName.markError();
			throw new SqlParserException(String.format("%s does not allow column names with trailing spaces: %s",SqlRepeatablerModel.PROGRAMNAME,tokenColumnName),tokenColumnName.getLocation());
		}
	}

}
