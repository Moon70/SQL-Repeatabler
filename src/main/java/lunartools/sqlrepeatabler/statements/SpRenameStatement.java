package lunartools.sqlrepeatabler.statements;

import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlCharacter;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

public class SpRenameStatement implements Statement{
	public static final String COMMAND="SP_RENAME";
	private Token tokenStatement;
	private Token tableName;
	private Token tableNameWithoutBrackets;
	private Token plainSchemaAndTableName;
	private Token oldName;
	private Token newName;
	private Token type;

	public SpRenameStatement(Token statement,Token table,Token oldName, Token newName, Token type) throws CloneNotSupportedException {
		this.tokenStatement=statement;
		this.tableName=table;
		this.oldName=oldName;
		this.newName=newName;
		this.type=type;
		this.tableNameWithoutBrackets=(Token)table.cloneWithoutDelimiters();
		if(tableNameWithoutBrackets.toString().toLowerCase().startsWith("dbo.")) {
		    plainSchemaAndTableName=tableNameWithoutBrackets;
		}else {
		    plainSchemaAndTableName=new Token("dbo.",Category.TABLE);
		    plainSchemaAndTableName.append(tableNameWithoutBrackets);
		}
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock) throws Exception {
		SqlBlock sqlBlockStatement=new SqlBlock();

//        sqlBlockStatement.add(SqlString.createSqlStringFromString("IF COL_LENGTH ('%s','%s') IS NULL", Category.INSERTED, tableNameWithoutBrackets,newNameWithoutBrackets));
//        sqlBlockStatement.add(SqlString.createSqlStringFromString("BEGIN", Category.INSERTED));
//        sqlBlockStatement.add(SqlString.createSqlStringFromString("\tEXEC %s '%s.%s', %s, %s;", Category.INSERTED,tokenStatement, tableName,oldName,newName,type));
//        sqlBlockStatement.add(SqlString.createSqlStringFromString("END;", Category.INSERTED));

        sqlBlockStatement.add(SqlString.createSqlStringFromString("IF EXISTS (",Category.INSERTED));
        sqlBlockStatement.add(SqlString.createSqlStringFromString("        SELECT 1"                            ,Category.INSERTED));
        sqlBlockStatement.add(SqlString.createSqlStringFromString("        FROM sys.columns c"                  ,Category.INSERTED));
        sqlBlockStatement.add(SqlString.createSqlStringFromString("        WHERE c.object_id = OBJECT_ID('%s')" ,Category.INSERTED,plainSchemaAndTableName));
        sqlBlockStatement.add(SqlString.createSqlStringFromString("            AND c.name = '%s'"               ,Category.INSERTED,oldName.cloneWithoutDelimiters()));
        sqlBlockStatement.add(SqlString.createSqlStringFromString("    )"                                       ,Category.INSERTED));
        sqlBlockStatement.add(SqlString.createSqlStringFromString("    AND NOT EXISTS ("                        ,Category.INSERTED));
        sqlBlockStatement.add(SqlString.createSqlStringFromString("        SELECT 1"                            ,Category.INSERTED));
        sqlBlockStatement.add(SqlString.createSqlStringFromString("        FROM sys.columns c"                  ,Category.INSERTED));
        sqlBlockStatement.add(SqlString.createSqlStringFromString("        WHERE c.object_id = OBJECT_ID('%s')" ,Category.INSERTED,plainSchemaAndTableName));
        sqlBlockStatement.add(SqlString.createSqlStringFromString("            AND c.name = '%s'"               ,Category.INSERTED,newName.cloneWithoutDelimiters()));
        sqlBlockStatement.add(SqlString.createSqlStringFromString("    )"                                       ,Category.INSERTED));
        sqlBlockStatement.add(SqlString.createSqlStringFromString("BEGIN"                                       ,Category.INSERTED));
        sqlBlockStatement.add(SqlString.createSqlStringFromString("    EXEC %s '%s.%s',"                        ,Category.INSERTED,tokenStatement, plainSchemaAndTableName,oldName.cloneWithoutDelimiters()));
        sqlBlockStatement.add(SqlString.createSqlStringFromString("                   '%s',"                    ,Category.INSERTED,newName.cloneWithoutDelimiters()));
        sqlBlockStatement.add(SqlString.createSqlStringFromString("                   '%s';"                    ,Category.INSERTED,type.cloneWithoutDelimiters()));
        sqlBlockStatement.add(SqlString.createSqlStringFromString("END;"                                        ,Category.INSERTED));

		SqlCharacter sqlCharacter=tokenStatement.getFirstCharacter();
		sqlBlockStatement.setBackgroundColor(sqlCharacter.getBackgroundColor());
		sqlBlock.add(sqlBlockStatement);
	}

}
