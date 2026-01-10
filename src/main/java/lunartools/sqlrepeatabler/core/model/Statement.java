package lunartools.sqlrepeatabler.core.model;

public interface Statement {

	public abstract void toSqlCharacters(SqlBlock sqlBlock);

}
