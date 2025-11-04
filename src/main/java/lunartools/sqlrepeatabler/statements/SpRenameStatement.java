package lunartools.sqlrepeatabler.statements;

import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

public class SpRenameStatement implements Statement{
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
	public void toSqlCharacters(SqlBlock sqlBlock) throws Exception {
		sqlBlock.add(SqlString.createSqlStringFromString("IF COL_LENGTH ('%s','%s') IS NULL", Category.INSERTED, tableNameWithoutBrackets,newNameWithoutBrackets));
		sqlBlock.add(SqlString.createSqlStringFromString("BEGIN", Category.INSERTED));
		sqlBlock.add(SqlString.createSqlStringFromString("\tEXEC %s '%s.%s', %s, %s;", Category.INSERTED,tokenStatement, tableName,oldName,newName,type));
		sqlBlock.add(SqlString.createSqlStringFromString("END;", Category.INSERTED));
	}

}
