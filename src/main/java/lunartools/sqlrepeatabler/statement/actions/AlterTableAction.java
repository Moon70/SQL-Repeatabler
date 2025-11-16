package lunartools.sqlrepeatabler.statement.actions;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.Token;

public abstract class AlterTableAction {
	private Token action;
	private Token name;

	public AlterTableAction(Token action,Token name) {
		this.action=action;
		this.name=name;
	}

	public Token getAction() {
		return action;
	}

	public Token getName() {
		return name;
	}

    public abstract void toSqlCharacters(SqlBlock sqlBlock,Token tokenStatement,TableName tableName)throws Exception;
}
