package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

public class SpRenameStatement implements Statement{
	private static Logger logger = LoggerFactory.getLogger(SpRenameStatement.class);
	public static final String COMMAND="SP_RENAME";
	private Token tokenStatement;
	private Token tableName;
	private Token tableNameWithoutBrackets;
	private Token oldName;
	private Token newName;
	private Token newNameWithoutBrackets;
	private Token type;

	public SpRenameStatement(Token statement,Token table,Token oldName, Token newName, Token type) throws CloneNotSupportedException {
		this.tokenStatement=statement;
		this.tableName=table;
		this.oldName=oldName;
		this.newName=newName;
		this.type=type;
		this.tableNameWithoutBrackets=(Token)table.clone();
		tableNameWithoutBrackets.removeEnclosing('[',']');
		this.newNameWithoutBrackets=(Token)newName.clone();
		newNameWithoutBrackets.removeEnclosing('[',']');
	}

	@Override
	public void toSqlCharacters(ArrayList<SqlString> sqlCharacterLines) throws Exception {
		sqlCharacterLines.add(SqlString.createSqlStringFromString("IF COL_LENGTH ('%s','%s') IS NULL", Category.INSERTED, tableNameWithoutBrackets,newNameWithoutBrackets));
		sqlCharacterLines.add(SqlString.createSqlStringFromString("BEGIN", Category.INSERTED));
		sqlCharacterLines.add(SqlString.createSqlStringFromString("\tEXEC %s '%s.%s', %s, %s;", Category.INSERTED,tokenStatement, tableName,oldName,newName,type));
		sqlCharacterLines.add(SqlString.createSqlStringFromString("END;", Category.INSERTED));
	}

}
