package lunartools.sqlrepeatabler.statements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.SqlParser;

public class SpRenameStatement implements Statement{
	private static Logger logger = LoggerFactory.getLogger(SpRenameStatement.class);
	public static final String COMMAND="SP_RENAME";
	private String tableName;
	private String oldName;
	private String newName;
	private String type;

	public SpRenameStatement(String table,String oldName, String newName, String type) {
		this.tableName=table;
		this.oldName=oldName;
		this.newName=newName;
		this.type=type;
	}

	@Override
	public void toSql(StringBuilder sb) throws Exception {
		sb.append(String.format("IF COL_LENGTH ('%s','%s') IS NULL", removeBrackets(tableName),removeBrackets(newName))).append(SqlParser.CRLF);
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

}
