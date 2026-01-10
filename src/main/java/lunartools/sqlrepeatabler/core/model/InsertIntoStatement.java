package lunartools.sqlrepeatabler.core.model;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsertIntoStatement implements Statement{
	private static Logger logger = LoggerFactory.getLogger(InsertIntoStatement.class);
	public static final String COMMAND="INSERT INTO";
	private Token tokenStatement;
	private TableName tableName;
	private Token tokenColumnNames;
	private ArrayList<Token> columnValuesTokensList;

	public InsertIntoStatement(Token statement,TableName tableName,Token tokenColumnNames,ArrayList<Token> columnValuesTokensList) {
		this.tokenStatement=statement;
		this.tableName=tableName;
		this.tokenColumnNames=tokenColumnNames;
		this.columnValuesTokensList=columnValuesTokensList;
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock){
		SqlBlock sqlBlockStatement=new SqlBlock();

		Token tokenFirstColumnName=getFirstValueFromCsvToken(tokenColumnNames);
		if(!tokenFirstColumnName.toString().equalsIgnoreCase("ID")){
			logger.warn(String.format("INSERT INTO: Unique key column name is '%s', usually it is 'ID' !",tokenFirstColumnName));
		}
		Token tokenValues=new Token("VALUES",Category.COMMAND);

		ArrayList<Token> tokensToBeMarkedAsWarning=new ArrayList<>();
		for(int i=0;i<columnValuesTokensList.size();i++) {
			Token tokenAllValues;
			tokenAllValues = columnValuesTokensList.get(i).clone();
			tokenAllValues.removeEnclosing('(',')');
			Token[] columnValues=tokenAllValues.split(',');
			Token tokenFirstColumnValue=columnValues[0];
			try {
                Long.parseLong(tokenFirstColumnValue.toString());
            } catch (Exception e) {
                tokensToBeMarkedAsWarning.add(tokenFirstColumnValue);
            }
			sqlBlockStatement.add(SqlString.createSqlStringFromString("IF NOT EXISTS (SELECT 1 FROM %s WHERE %s=%s)", Category.INSERTED,tableName.getFullName(),tokenFirstColumnName,tokenFirstColumnValue));
			sqlBlockStatement.add(SqlString.createSqlStringFromString("BEGIN", Category.INSERTED));
			sqlBlockStatement.add(SqlString.createSqlStringFromString("\tSET IDENTITY_INSERT %s ON;",Category.INSERTED,tableName.getFullSchemaAndName()));
			sqlBlockStatement.add(SqlString.createSqlStringFromString("\t\t%s %s %s %s", Category.INSERTED,tokenStatement,tableName.getFullName(),tokenColumnNames,tokenValues));
			sqlBlockStatement.add(SqlString.createSqlStringFromString("\t\t\t%s;", Category.INSERTED,columnValuesTokensList.get(i)));
			sqlBlockStatement.add(SqlString.createSqlStringFromString("\tSET IDENTITY_INSERT %s OFF;", Category.INSERTED,tableName.getFullSchemaAndName()));
			sqlBlockStatement.add(SqlString.createSqlStringFromString("END;", Category.INSERTED));

			if(i<columnValuesTokensList.size()-1) {
				sqlBlockStatement.add(SqlString.EMPTY_LINE);
			}
		}
		
		SqlCharacter sqlCharacter=tokenStatement.getFirstCharacter();
		sqlBlockStatement.setBackgroundColor(sqlCharacter.getBackgroundColor());
		if(tokensToBeMarkedAsWarning.size()>0) {
		    logger.warn(String.format("INSERT INTO: %d unique key values are not integers. This is fine if the column is just unique, but it may be a mistake if it is the primary key. %s",tokensToBeMarkedAsWarning.size(),tokensToBeMarkedAsWarning.get(0).getLocation()));
		    for(Token token:tokensToBeMarkedAsWarning) {
		        token.markWarn();
		    }
		}
		sqlBlock.add(sqlBlockStatement);
	}

	private Token getFirstValueFromCsvToken(Token token){
		Token tokenClone = token.clone();
		tokenClone.removeEnclosing('(',')');
		Token[] tokenArray=tokenClone.split(',');
		return tokenArray[0];
	}

}
