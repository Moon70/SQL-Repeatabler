package lunartools.sqlrepeatabler.statements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;

public class SetIdentityInsertStatement implements Statement{
	private static Logger logger = LoggerFactory.getLogger(SetIdentityInsertStatement.class);
	public static final String COMMAND="SET IDENTITY_INSERT";
//	private TableName tableName;
//	private String parameters;

	public SetIdentityInsertStatement(TableName tableName,String parameters) {
//		this.tableName=tableName;
//		this.parameters=parameters;
	}

	@Override
	public void toSql(StringBuilder sb) throws Exception {
		//sb.append(String.format("SET IDENTITY_INSERT %s %s;",tableName.getFullSchemaAndName(),parameters)).append(Parser.CRLF);
	}

}
