package lunartools.sqlrepeatabler.statement;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlCharacter;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.parser.Token;

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
	public void toSqlCharacters(SqlBlock sqlBlock) throws Exception {
		SqlBlock sqlBlockStatement=new SqlBlock();
		
		Token tokenFirstColumnName=getFirstValueFromCsvToken(tokenColumnNames);
		if(!tokenFirstColumnName.toString().equalsIgnoreCase("ID")){
			logger.warn("INSERT INTO: First column name is '"+tokenFirstColumnName.toString()+"', usually it is 'ID' !");
		}
		Token tokenValues=new Token("VALUES",Category.COMMAND);

		for(int i=0;i<columnValuesTokensList.size();i++) {
			Token tokenAllValues=columnValuesTokensList.get(i).clone();
			tokenAllValues.removeEnclosing('(',')');
			Token[] columnValues=tokenAllValues.split(',');
			Token tokenFirstColumnValue=columnValues[0];
			sqlBlockStatement.add(SqlString.createSqlStringFromString("if (select COUNT(*) from %s where %s=%s)=0", Category.INSERTED,tableName.getFullName(),tokenFirstColumnName,tokenFirstColumnValue));
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
		sqlBlock.add(sqlBlockStatement);
	}

	private Token getFirstValueFromCsvToken(Token token) throws CloneNotSupportedException {
		Token tokenClone=token.clone();
		tokenClone.removeEnclosing('(',')');
		Token[] tokenArray=tokenClone.split(',');
		return tokenArray[0];
	}

}
