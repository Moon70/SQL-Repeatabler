package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlCharacter;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;
import lunartools.sqlrepeatabler.segments.TableSegment;

public class CreateTableStatement implements Statement{
	public static final String COMMAND="CREATE TABLE";
	private Token tokenStatement;
	private TableName tableName;
	private ArrayList<TableSegment> tableElements;

	public CreateTableStatement(Token statement,TableName tableName,ArrayList<TableSegment> tableElements) {
		this.tokenStatement=statement;
		this.tableName=tableName;
		this.tableElements=tableElements;
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock) throws Exception {
		sqlBlock.add(SqlString.createSqlStringFromString("IF OBJECT_ID(N'%s', 'U') IS NULL", Category.INSERTED, tableName.getFullNameWithoutDelimiter()));
		sqlBlock.add(SqlString.createSqlStringFromString("BEGIN", Category.INSERTED));
		sqlBlock.add(SqlString.createSqlStringFromString("\t%s %s (",Category.INSERTED,tokenStatement,tableName.getFullName()));

		SqlString sqlString;
		for(int i=0;i<tableElements.size();i++) {
			sqlString=SqlString.createSqlStringFromString("\t\t%s", Category.INSERTED,tableElements.get(i).getToken());
			if(i<tableElements.size()-1) {
				sqlString.append(new SqlCharacter(',',Category.INSERTED));
			}
			sqlBlock.add(sqlString);
		}

		sqlBlock.add(SqlString.createSqlStringFromString("\t);", Category.INSERTED));
		sqlBlock.add(SqlString.createSqlStringFromString("END;", Category.INSERTED));
	}

}
