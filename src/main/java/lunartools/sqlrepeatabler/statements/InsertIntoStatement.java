package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
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
		Token tokenFirstColumnName=getFirstValueFromCsvToken(tokenColumnNames);
		if(!tokenFirstColumnName.toString().equalsIgnoreCase("ID")){
			logger.warn("INSERT INTO: First column name is '"+tokenFirstColumnName.toString()+"', expected is 'ID' !");
		}

		for(int i=0;i<columnValuesTokensList.size();i++) {
			Token tokenAllValues=columnValuesTokensList.get(i).clone();
			tokenAllValues.removeEnclosing('(',')');
			Token[] columnValues=tokenAllValues.split(',');
			Token tokenFirstColumnValue=columnValues[0];
			sqlBlock.add(SqlString.createSqlStringFromString("if (select COUNT(*) from %s where %s=%s)=0", Category.INSERTED,tableName.getFullName(),tokenFirstColumnName,tokenFirstColumnValue));
			sqlBlock.add(SqlString.createSqlStringFromString("BEGIN", Category.INSERTED));
			sqlBlock.add(SqlString.createSqlStringFromString("\tSET IDENTITY_INSERT %s ON;",Category.INSERTED,tableName.getFullSchemaAndName()));
			sqlBlock.add(SqlString.createSqlStringFromString("\t\t%s %s %s VALUES", Category.INSERTED,tokenStatement,tableName.getFullName(),tokenColumnNames));
			sqlBlock.add(SqlString.createSqlStringFromString("\t\t\t%s;", Category.INSERTED,columnValuesTokensList.get(i)));
			sqlBlock.add(SqlString.createSqlStringFromString("\tSET IDENTITY_INSERT %s OFF;", Category.INSERTED,tableName.getFullSchemaAndName()));
			sqlBlock.add(SqlString.createSqlStringFromString("END;", Category.INSERTED));

			if(i<columnValuesTokensList.size()-1) {
				sqlBlock.add(SqlString.EMPTY_LINE);
			}
		}
	}

	private Token getFirstValueFromCsvToken(Token token) throws CloneNotSupportedException {
		Token tokenClone=token.clone();
		tokenClone.removeEnclosing('(',')');
		Token[] tokenArray=tokenClone.split(',');
		return tokenArray[0];
	}

}
