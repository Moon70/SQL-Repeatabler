package lunartools.sqlrepeatabler.statement;

import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.TableName;
import lunartools.sqlrepeatabler.parser.Token;

public class SetIdentityInsertStatement implements Statement{
	public static final String COMMAND="SET IDENTITY_INSERT";

	public SetIdentityInsertStatement(Token statement,TableName tableName,Token parameters) {}

	//No output, because each 'INSERT' generates it´s own 'SET IDENTITY_INSERT'
	@Override
	public void toSqlCharacters(SqlBlock sqlBlock){}

}
