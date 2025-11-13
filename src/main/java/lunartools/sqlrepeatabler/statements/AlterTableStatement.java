package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlCharacter;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;
import lunartools.sqlrepeatabler.segments.AlterColumnSegment;
import lunartools.sqlrepeatabler.segments.Segment;

public class AlterTableStatement implements Statement{
	public static final String COMMAND="ALTER TABLE";
	private Token tokenStatement;
	private TableName tableName;
	private ArrayList<Segment> segments;

	public AlterTableStatement(Token statement,TableName tableName,ArrayList<Segment> segments) {
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
			Segment segment=segments.get(i);
			if(segment instanceof AlterColumnSegment) {
				hasAlterColumnAction=true;
				if(i>0) {
					sqlblockTemp.getLastLine().append(new SqlCharacter(',',Category.UNCATEGORIZED));
				}
				segment.toSqlCharacters(sqlblockTemp,tokenStatement, tableName, hasAlterColumnAction);
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
			Segment columnelement=segments.get(i);
			if(!(columnelement instanceof AlterColumnSegment)) {
				columnelement.toSqlCharacters(sqlBlock,tokenStatement, tableName, hasAlterColumnAction);
				if(i<segments.size()-1) {
					sqlBlock.add(SqlString.EMPTY_LINE);
				}
			}
		}
	}

}
