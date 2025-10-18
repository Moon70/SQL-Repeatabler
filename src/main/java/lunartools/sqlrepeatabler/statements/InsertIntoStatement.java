package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.SqlParser;
import lunartools.sqlrepeatabler.common.TableName;

public class InsertIntoStatement implements Statement{
	private static Logger logger = LoggerFactory.getLogger(InsertIntoStatement.class);
	public static final String COMMAND="INSERT INTO";
	private TableName tableName;
	private String columnNames;
	private ArrayList<String> columnValuesList;

	public InsertIntoStatement(TableName tableName,String columnNames,ArrayList<String> columnValuesList) {
		this.tableName=tableName;
		this.columnNames=columnNames;
		this.columnValuesList=columnValuesList;
	}
	
	@Override
	public void toSql(StringBuilder sb) throws Exception {
		String nameId=getFirstValueFromCsvInbraces(columnNames);
		if(!nameId.equalsIgnoreCase("ID")){
			logger.warn("INSERT INTO: First column name is '"+nameId+"', expected is 'ID' !");
		}
		for(int i=0;i<columnValuesList.size();i++) {
			sb.append(String.format("if (select COUNT(*) from %s where %s=%s)=0",tableName.getFullName(),nameId,getFirstValueFromCsvInbraces(columnValuesList.get(i)))).append(SqlParser.CRLF);
			sb.append(String.format("BEGIN")).append(SqlParser.CRLF);
			sb.append(String.format("\tSET IDENTITY_INSERT %s ON;",tableName.getFullSchemaAndName())).append(SqlParser.CRLF);
			sb.append(String.format("\t\tINSERT INTO %s %s VALUES",tableName.getFullName(),columnNames)).append(SqlParser.CRLF);
			sb.append(String.format("\t\t\t%s;",columnValuesList.get(i))).append(SqlParser.CRLF);
			sb.append(String.format("\tSET IDENTITY_INSERT %s OFF;",tableName.getFullSchemaAndName())).append(SqlParser.CRLF);
			sb.append(String.format("END;")).append(SqlParser.CRLF);
			if(i<columnValuesList.size()-1) {
				sb.append(SqlParser.CRLF);
			}
		}
	}

	private String getFirstValueFromCsvInbraces(String s) {
		int start=s.indexOf('(')+1;
		int end=s.indexOf(',', start);
		return s.substring(start,end);
	}

}
