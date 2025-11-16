package lunartools.sqlrepeatabler.statement;

import lunartools.sqlrepeatabler.parser.SqlBlock;

public class UseStatement implements Statement{
	public static final String COMMAND="USE";

	public UseStatement() {}

    @Override
    public void toSqlCharacters(SqlBlock sqlBlock) throws Exception {}

}
