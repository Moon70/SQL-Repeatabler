package lunartools.sqlrepeatabler.statements;

public class UseStatement implements Statement{
	public static final String COMMAND="USE";

	public UseStatement() {}

	@Override
	public void toSql(StringBuilder sb) throws Exception {}

}
