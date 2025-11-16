package lunartools.sqlrepeatabler.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlParserException;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.parser.StatementTokenizer;
import lunartools.sqlrepeatabler.parser.Token;

public class SpRenameStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(SpRenameStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().toUpperCase().startsWith(SpRenameStatement.COMMAND);
	}

	@Override
	public Statement createStatement(SqlScript sqlScript) throws Exception{
		if(!match(sqlScript.peekLineAsString())) {
			throw new Exception("Illegal factory call");
		}
		
		StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
		logger.info("Statement: "+statementTokenizer.toString());
		statementTokenizer.setBackgroundColor(null);

		Token tokenStatement=statementTokenizer.nextToken(SpRenameStatement.COMMAND);
		statementTokenizer.stripWhiteSpaceLeft();
		tokenStatement.setCategory(Category.STATEMENT);

		Token objName=statementTokenizer.nextToken();//@objname of stored procedure 'sp_rename'
		objName.removeEnclosing('\'');
		Token newName=statementTokenizer.nextToken();//@newname of stored procedure 'sp_rename'
		newName.setCategory(Category.COLUMN);
		Token objtype=statementTokenizer.nextToken();//@objtype of stored procedure 'sp_rename'
		objtype.setCategory(Category.PARAMETER);
		objtype=objtype.cloneWithoutDelimiters();
		
		if(objtype.equalsIgnoreCase("COLUMN")) {
			TableName tableName;
			Token oldName;
			Token[] subTokens=objName.split('.');
			if(subTokens.length==3) {
				tableName=new TableName(subTokens[0],subTokens[1]);
				oldName=subTokens[2];
			}else if(subTokens.length==2) {
				tableName=new TableName(subTokens[0]);
				oldName=subTokens[1];
			}else {
				throw new SqlParserException("Error parsing objectname",objName.getCharacterLocation());
			}
			oldName.setCategory(Category.COLUMN);
			return new SpRenameStatement(tokenStatement,tableName,oldName,newName,objtype);
		}else {
			throw new SqlParserException(String.format("Type not supported yet: %s", objtype.toString()), objtype.getCharacterLocation());
		}
	}
}
