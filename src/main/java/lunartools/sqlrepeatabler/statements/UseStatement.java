package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import lunartools.sqlrepeatabler.parser.SqlScriptLine;

public class UseStatement implements Statement{
	public static final String COMMAND="USE";

	public UseStatement() {}

	@Override
	public void toSql(StringBuilder sb) throws Exception {}

    @Override
    public void toSqlCharacters(ArrayList<SqlScriptLine> sqlCharacterLines) throws Exception {
        // TODO Auto-generated method stub
        
    }

}
