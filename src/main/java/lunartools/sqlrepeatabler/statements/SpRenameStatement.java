package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.parser.SqlParser;
import lunartools.sqlrepeatabler.parser.SqlScriptLine;
import lunartools.sqlrepeatabler.parser.Token;

public class SpRenameStatement implements Statement{
	private static Logger logger = LoggerFactory.getLogger(SpRenameStatement.class);
	public static final String COMMAND="SP_RENAME";
	private Token tableName;
	private Token oldName;
	private Token newName;
	private Token type;

	public SpRenameStatement(Token table,Token oldName, Token newName, Token type) {
		this.tableName=table;
		this.oldName=oldName;
		this.newName=newName;
		this.type=type;
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
    public void toSqlCharacters(ArrayList<SqlScriptLine> sqlCharacterLines) throws Exception {
        // TODO Auto-generated method stub
        
    }

}
