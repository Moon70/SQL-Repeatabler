package lunartools.sqlrepeatabler.statement.actions;

import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.TableName;
import lunartools.sqlrepeatabler.parser.Token;

public class AlterColumnAction extends AlterTableAction{
	private Token name;
	private Token parameters;

	public AlterColumnAction(Token name,Token parameters) {
		super(new Token("ALTER COLUMN",Category.COMMAND),name);
		this.name=name;
		this.parameters=parameters;
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock,Token tokenStatement,TableName tableName){
		sqlBlock.add(SqlString.createSqlStringFromString("    %s %s %s"	,Category.INSERTED,getAction(),name,parameters));
	}

	public String toString() {
		return String.format("AlterColumnAction: %s %s %s",getAction(),name,parameters);
	}
}
