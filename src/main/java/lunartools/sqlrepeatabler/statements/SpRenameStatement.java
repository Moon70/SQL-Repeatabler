package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlParser;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

public class SpRenameStatement implements Statement{
	private static Logger logger = LoggerFactory.getLogger(SpRenameStatement.class);
	public static final String COMMAND="SP_RENAME";
	private Token tableName;
	private Token tableNameWithoutBrackets;
	private Token oldName;
	private Token newName;
	private Token newNameWithoutBrackets;
	private Token type;

	public SpRenameStatement(Token table,Token oldName, Token newName, Token type) throws CloneNotSupportedException {
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
	public void toSql(StringBuilder sb) throws Exception {
		sb.append(String.format("IF COL_LENGTH ('%s','%s') IS NULL", removeBrackets(tableName.toString()),removeBrackets(newName.toString()))).append(SqlParser.CRLF);
		sb.append(String.format("BEGIN")).append(SqlParser.CRLF);
		sb.append(String.format("\texec sp_rename '%s.%s', %s, %s;", tableName,oldName,newName,type)).append(SqlParser.CRLF);
		sb.append(String.format("END;")).append(SqlParser.CRLF);
	}

	public String removeBrackets(String s) {
		if(s.charAt(0)=='[' && s.charAt(s.length()-1)==']') {
			return s.substring(1,s.length()-1);
		}else {
			return s;
		}
	}

	@Override
	public void toSqlCharacters(ArrayList<SqlString> sqlCharacterLines) throws Exception {
		sqlCharacterLines.add(SqlString.createSqlStringFromString("IF COL_LENGTH ('%s','%s') IS NULL", Category.INSERTED, tableNameWithoutBrackets,newNameWithoutBrackets));
		sqlCharacterLines.add(SqlString.createSqlStringFromString("BEGIN", Category.INSERTED));
		sqlCharacterLines.add(SqlString.createSqlStringFromString("\texec sp_rename '%s.%s', %s, %s;", Category.INSERTED, tableName,oldName,newName,type));
		sqlCharacterLines.add(SqlString.createSqlStringFromString("END;", Category.INSERTED));
	}

}
