package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlParser;
import lunartools.sqlrepeatabler.parser.SqlString;
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
			sb.append(String.format("if (select COUNT(*) from %s where %s=%s)=0",tableName.getFullNameAsString(),nameId,getFirstValueFromCsvInParenthesis(columnValuesTokensList.get(i).toString()))).append(SqlParser.CRLF);
			sb.append(String.format("BEGIN")).append(SqlParser.CRLF);
			sb.append(String.format("\tSET IDENTITY_INSERT %s ON;",tableName.getFullSchemaAndNameAsString())).append(SqlParser.CRLF);
			sb.append(String.format("\t\tINSERT INTO %s %s VALUES",tableName.getFullNameAsString(),tokenColumnNames.toString())).append(SqlParser.CRLF);
			sb.append(String.format("\t\t\t%s;",columnValuesTokensList.get(i).toString())).append(SqlParser.CRLF);
			sb.append(String.format("\tSET IDENTITY_INSERT %s OFF;",tableName.getFullSchemaAndNameAsString())).append(SqlParser.CRLF);
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

	private Token getFirstValueFromCsvToken(Token token) throws CloneNotSupportedException {
	    Token tokenClone=token.clone();
	    tokenClone.removeEnclosing('(',')');
	    Token[] tokenArray=tokenClone.split(',');
	    return tokenArray[0];
	}
	
    @Override
    public void toSqlCharacters(ArrayList<SqlString> sqlCharacterLines) throws Exception {
        Token tokenFirstColumnName=getFirstValueFromCsvToken(tokenColumnNames);
        if(!tokenFirstColumnName.toString().equalsIgnoreCase("ID")){
            logger.warn("INSERT INTO: First column name is '"+tokenFirstColumnName.toString()+"', expected is 'ID' !");
        }
        
        for(int i=0;i<columnValuesTokensList.size();i++) {
            Token tokenAllValues=columnValuesTokensList.get(i).clone();
            tokenAllValues.removeEnclosing('(',')');
            Token[] columnValues=tokenAllValues.split(',');
            Token tokenFirstColumnValue=columnValues[0];
            sqlCharacterLines.add(SqlString.createSqlStringFromString("if (select COUNT(*) from %s where %s=%s)=0", Category.INSERTED,tableName.getFullName(),tokenFirstColumnName,tokenFirstColumnValue));
            sqlCharacterLines.add(SqlString.createSqlStringFromString("BEGIN", Category.INSERTED));
            sqlCharacterLines.add(SqlString.createSqlStringFromString("\tSET IDENTITY_INSERT %s ON;",Category.INSERTED,tableName.getFullSchemaAndName()));
            sqlCharacterLines.add(SqlString.createSqlStringFromString("\t\tINSERT INTO %s %s VALUES", Category.INSERTED,tableName.getFullName(),tokenColumnNames));
            sqlCharacterLines.add(SqlString.createSqlStringFromString("\t\t\t%s;", Category.INSERTED,columnValuesTokensList.get(i)));
            sqlCharacterLines.add(SqlString.createSqlStringFromString("\tSET IDENTITY_INSERT %s OFF;", Category.INSERTED,tableName.getFullSchemaAndName()));
            sqlCharacterLines.add(SqlString.createSqlStringFromString("END;", Category.INSERTED));

            //sb.append(String.format("if (select COUNT(*) from %s where %s=%s)=0",tableName.getFullNameAsString(),nameId,getFirstValueFromCsvInParenthesis(columnValuesTokensList.get(i).toString()))).append(SqlParser.CRLF);
            //sb.append(String.format("BEGIN")).append(SqlParser.CRLF);
            //sb.append(String.format("\tSET IDENTITY_INSERT %s ON;",tableName.getFullSchemaAndName())).append(SqlParser.CRLF);
            //sb.append(String.format("\t\tINSERT INTO %s %s VALUES",tableName.getFullNameAsString(),tokenColumnNames.toString())).append(SqlParser.CRLF);
            //sb.append(String.format("\t\t\t%s;",columnValuesTokensList.get(i).toString())).append(SqlParser.CRLF);
            //sb.append(String.format("\tSET IDENTITY_INSERT %s OFF;",tableName.getFullSchemaAndName())).append(SqlParser.CRLF);
            //sb.append(String.format("END;")).append(SqlParser.CRLF);
//            if(i<columnValuesTokensList.size()-1) {
//                sb.append(SqlParser.CRLF);
//            }
        }
    }

}
