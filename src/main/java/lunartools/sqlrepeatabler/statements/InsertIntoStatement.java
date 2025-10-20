package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.SqlParser;
import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Token;

public class InsertIntoStatement implements Statement{
	private static Logger logger = LoggerFactory.getLogger(InsertIntoStatement.class);
	public static final String COMMAND="INSERT INTO";
	private TableName tableName;
	private Token tokenColumnNames;
	private ArrayList<Token> columnValuesTokensList;

	public InsertIntoStatement(TableName tableName,Token tokenColumnNames,ArrayList<Token> columnValuesTokensList) {
		this.tableName=tableName;
		this.tokenColumnNames=tokenColumnNames;
		this.columnValuesTokensList=columnValuesTokensList;
	}
	
	@Override
	public void toSql(StringBuilder sb) throws Exception {
		Token tokenColumnNamesClone=tokenColumnNames.clone();
		tokenColumnNamesClone.removeEnclosing('(',')');
		Token[] columnValuesTokenArray=tokenColumnNamesClone.split(',');
		String nameId=columnValuesTokenArray[0].toString();
		if(!nameId.equalsIgnoreCase("ID")){
			logger.warn("INSERT INTO: First column name is '"+nameId+"', expected is 'ID' !");
		}
		for(int i=0;i<columnValuesTokensList.size();i++) {
			sb.append(String.format("if (select COUNT(*) from %s where %s=%s)=0",tableName.getFullName(),nameId,getFirstValueFromCsvInParenthesis(columnValuesTokensList.get(i).toString()))).append(SqlParser.CRLF);
			sb.append(String.format("BEGIN")).append(SqlParser.CRLF);
			sb.append(String.format("\tSET IDENTITY_INSERT %s ON;",tableName.getFullSchemaAndName())).append(SqlParser.CRLF);
			sb.append(String.format("\t\tINSERT INTO %s %s VALUES",tableName.getFullName(),tokenColumnNames.toString())).append(SqlParser.CRLF);
			sb.append(String.format("\t\t\t%s;",columnValuesTokensList.get(i).toString())).append(SqlParser.CRLF);
			sb.append(String.format("\tSET IDENTITY_INSERT %s OFF;",tableName.getFullSchemaAndName())).append(SqlParser.CRLF);
			sb.append(String.format("END;")).append(SqlParser.CRLF);
			if(i<columnValuesTokensList.size()-1) {
				sb.append(SqlParser.CRLF);
			}
		}
	}

	//TODO: remove method after parser refactoring
	private String getFirstValueFromCsvInParenthesis(String s) {
		int start=s.indexOf('(')+1;
		int end=s.indexOf(',', start);
		return s.substring(start,end);
	}

}
