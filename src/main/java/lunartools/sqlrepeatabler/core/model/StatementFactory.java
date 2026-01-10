package lunartools.sqlrepeatabler.core.model;

import java.io.EOFException;

public abstract class StatementFactory {

	public abstract boolean match(String line);

	public abstract Statement createStatement(SqlScript sqlScript) throws SqlParserException,EOFException;

}
