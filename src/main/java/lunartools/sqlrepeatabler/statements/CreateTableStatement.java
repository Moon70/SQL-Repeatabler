package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlCharacter;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;
import lunartools.sqlrepeatabler.segments.TableSegment;

public class CreateTableStatement implements Statement{
	private static Logger logger = LoggerFactory.getLogger(CreateTableStatement.class);
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
	public void toSqlCharacters(ArrayList<SqlString> sqlCharacterLines) throws Exception {
		sqlCharacterLines.add(SqlString.createSqlStringFromString("IF OBJECT_ID(N'%s', 'U') IS NULL", Category.INSERTED, tableName.getFullNameWithoutDelimiter()));
		sqlCharacterLines.add(SqlString.createSqlStringFromString("BEGIN", Category.INSERTED));
		sqlCharacterLines.add(SqlString.createSqlStringFromString("\t%s %s (",Category.INSERTED,tokenStatement,tableName.getFullName()));

		SqlString sqlString;
		for(int i=0;i<tableElements.size();i++) {
			sqlString=SqlString.createSqlStringFromString("\t\t%s", Category.INSERTED,tableElements.get(i).getToken());
			if(i<tableElements.size()-1) {
				sqlString.append(new SqlCharacter(',',Category.INSERTED));
			}
			sqlCharacterLines.add(sqlString);
		}

		sqlCharacterLines.add(SqlString.createSqlStringFromString("\t);", Category.INSERTED));
		sqlCharacterLines.add(SqlString.createSqlStringFromString("END;", Category.INSERTED));
	}

}
