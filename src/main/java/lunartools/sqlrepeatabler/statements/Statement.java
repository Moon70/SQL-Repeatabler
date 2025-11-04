package lunartools.sqlrepeatabler.statements;

import lunartools.sqlrepeatabler.parser.SqlBlock;

public interface Statement {

    public abstract void toSqlCharacters(SqlBlock sqlBlock)throws Exception;

}
