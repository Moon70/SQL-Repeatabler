package lunartools.sqlrepeatabler.segments;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.SqlParser;
import lunartools.sqlrepeatabler.parser.Token;

public class AddColumnSegment extends Segment{
	private String action;
	private Token name;
	private Token parameters;

	public AddColumnSegment(String action,Token name,Token parameters) {
		super(action,name);
		this.action=action;
		this.name=name;
		this.parameters=parameters;
	}

	public void toSql(StringBuilder sb,TableName tableName,boolean mySql) throws Exception {
		sb.append(String.format("IF COL_LENGTH ('%s','%s') IS NULL",tableName.getFullNameWithoutDelimiter(),getColumnNameWithoutDelimiters())).append(SqlParser.CRLF);
		sb.append(String.format("BEGIN")).append(SqlParser.CRLF);
		sb.append(String.format("\tALTER TABLE %s",tableName.getFullName())).append(SqlParser.CRLF);
		sb.append(String.format("\t\t%s %s %s;",action,name,parameters)).append(SqlParser.CRLF);
		sb.append(String.format("END;")).append(SqlParser.CRLF);
	}

	public String toString() {
		return String.format("AddColumnSegment: %s %s %s",action,name,parameters);
	}
}
