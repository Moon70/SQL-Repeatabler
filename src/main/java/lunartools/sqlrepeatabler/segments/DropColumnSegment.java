package lunartools.sqlrepeatabler.segments;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlParser;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

public class DropColumnSegment extends Segment{
	private Token name;

	public DropColumnSegment(Token name) {
		super(new Token("DROP",Category.COMMAND),name);
		this.name=name;
	}

	public void toSql(StringBuilder sb,TableName tableName,boolean mySql) throws Exception {
		sb.append(String.format("IF COL_LENGTH ('%s','%s') IS NOT NULL", tableName.getFullNameWithoutDelimiterAsString(),getColumnNameWithoutDelimiters())).append(SqlParser.CRLF);
		sb.append(String.format("BEGIN")).append(SqlParser.CRLF);
		sb.append(String.format("\tALTER TABLE %s",tableName.getFullNameAsString())).append(SqlParser.CRLF);
		sb.append(String.format("\t\t%s COLUMN %s;",getAction().toString(),name)).append(SqlParser.CRLF);
		sb.append(String.format("END;")).append(SqlParser.CRLF);
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock,Token tokenStatement,TableName tableName,boolean mySql) throws Exception {
        sqlBlock.add(SqlString.createSqlStringFromString("IF COL_LENGTH ('%s','%s') IS NOT NULL", Category.INSERTED,tableName.getFullNameWithoutDelimiter(),getName().cloneWithoutDelimiters()));
        sqlBlock.add(SqlString.createSqlStringFromString("BEGIN", Category.INSERTED));
        sqlBlock.add(SqlString.createSqlStringFromString("\t%s %s", Category.INSERTED,tokenStatement,tableName.getFullName()));
        sqlBlock.add(SqlString.createSqlStringFromString("\t\t%s COLUMN %s;", Category.INSERTED,getAction(),getName()));
        sqlBlock.add(SqlString.createSqlStringFromString("END;", Category.INSERTED));
	}
	
	public String toString() {
		return String.format("DropColumnSegment: %s %s",getAction().toString(),name);
	}
}
