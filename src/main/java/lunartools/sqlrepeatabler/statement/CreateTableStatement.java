package lunartools.sqlrepeatabler.statement;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlCharacter;
import lunartools.sqlrepeatabler.parser.SqlParserException;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

public class CreateTableStatement implements Statement{
    private static Logger logger = LoggerFactory.getLogger(CreateTableStatement.class);
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
		
//		sqlBlockStatement.add(SqlString.createSqlStringFromString(	"IF OBJECT_ID(N'%s', 'U') IS NULL"	        ,Category.INSERTED, tableName.getFullNameWithoutDelimiter()));
        sqlBlockStatement.add(SqlString.createSqlStringFromString(  "IF NOT EXISTS ("                           ,Category.INSERTED));
        sqlBlockStatement.add(SqlString.createSqlStringFromString(  "    SELECT 1"                              ,Category.INSERTED));
        sqlBlockStatement.add(SqlString.createSqlStringFromString(  "    FROM sys.objects"                      ,Category.INSERTED));
        sqlBlockStatement.add(SqlString.createSqlStringFromString(  "    WHERE object_id = OBJECT_ID(N'%s')"    ,Category.INSERTED,tableName.getFullNameWithoutDelimiter()));
        sqlBlockStatement.add(SqlString.createSqlStringFromString(  "        AND type = 'U'"                    ,Category.INSERTED));
        sqlBlockStatement.add(SqlString.createSqlStringFromString(  ")"                                         ,Category.INSERTED));
		sqlBlockStatement.add(SqlString.createSqlStringFromString(	"BEGIN"								,Category.INSERTED));
		sqlBlockStatement.add(SqlString.createSqlStringFromString(	"    %s %s ("						,Category.INSERTED,tokenStatement,tableName.getFullName()));

		SqlString sqlString;
		for(int i=0;i<tokens.size();i++) {
		    Token token=tokens.get(i);
		    if(token.toString().toUpperCase().startsWith("PRIMARY KEY")) {
		        try {
		            token=updatePrimaryKeyToken(token);
		            logger.info(String.format("Added a nice CONSTRAINT name to the PRIMARY KEY declaration: %s",token));
                } catch (Exception e) {
                    logger.warn("Error while adding CONSTRAINT name. Using PRIMARY KEY declaration without CONSTRAINT name.");
                }
		    }
		    sqlString=SqlString.createSqlStringFromString(			"        %s"						,Category.INSERTED,token);
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
	
	private Token updatePrimaryKeyToken(Token token) throws Exception {
        Pattern patternColumnsList = Pattern.compile("PRIMARY\\s+KEY\\s*\\(([^)]+)\\)", Pattern.CASE_INSENSITIVE);
        Matcher matcherColumnsList = patternColumnsList.matcher(token.toString());
        if(matcherColumnsList.find()) {
            String columnsListAsString = matcherColumnsList.group(1).trim();
            String[] columnsArray = columnsListAsString.split("\\s*,\\s*");
            String firstColumnName = columnsArray[0].replaceAll("\\[|\\]|\\\"", "");
            token=new Token(String.format("CONSTRAINT PK_%s_%s",tableName.getTableNameWithoutDelimiter(),firstColumnName),Category.INSERTED).append(SqlCharacter.SEPARATOR).append(token);
        } else {
            throw new SqlParserException("Error extracting constraint column list.",token.getLocation());
        }
	    return token;
	}

}
