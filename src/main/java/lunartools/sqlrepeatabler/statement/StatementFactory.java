package lunartools.sqlrepeatabler.statement;

import lunartools.sqlrepeatabler.parser.SqlScript;

public abstract class StatementFactory {

	public abstract boolean match(String line);

	public abstract Statement createStatement(SqlScript sqlScript) throws Exception;

}
