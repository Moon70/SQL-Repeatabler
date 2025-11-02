package lunartools.sqlrepeatabler.segments;

import java.util.ArrayList;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

public class AlterColumnSegment extends Segment{
	private Token name;
	private Token parameters;

	public AlterColumnSegment(Token name,Token parameters) {
		super(new Token("ALTER COLUMN",Category.COMMAND),name);
		this.name=name;
		this.parameters=parameters;
	}

	public void toSql(StringBuilder sb,TableName tableName,boolean mySql) throws Exception {
		sb.append(String.format("\t%s %s %s",getAction().toString(),name,parameters));
	}

	@Override
	public void toSqlCharacters(ArrayList<SqlString> sqlCharacterLines,TableName tableName,boolean mySql) throws Exception {
        sqlCharacterLines.add(SqlString.createSqlStringFromString("\t%s %s %s", Category.INSERTED,getAction(),name,parameters));
	}
	
	public String toString() {
		return String.format("AlterColumnSegment: %s %s %s",getAction().toString(),name,parameters);
	}
}
