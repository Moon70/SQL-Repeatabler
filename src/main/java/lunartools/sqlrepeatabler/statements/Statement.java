package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import lunartools.sqlrepeatabler.parser.SqlScriptLine;

public interface Statement {

    public abstract void toSql(StringBuilder sb)throws Exception;

    public abstract void toSqlCharacters(ArrayList<SqlScriptLine> sqlCharacterLines)throws Exception;

}
