package lunartools.sqlrepeatabler.core.model;

public class AddForeignKeyConstraintAction extends AlterTableAction{
	private Token name;
	private Token foreignKey;
	private Token referencesTable;
	private Token referencesColumn;

	public AddForeignKeyConstraintAction(Token name,Token foreignKey, Token referencesTable, Token referencesColumn) {
		super(new Token("ADD",Category.COMMAND),name);
		this.name=name;
		this.foreignKey=foreignKey;
		this.referencesTable=referencesTable;
		this.referencesColumn=referencesColumn;
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock,Token tokenStatement,TableName tableName){
		sqlBlock.add(SqlString.createSqlStringFromString("IF NOT EXISTS ("												,Category.INSERTED));
		sqlBlock.add(SqlString.createSqlStringFromString("    SELECT 1"													,Category.INSERTED));
		sqlBlock.add(SqlString.createSqlStringFromString("    FROM sys.foreign_keys" 									,Category.INSERTED));
		sqlBlock.add(SqlString.createSqlStringFromString("    WHERE name = '%s'"                                        ,Category.INSERTED,getName().cloneWithoutDelimiters()));
		sqlBlock.add(SqlString.createSqlStringFromString("        AND parent_object_id = OBJECT_ID('%s')"               ,Category.INSERTED,tableName.getFullNameWithoutDelimiter()));
		sqlBlock.add(SqlString.createSqlStringFromString(")"															,Category.INSERTED));
		sqlBlock.add(SqlString.createSqlStringFromString("BEGIN"														,Category.INSERTED));
		sqlBlock.add(SqlString.createSqlStringFromString("    %s %s"													,Category.INSERTED,tokenStatement,tableName.getFullName()));
		sqlBlock.add(SqlString.createSqlStringFromString("        %s CONSTRAINT %s"										,Category.INSERTED,getAction(),getName()));
		sqlBlock.add(SqlString.createSqlStringFromString("        FOREIGN KEY %s"										,Category.INSERTED,foreignKey));
		sqlBlock.add(SqlString.createSqlStringFromString("        REFERENCES %s %s;"									,Category.INSERTED,referencesTable,referencesColumn));
		sqlBlock.add(SqlString.createSqlStringFromString("END;"															,Category.INSERTED));
	}

	public String toString() {
		return String.format("AddForeignKeyConstraintAction: %s CONSTRAINT %s %s %s %s", getAction(),name,foreignKey,referencesTable,referencesColumn);
	}
}
