package lunartools.sqlrepeatabler.segments;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlParser;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

public class AddUniqueConstraintSegment extends Segment{
	private Token name;
	private Token referencesColumn;

	public AddUniqueConstraintSegment(Token name,Token referencesColumn) {
		super(new Token("ADD",Category.COMMAND),name);
		this.name=name;
		this.referencesColumn=referencesColumn;
	}

	public void toSql(StringBuilder sb,TableName tableName,boolean mySql) throws Exception {
		sb.append(String.format("IF OBJECT_ID('%s','UQ') IS NULL", stripDelimiters(name.toString()))).append(SqlParser.CRLF);
		sb.append(String.format("BEGIN")).append(SqlParser.CRLF);
		sb.append(String.format("\tALTER TABLE %s",tableName.getFullNameAsString())).append(SqlParser.CRLF);
		sb.append(String.format("\t\tADD CONSTRAINT %s unique %s;",name,referencesColumn)).append(SqlParser.CRLF);
		sb.append("END;").append(SqlParser.CRLF);
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock,Token tokenStatement,TableName tableName,boolean mySql) throws Exception {
        sqlBlock.add(SqlString.createSqlStringFromString("IF OBJECT_ID('%s','UQ') IS NULL", Category.INSERTED,getName().cloneWithoutDelimiters()));
        sqlBlock.add(SqlString.createSqlStringFromString("BEGIN", Category.INSERTED));
        sqlBlock.add(SqlString.createSqlStringFromString("\t%s %s", Category.INSERTED,tokenStatement,tableName.getFullName()));
        sqlBlock.add(SqlString.createSqlStringFromString("\t\tADD CONSTRAINT %s unique %s;", Category.INSERTED,getName(),referencesColumn));
        sqlBlock.add(SqlString.createSqlStringFromString("END;", Category.INSERTED));
	}
	
	public String toString() {
		return String.format("AddUniqueConstraintSegment: %s CONSTRAINT %s %s", getAction().toString(),name,referencesColumn);
	}
}
