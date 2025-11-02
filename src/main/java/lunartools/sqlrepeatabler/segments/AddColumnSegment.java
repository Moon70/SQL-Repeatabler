package lunartools.sqlrepeatabler.segments;

import java.util.ArrayList;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlParser;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

public class AddColumnSegment extends Segment{
	private Token name;
	private Token parameters;

	public AddColumnSegment(Token name,Token parameters) {
		super(new Token("ADD",Category.COMMAND),name);
		this.name=name;
		this.parameters=parameters;
	}

	public void toSql(StringBuilder sb,TableName tableName,boolean mySql) throws Exception {
		sb.append(String.format("IF COL_LENGTH ('%s','%s') IS NULL",tableName.getFullNameWithoutDelimiterAsString(),getColumnNameWithoutDelimiters())).append(SqlParser.CRLF);
		sb.append(String.format("BEGIN")).append(SqlParser.CRLF);
		sb.append(String.format("\tALTER TABLE %s",tableName.getFullNameAsString())).append(SqlParser.CRLF);
		sb.append(String.format("\t\t%s %s %s;",getAction().toString(),name,parameters)).append(SqlParser.CRLF);
		sb.append(String.format("END;")).append(SqlParser.CRLF);
	}

	@Override
	public void toSqlCharacters(ArrayList<SqlString> sqlCharacterLines,Token tokenStatement,TableName tableName,boolean mySql) throws Exception {
        sqlCharacterLines.add(SqlString.createSqlStringFromString("IF COL_LENGTH ('%s','%s') IS NULL", Category.INSERTED,tableName.getFullNameWithoutDelimiter(),getName().cloneWithoutDelimiters()));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("BEGIN", Category.INSERTED));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("\t%s %s", Category.INSERTED,tokenStatement,tableName.getFullName()));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("\t\t%s %s %s;", Category.INSERTED,getAction(),name,parameters));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("END;", Category.INSERTED));
	}
	
	public String toString() {
		return String.format("AddColumnSegment: %s %s %s",getAction().toString(),name,parameters);
	}
}
