package lunartools.sqlrepeatabler.statement;

import java.util.ArrayList;

import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlCharacter;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.TableName;
import lunartools.sqlrepeatabler.parser.Token;
import lunartools.sqlrepeatabler.statement.actions.AlterColumnAction;
import lunartools.sqlrepeatabler.statement.actions.AlterTableAction;

public class AlterTableStatement implements Statement {
	public static final String COMMAND="ALTER TABLE";
	private Token tokenStatement;
	private TableName tableName;
	private ArrayList<AlterTableAction> alterTableActions;

	public AlterTableStatement(Token statement,TableName tableName,ArrayList<AlterTableAction> alterTableActions) {
		this.tokenStatement=statement;
		this.tableName=tableName;
		this.alterTableActions=alterTableActions;
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock){
		SqlCharacter sqlCharacter=tokenStatement.getFirstCharacter();
		String statementBackgroundColor=sqlCharacter.getBackgroundColor();

		boolean hasAlterColumnAction=false;

		SqlBlock sqlblockTemp=new SqlBlock();
		for(int i=0;i<alterTableActions.size();i++) {
			AlterTableAction alterTableAction=alterTableActions.get(i);
			if(alterTableAction instanceof AlterColumnAction) {
				hasAlterColumnAction=true;
				if(i>0) {
					sqlblockTemp.getLastLine().append(new SqlCharacter(',',Category.UNCATEGORIZED));
				}
				alterTableAction.toSqlCharacters(sqlblockTemp,tokenStatement, tableName);
			}
		}
		if(hasAlterColumnAction) {
			SqlBlock sqlBlockStatement=new SqlBlock();
			sqlBlockStatement.add(SqlString.createSqlStringFromString("%s %s", Category.INSERTED,tokenStatement,tableName.getFullName()));
			sqlblockTemp.getLastLine().append(new SqlCharacter(';',Category.UNCATEGORIZED));
			sqlBlockStatement.add(sqlblockTemp);
			sqlBlockStatement.setBackgroundColor(statementBackgroundColor);
			sqlBlock.add(sqlBlockStatement);
		}

		for(int i=0;i<alterTableActions.size();i++) {
			AlterTableAction alterTableAction=alterTableActions.get(i);
			if(!(alterTableAction instanceof AlterColumnAction)) {
				SqlBlock sqlBlockStatement=new SqlBlock();
				alterTableAction.toSqlCharacters(sqlBlockStatement,tokenStatement, tableName);
				if(i<alterTableActions.size()-1) {
					sqlBlockStatement.add(SqlString.EMPTY_LINE);
				}
				sqlBlockStatement.setBackgroundColor(statementBackgroundColor);
				sqlBlock.add(sqlBlockStatement);
			}
		}
	}

}
