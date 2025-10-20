package lunartools.sqlrepeatabler.segments;

import lunartools.sqlrepeatabler.SqlParser;
import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Token;

public class AddUniqueConstraintSegment extends Segment{
	private String action;
	private Token name;
	private Token referencesColumn;

	public AddUniqueConstraintSegment(String action,Token name,Token referencesColumn) {
		super(action,name);
		this.action=action;
		this.name=name;
		this.referencesColumn=referencesColumn;
	}

	public void toSql(StringBuilder sb,TableName tableName,boolean mySql) throws Exception {
		sb.append(String.format("IF OBJECT_ID('%s','UQ') IS NULL", stripDelimiters(name.toString()))).append(SqlParser.CRLF);
		sb.append(String.format("BEGIN")).append(SqlParser.CRLF);
		sb.append(String.format("\tALTER TABLE %s",tableName.getFullName())).append(SqlParser.CRLF);
		sb.append(String.format("\t\tADD CONSTRAINT %s unique %s;",name,referencesColumn)).append(SqlParser.CRLF);
		sb.append("END;").append(SqlParser.CRLF);
	}

	public String toString() {
		return String.format("AddUniqueConstraintSegment: %s CONSTRAINT %s %s", action,name,referencesColumn);
	}
}
