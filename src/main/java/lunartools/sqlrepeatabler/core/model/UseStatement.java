package lunartools.sqlrepeatabler.core.model;

public class UseStatement implements Statement{
	public static final String COMMAND="USE";

	public UseStatement() {}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock){}

}
