package lunartools.sqlrepeatabler.statement;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.Token;

public class SetIdentityInsertStatement implements Statement{
	public static final String COMMAND="SET IDENTITY_INSERT";
//	private Token tokenStatement;
//	private TableName tableName;
//	private Token parameters;

	public SetIdentityInsertStatement(Token statement,TableName tableName,Token parameters) {
//		this.tokenStatement=statement;
//		this.tableName=tableName;
//		this.parameters=parameters;
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock) throws Exception {}

}
