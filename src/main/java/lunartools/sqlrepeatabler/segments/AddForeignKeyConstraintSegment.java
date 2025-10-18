package lunartools.sqlrepeatabler.segments;

import lunartools.sqlrepeatabler.SqlParser;
import lunartools.sqlrepeatabler.common.TableName;

public class AddForeignKeyConstraintSegment extends Segment{
	private String action;
	private String name;
	private String foreignKey;
	private String referencesTable;
	private String referencesColumn;

	public AddForeignKeyConstraintSegment(String action,String name,String foreignKey, String referencesTable, String referencesColumn) {
		super(action,name);
		this.action=action;
		this.name=name;
		this.foreignKey=foreignKey;
		this.referencesTable=referencesTable;
		this.referencesColumn=referencesColumn;
	}

	public void toSql(StringBuilder sb,TableName tableName,boolean mySql) throws Exception {
		sb.append(String.format("IF NOT EXISTS (")).append(SqlParser.CRLF);
		sb.append(String.format("\tSELECT 1")).append(SqlParser.CRLF);
		sb.append(String.format("\tFROM sys.foreign_keys")).append(SqlParser.CRLF);
		sb.append(String.format("\tWHERE name = '%s' AND parent_object_id = OBJECT_ID('%s')", stripDelimiters(name), tableName.getFullNameWithoutDelimiter())).append(SqlParser.CRLF);
		sb.append(String.format(")")).append(SqlParser.CRLF);
		sb.append(String.format("BEGIN")).append(SqlParser.CRLF);
		sb.append(String.format("\tALTER TABLE %s",tableName.getFullName())).append(SqlParser.CRLF);

		sb.append(String.format("\t\t%s CONSTRAINT %s",action,name)).append(SqlParser.CRLF);
		sb.append(String.format("\t\tFOREIGN KEY %s",foreignKey)).append(SqlParser.CRLF);
		sb.append(String.format("\t\tREFERENCES %s %s;",referencesTable,referencesColumn)).append(SqlParser.CRLF);

		sb.append(String.format("END;")).append(SqlParser.CRLF);
	}

	public String toString() {
		return String.format("AddForeignKeyConstraintSegment: %s CONSTRAINT %s %s %s %s", action,name,foreignKey,referencesTable,referencesColumn);
	}
}
