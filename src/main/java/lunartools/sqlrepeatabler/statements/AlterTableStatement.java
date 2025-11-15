package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlCharacter;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;
import lunartools.sqlrepeatabler.segments.AlterColumnAction;
import lunartools.sqlrepeatabler.segments.AlterTableAction;

public class AlterTableStatement implements Statement {
	public static final String COMMAND="ALTER TABLE";
	private Token tokenStatement;
	private TableName tableName;
	private ArrayList<AlterTableAction> segments;

	public AlterTableStatement(Token statement,TableName tableName,ArrayList<AlterTableAction> segments) {
		this.tokenStatement=statement;
		this.tableName=tableName;
		this.segments=segments;
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock) throws Exception {
		SqlCharacter sqlCharacter=tokenStatement.getFirstCharacter();
		String statementBackgroundColor=sqlCharacter.getBackgroundColor();
		
		boolean hasAlterColumnAction=false;

		SqlBlock sqlblockTemp=new SqlBlock();
		for(int i=0;i<segments.size();i++) {
			AlterTableAction segment=segments.get(i);
			if(segment instanceof AlterColumnAction) {
				hasAlterColumnAction=true;
				if(i>0) {
					sqlblockTemp.getLastLine().append(new SqlCharacter(',',Category.UNCATEGORIZED));
				}
				segment.toSqlCharacters(sqlblockTemp,tokenStatement, tableName);
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

		for(int i=0;i<segments.size();i++) {
			AlterTableAction columnelement=segments.get(i);
			if(!(columnelement instanceof AlterColumnAction)) {
				SqlBlock sqlBlockStatement=new SqlBlock();
				columnelement.toSqlCharacters(sqlBlockStatement,tokenStatement, tableName);
				if(i<segments.size()-1) {
					sqlBlockStatement.add(SqlString.EMPTY_LINE);
				}
				sqlBlockStatement.setBackgroundColor(statementBackgroundColor);
				sqlBlock.add(sqlBlockStatement);
			}
		}
	}

}
