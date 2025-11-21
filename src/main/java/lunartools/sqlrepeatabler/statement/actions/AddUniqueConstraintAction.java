package lunartools.sqlrepeatabler.statement.actions;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

public class AddUniqueConstraintAction extends AlterTableAction{
	private Token name;
	private Token referencesColumn;

	public AddUniqueConstraintAction(Token name,Token referencesColumn) {
		super(new Token("ADD",Category.COMMAND),name);
		this.name=name;
		this.referencesColumn=referencesColumn;
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock,Token tokenStatement,TableName tableName) throws Exception {
        sqlBlock.add(SqlString.createSqlStringFromString("IF OBJECT_ID('%s','UQ') IS NULL"		,Category.INSERTED,getName().cloneWithoutDelimiters()));
        sqlBlock.add(SqlString.createSqlStringFromString("BEGIN"								,Category.INSERTED));
        sqlBlock.add(SqlString.createSqlStringFromString("    %s %s"							,Category.INSERTED,tokenStatement,tableName.getFullName()));
        sqlBlock.add(SqlString.createSqlStringFromString("        ADD CONSTRAINT %s unique %s;"	,Category.INSERTED,getName(),referencesColumn));
        sqlBlock.add(SqlString.createSqlStringFromString("END;"									,Category.INSERTED));
	}
	
	public String toString() {
		return String.format("AddUniqueConstraintAction: %s CONSTRAINT %s %s", getAction(),name,referencesColumn);
	}
}
