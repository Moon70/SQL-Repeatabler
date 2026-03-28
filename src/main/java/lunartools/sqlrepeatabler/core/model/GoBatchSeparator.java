package lunartools.sqlrepeatabler.core.model;

public class GoBatchSeparator implements Statement{
	public static final String COMMAND="GO";

	public GoBatchSeparator() {}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock){}

}
