package lunartools.sqlrepeatabler.segments;

import java.util.ArrayList;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlParser;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

public class AddForeignKeyConstraintSegment extends Segment{
	private Token name;
	private Token foreignKey;
	private Token referencesTable;
	private Token referencesColumn;

	public AddForeignKeyConstraintSegment(Token name,Token foreignKey, Token referencesTable, Token referencesColumn) {
		super(new Token("ADD",Category.COMMAND),name);
		this.name=name;
		this.foreignKey=foreignKey;
		this.referencesTable=referencesTable;
		this.referencesColumn=referencesColumn;
	}

	public void toSql(StringBuilder sb,TableName tableName,boolean mySql) throws Exception {
		sb.append(String.format("IF NOT EXISTS (")).append(SqlParser.CRLF);
		sb.append(String.format("\tSELECT 1")).append(SqlParser.CRLF);
		sb.append(String.format("\tFROM sys.foreign_keys")).append(SqlParser.CRLF);
		sb.append(String.format("\tWHERE name = '%s' AND parent_object_id = OBJECT_ID('%s')", stripDelimiters(name.toString()), tableName.getFullNameWithoutDelimiterAsString())).append(SqlParser.CRLF);
		sb.append(String.format(")")).append(SqlParser.CRLF);
		sb.append(String.format("BEGIN")).append(SqlParser.CRLF);
		sb.append(String.format("\tALTER TABLE %s",tableName.getFullNameAsString())).append(SqlParser.CRLF);

		sb.append(String.format("\t\t%s CONSTRAINT %s",getAction().toString(),name)).append(SqlParser.CRLF);
		sb.append(String.format("\t\tFOREIGN KEY %s",foreignKey)).append(SqlParser.CRLF);
		sb.append(String.format("\t\tREFERENCES %s %s;",referencesTable,referencesColumn)).append(SqlParser.CRLF);

		sb.append(String.format("END;")).append(SqlParser.CRLF);
	}

	@Override
	public void toSqlCharacters(ArrayList<SqlString> sqlCharacterLines,TableName tableName,boolean mySql) throws Exception {
        sqlCharacterLines.add(SqlString.createSqlStringFromString("IF NOT EXISTS (", Category.INSERTED));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("\tSELECT 1", Category.INSERTED));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("\tFROM sys.foreign_keys", Category.INSERTED));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("\tWHERE name = '%s' AND parent_object_id = OBJECT_ID('%s')", Category.INSERTED,getName().cloneWithoutDelimiters(),tableName.getFullNameWithoutDelimiter()));
        sqlCharacterLines.add(SqlString.createSqlStringFromString(")", Category.INSERTED));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("BEGIN", Category.INSERTED));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("\tALTER TABLE %s", Category.INSERTED,tableName.getFullName()));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("\t\t%s CONSTRAINT %s", Category.INSERTED,getAction(),getName()));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("\t\tFOREIGN KEY %s", Category.INSERTED,foreignKey));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("\t\tREFERENCES %s %s;", Category.INSERTED,referencesTable,referencesColumn));
        sqlCharacterLines.add(SqlString.createSqlStringFromString("END;", Category.INSERTED));
	}
	
	public String toString() {
		return String.format("AddForeignKeyConstraintSegment: %s CONSTRAINT %s %s %s %s", getAction().toString(),name,foreignKey,referencesTable,referencesColumn);
	}
}
