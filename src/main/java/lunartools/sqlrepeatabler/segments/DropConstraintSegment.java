package lunartools.sqlrepeatabler.segments;

import lunartools.sqlrepeatabler.SqlParser;
import lunartools.sqlrepeatabler.common.TableName;

public class DropConstraintSegment extends Segment{
	private String action;
	private String name;

	public DropConstraintSegment(String action,String name) {
		super(action,name);
		this.action=action;
		this.name=name;
	}

	public void toSql(StringBuilder sb,TableName tableName,boolean mySql) throws Exception {
		sb.append(String.format("IF EXISTS (")).append(SqlParser.CRLF);
		sb.append(String.format("\tSELECT 1")).append(SqlParser.CRLF);
		sb.append(String.format("\tFROM sys.objects")).append(SqlParser.CRLF);
		sb.append(String.format("\tWHERE object_id = OBJECT_ID(N'%s.%s')", tableName.getFullName(),name)).append(SqlParser.CRLF);
		sb.append(String.format("\t\tAND type IN ('C', 'D', 'F', 'PK', 'UQ')")).append(SqlParser.CRLF);
		sb.append(String.format("\t\tAND parent_object_id = OBJECT_ID(N'%s')", tableName.getFullName())).append(SqlParser.CRLF);
		sb.append(String.format(")")).append(SqlParser.CRLF);
		sb.append(String.format("BEGIN")).append(SqlParser.CRLF);
		sb.append(String.format("\tALTER TABLE %s", tableName.getFullName())).append(SqlParser.CRLF);
		sb.append(String.format("\t%s CONSTRAINT %s;", action, name)).append(SqlParser.CRLF);
		sb.append(String.format("END;")).append(SqlParser.CRLF);
	}

	public String toString() {
		return String.format("DropConstraintSegment: %s %s",action,name);
	}
}
