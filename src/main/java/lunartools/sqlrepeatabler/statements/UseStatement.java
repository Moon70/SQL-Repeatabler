package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import lunartools.sqlrepeatabler.parser.SqlString;

public class UseStatement implements Statement{
	public static final String COMMAND="USE";

	public UseStatement() {}

    @Override
    public void toSqlCharacters(ArrayList<SqlString> sqlCharacterLines) throws Exception {}

}
