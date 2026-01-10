package lunartools.sqlrepeatabler.core.model;

public class AddUniqueConstraintAction extends AlterTableAction{
	private Token name;
	private Token referencesColumn;

	public AddUniqueConstraintAction(Token name,Token referencesColumn) {
		super(new Token("ADD",Category.COMMAND),name);
		this.name=name;
		this.referencesColumn=referencesColumn;
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock,Token tokenStatement,TableName tableName){
		sqlBlock.add(SqlString.createSqlStringFromString("IF NOT EXISTS ("                      ,Category.INSERTED));
		sqlBlock.add(SqlString.createSqlStringFromString("    SELECT 1"                         ,Category.INSERTED));
		sqlBlock.add(SqlString.createSqlStringFromString("    FROM sys.key_constraints kc"      ,Category.INSERTED));
		sqlBlock.add(SqlString.createSqlStringFromString("    WHERE kc.name = '%s'"             ,Category.INSERTED,getName().cloneWithoutDelimiters()));
		sqlBlock.add(SqlString.createSqlStringFromString("        AND kc.type = 'UQ'"           ,Category.INSERTED));
		sqlBlock.add(SqlString.createSqlStringFromString(")"                                    ,Category.INSERTED));
		sqlBlock.add(SqlString.createSqlStringFromString("BEGIN"								,Category.INSERTED));
		sqlBlock.add(SqlString.createSqlStringFromString("    %s %s"							,Category.INSERTED,tokenStatement,tableName.getFullName()));
		sqlBlock.add(SqlString.createSqlStringFromString("        ADD CONSTRAINT %s UNIQUE %s;"	,Category.INSERTED,getName(),referencesColumn));
		sqlBlock.add(SqlString.createSqlStringFromString("END;"									,Category.INSERTED));
	}

	public String toString() {
		return String.format("AddUniqueConstraintAction: %s CONSTRAINT %s %s", getAction(),name,referencesColumn);
	}
}
