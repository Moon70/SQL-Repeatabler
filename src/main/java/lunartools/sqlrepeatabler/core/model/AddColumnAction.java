package lunartools.sqlrepeatabler.core.model;

public class AddColumnAction extends AlterTableAction{
	private Token name;
	private Token parameters;

	public AddColumnAction(Token name,Token parameters) {
		super(new Token("ADD",Category.COMMAND),name);
		this.name=name;
		this.parameters=parameters;
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock,Token tokenStatement,TableName tableName){
        sqlBlock.add(SqlString.createSqlStringFromString("IF COL_LENGTH ('%s','%s') IS NULL"	,Category.INSERTED,tableName.getFullNameWithoutDelimiter(),getName().cloneWithoutDelimiters()));
        sqlBlock.add(SqlString.createSqlStringFromString("BEGIN"								,Category.INSERTED));
        sqlBlock.add(SqlString.createSqlStringFromString("    %s %s"							,Category.INSERTED,tokenStatement,tableName.getFullName()));
        sqlBlock.add(SqlString.createSqlStringFromString("        %s %s %s;"					,Category.INSERTED,getAction(),name,parameters));
        sqlBlock.add(SqlString.createSqlStringFromString("END;"									,Category.INSERTED));
	}
	
	public String toString() {
		return String.format("AddColumnAction: %s %s %s",getAction(),name,parameters);
	}
}
