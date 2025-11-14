package lunartools.sqlrepeatabler.statements;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlCharacter;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

public class SpRenameStatement implements Statement{
	public static final String COMMAND="SP_RENAME";
	private Token tokenStatement;
	private TableName tableName;
	private Token oldName;
	private Token newName;
	private Token type;

	public SpRenameStatement(Token statement,TableName tableName,Token oldName, Token newName, Token type) throws CloneNotSupportedException {
		this.tokenStatement=statement;
		this.tableName=tableName;
		this.oldName=oldName;
		this.newName=newName;
		this.type=type;
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock) throws Exception {
		SqlBlock sqlBlockStatement=new SqlBlock();

		//sqlBlockStatement.add(SqlString.createSqlStringFromString("IF COL_LENGTH ('%s','%s') IS NULL", Category.INSERTED, tableNameWithoutBrackets,newNameWithoutBrackets));
		//sqlBlockStatement.add(SqlString.createSqlStringFromString("BEGIN", Category.INSERTED));
		//sqlBlockStatement.add(SqlString.createSqlStringFromString("\tEXEC %s '%s.%s', %s, %s;", Category.INSERTED,tokenStatement, tableName,oldName,newName,type));
		//sqlBlockStatement.add(SqlString.createSqlStringFromString("END;", Category.INSERTED));

		Token tableFullnameWithoutDelimiter=tableName.getFullNameWithoutDelimiter();

		sqlBlockStatement.add(SqlString.createSqlStringFromString("IF EXISTS (",Category.INSERTED));
		sqlBlockStatement.add(SqlString.createSqlStringFromString("        SELECT 1"                            ,Category.INSERTED));
		sqlBlockStatement.add(SqlString.createSqlStringFromString("        FROM sys.columns c"                  ,Category.INSERTED));
		sqlBlockStatement.add(SqlString.createSqlStringFromString("        WHERE c.object_id = OBJECT_ID('%s')" ,Category.INSERTED,tableFullnameWithoutDelimiter));
		sqlBlockStatement.add(SqlString.createSqlStringFromString("            AND c.name = '%s'"               ,Category.INSERTED,oldName.cloneWithoutDelimiters()));
		sqlBlockStatement.add(SqlString.createSqlStringFromString("    )"                                       ,Category.INSERTED));
		sqlBlockStatement.add(SqlString.createSqlStringFromString("    AND NOT EXISTS ("                        ,Category.INSERTED));
		sqlBlockStatement.add(SqlString.createSqlStringFromString("        SELECT 1"                            ,Category.INSERTED));
		sqlBlockStatement.add(SqlString.createSqlStringFromString("        FROM sys.columns c"                  ,Category.INSERTED));
		sqlBlockStatement.add(SqlString.createSqlStringFromString("        WHERE c.object_id = OBJECT_ID('%s')" ,Category.INSERTED,tableFullnameWithoutDelimiter));
		sqlBlockStatement.add(SqlString.createSqlStringFromString("            AND c.name = '%s'"               ,Category.INSERTED,newName.cloneWithoutDelimiters()));
		sqlBlockStatement.add(SqlString.createSqlStringFromString("    )"                                       ,Category.INSERTED));
		sqlBlockStatement.add(SqlString.createSqlStringFromString("BEGIN"                                       ,Category.INSERTED));
		sqlBlockStatement.add(SqlString.createSqlStringFromString("    EXEC %s '%s.%s',"                        ,Category.INSERTED,tokenStatement, tableFullnameWithoutDelimiter,oldName.cloneWithoutDelimiters()));
		sqlBlockStatement.add(SqlString.createSqlStringFromString("                   '%s',"                    ,Category.INSERTED,newName.cloneWithoutDelimiters()));
		sqlBlockStatement.add(SqlString.createSqlStringFromString("                   '%s';"                    ,Category.INSERTED,type.cloneWithoutDelimiters()));
		sqlBlockStatement.add(SqlString.createSqlStringFromString("END;"                                        ,Category.INSERTED));

		SqlCharacter sqlCharacter=tokenStatement.getFirstCharacter();
		sqlBlockStatement.setBackgroundColor(sqlCharacter.getBackgroundColor());
		sqlBlock.add(sqlBlockStatement);
	}

}
