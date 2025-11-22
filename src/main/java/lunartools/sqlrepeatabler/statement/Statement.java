package lunartools.sqlrepeatabler.statement;

import lunartools.sqlrepeatabler.parser.SqlBlock;

public interface Statement {

	public abstract void toSqlCharacters(SqlBlock sqlBlock);

}
