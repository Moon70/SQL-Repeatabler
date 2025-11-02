package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

public class SetIdentityInsertStatement implements Statement{
	private static Logger logger = LoggerFactory.getLogger(SetIdentityInsertStatement.class);
	public static final String COMMAND="SET IDENTITY_INSERT";
	private Token tokenStatement;
	private TableName tableName;
	private Token parameters;

	public SetIdentityInsertStatement(Token statement,TableName tableName,Token parameters) {
		this.tokenStatement=statement;
		this.tableName=tableName;
		this.parameters=parameters;
	}

	@Override
	public void toSqlCharacters(ArrayList<SqlString> sqlCharacterLines) throws Exception {}

}
