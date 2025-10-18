package lunartools.sqlrepeatabler.segments;

import lunartools.sqlrepeatabler.SqlParser;
import lunartools.sqlrepeatabler.common.TableName;

public class DropColumnSegment extends Segment{
	private String action;
	private String name;

	public DropColumnSegment(String action,String name) {
		super(action,name);
		this.action=action;
		this.name=name;
	}

	public void toSql(StringBuilder sb,TableName tableName,boolean mySql) throws Exception {
		sb.append(String.format("IF COL_LENGTH ('%s','%s') IS NOT NULL", tableName.getFullNameWithoutDelimiter(),getColumnNameWithoutDelimiters())).append(SqlParser.CRLF);
		sb.append(String.format("BEGIN")).append(SqlParser.CRLF);
		sb.append(String.format("\tALTER TABLE %s",tableName.getFullName())).append(SqlParser.CRLF);
		sb.append(String.format("\t\t%s COLUMN %s;",action,name)).append(SqlParser.CRLF);
		sb.append(String.format("END;")).append(SqlParser.CRLF);
	}

	public String toString() {
		return String.format("DropColumnSegment: %s %s",action,name);
	}
}
