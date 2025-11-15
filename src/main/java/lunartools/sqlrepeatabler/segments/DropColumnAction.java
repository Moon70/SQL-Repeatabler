package lunartools.sqlrepeatabler.segments;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

public class DropColumnAction extends AlterTableAction{
	private Token name;

	public DropColumnAction(Token name) {
		super(new Token("DROP",Category.COMMAND),name);
		this.name=name;
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock,Token tokenStatement,TableName tableName) throws Exception {
        sqlBlock.add(SqlString.createSqlStringFromString("IF COL_LENGTH ('%s','%s') IS NOT NULL"	,Category.INSERTED,tableName.getFullNameWithoutDelimiter(),getName().cloneWithoutDelimiters()));
        sqlBlock.add(SqlString.createSqlStringFromString("BEGIN"									,Category.INSERTED));
        sqlBlock.add(SqlString.createSqlStringFromString("    %s %s"								,Category.INSERTED,tokenStatement,tableName.getFullName()));
        sqlBlock.add(SqlString.createSqlStringFromString("        %s COLUMN %s;"					,Category.INSERTED,getAction(),getName()));
        sqlBlock.add(SqlString.createSqlStringFromString("END;"										,Category.INSERTED));
	}
	
	public String toString() {
		return String.format("DropColumnAction: %s %s",getAction().toString(),name);
	}
}
