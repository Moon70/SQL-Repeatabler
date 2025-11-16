package lunartools.sqlrepeatabler.statement;

import java.util.ArrayList;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlCharacter;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

public class CreateTableStatement implements Statement{
	public static final String COMMAND="CREATE TABLE";
	private Token tokenStatement;
	private TableName tableName;
	private ArrayList<Token> tokens;

	public CreateTableStatement(Token statement,TableName tableName,ArrayList<Token> tokens) {
		this.tokenStatement=statement;
		this.tableName=tableName;
		this.tokens=tokens;
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock) throws Exception {
		SqlBlock sqlBlockStatement=new SqlBlock();
		
		sqlBlockStatement.add(SqlString.createSqlStringFromString(	"IF OBJECT_ID(N'%s', 'U') IS NULL"	,Category.INSERTED, tableName.getFullNameWithoutDelimiter()));
		sqlBlockStatement.add(SqlString.createSqlStringFromString(	"BEGIN"								,Category.INSERTED));
		sqlBlockStatement.add(SqlString.createSqlStringFromString(	"    %s %s ("						,Category.INSERTED,tokenStatement,tableName.getFullName()));

		SqlString sqlString;
		for(int i=0;i<tokens.size();i++) {
			sqlString=SqlString.createSqlStringFromString(			"        %s"						,Category.INSERTED,tokens.get(i));
			if(i<tokens.size()-1) {
				sqlString.append(new SqlCharacter(',',Category.INSERTED));
			}
			sqlBlockStatement.add(sqlString);
		}

		sqlBlockStatement.add(SqlString.createSqlStringFromString(	"    );"							,Category.INSERTED));
		sqlBlockStatement.add(SqlString.createSqlStringFromString(	"END;"								,Category.INSERTED));

		SqlCharacter sqlCharacter=tokenStatement.getFirstCharacter();
		sqlBlockStatement.setBackgroundColor(sqlCharacter.getBackgroundColor());
		sqlBlock.add(sqlBlockStatement);
	}

}
