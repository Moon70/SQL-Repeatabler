package lunartools.sqlrepeatabler.segments;

import java.util.ArrayList;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlParser;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

public class DropConstraintSegment extends Segment{
	private Token name;

	public DropConstraintSegment(Token name) {
		super(new Token("DROP",Category.COMMAND),name);
		this.name=name;
	}

	public void toSql(StringBuilder sb,TableName tableName,boolean mySql) throws Exception {
		sb.append(String.format("IF EXISTS (")).append(SqlParser.CRLF);
		sb.append(String.format("\tSELECT 1")).append(SqlParser.CRLF);
		sb.append(String.format("\tFROM sys.objects")).append(SqlParser.CRLF);
		sb.append(String.format("\tWHERE object_id = OBJECT_ID(N'%s.%s')", tableName.getFullNameAsString(),name)).append(SqlParser.CRLF);
		sb.append(String.format("\t\tAND type IN ('C', 'D', 'F', 'PK', 'UQ')")).append(SqlParser.CRLF);
		sb.append(String.format("\t\tAND parent_object_id = OBJECT_ID(N'%s')", tableName.getFullNameAsString())).append(SqlParser.CRLF);
		sb.append(String.format(")")).append(SqlParser.CRLF);
		sb.append(String.format("BEGIN")).append(SqlParser.CRLF);
		sb.append(String.format("\tALTER TABLE %s", tableName.getFullNameAsString())).append(SqlParser.CRLF);
		sb.append(String.format("\t%s CONSTRAINT %s;", getAction().toString(), name)).append(SqlParser.CRLF);
		sb.append(String.format("END;")).append(SqlParser.CRLF);
	}

	@Override
	public void toSqlCharacters(ArrayList<SqlString> sqlCharacterLines,Token tokenStatement,TableName tableName,boolean mySql) throws Exception {
        sqlCharacterLines.add(SqlString.createSqlStringFromString("IF EXISTS (", Category.INSERTED));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("\tSELECT 1", Category.INSERTED));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("\tFROM sys.objects", Category.INSERTED));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("\tWHERE object_id = OBJECT_ID(N'%s.%s')", Category.INSERTED,tableName.getFullName(),name));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("\t\tAND type IN ('C', 'D', 'F', 'PK', 'UQ')", Category.INSERTED));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("\t\tAND parent_object_id = OBJECT_ID(N'%s')", Category.INSERTED, tableName.getFullName()));
        sqlCharacterLines.add(SqlString.createSqlStringFromString(")", Category.INSERTED));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("BEGIN", Category.INSERTED));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("\t%s %s", Category.INSERTED,tokenStatement, tableName.getFullName()));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("\t%s CONSTRAINT %s;", Category.INSERTED,getAction(),name));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("END;", Category.INSERTED));
	}
	
	public String toString() {
		return String.format("DropConstraintSegment: %s %s",getAction().toString(),name);
	}
}
