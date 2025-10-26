package lunartools.sqlrepeatabler.common;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlCharacter;
import lunartools.sqlrepeatabler.parser.StatementTokenizer;
import lunartools.sqlrepeatabler.parser.Token;

public class TableName {
	private static Logger logger = LoggerFactory.getLogger(TableName.class);
	private Token databaseName;
	private Token schemaName;
	//private String schemaName="[dbo]";
	private Token tableName;
	private boolean mySql;

	private TableName(ArrayList<Token> segments, boolean mySql) {
		this.databaseName=segments.get(0);
		this.schemaName=segments.get(1);
		this.tableName=segments.get(2);
		this.mySql=mySql;
	}

	public static TableName createInstanceByConsuming(StatementTokenizer statementTokenizer) throws Exception{
		boolean mySql=false;
		ArrayList<Token> tokens=new ArrayList<>();

		for(int i=0;i<3;i++) {//database name, schema name, table name
			statementTokenizer.stripWhiteSpaceLeft();
			SqlCharacter character=statementTokenizer.charAt(0);
			if(character.getChar()=='[') {
				Token token=createBracketSegmentByConsuming(statementTokenizer);
				token.categorize(Category.TABLE);
				tokens.add(token);
			}else if(character.getChar()=='"') {
				Token token=createQuoteSegmentByConsuming(statementTokenizer);
				//token.categorize(Category.TABLE);
				tokens.add(token);
			}else if(character.getChar()=='`') {
				mySql=true;
				Token token=createBacktickSegmentByConsuming(statementTokenizer);
				//token.categorize(Category.TABLE);
				tokens.add(token);
			}else {
				Token token=createSpaceSegmentByConsuming(statementTokenizer);
				//token.categorize(Category.TABLE);
				tokens.add(token);
			}
			statementTokenizer.stripWhiteSpaceLeft();
			if(statementTokenizer.charAt(0).getChar()!='.') {
				break;
			}
			statementTokenizer.deleteCharAt(0);
		}
		while(tokens.size()<3) {
			tokens.add(0,null);
		}
		return new TableName(tokens,mySql);
	}

	private static Token createBracketSegmentByConsuming(StatementTokenizer statementTokenizer) {
		ArrayList<SqlCharacter> sbSegment=new ArrayList<>();
		sbSegment.add(statementTokenizer.charAt(0));
		statementTokenizer.deleteCharAt(0);
		while(true) {
			if(statementTokenizer.charAt(0).getChar()==']') {
				sbSegment.add(statementTokenizer.charAt(0));
				statementTokenizer.deleteCharAt(0);
				if(statementTokenizer.charAt(0).getChar()==']') {
					continue;
				}else {
					break;
				}
			}
			sbSegment.add(statementTokenizer.charAt(0));
			statementTokenizer.deleteCharAt(0);
		}
		return new Token(sbSegment);
	}

	private static Token createQuoteSegmentByConsuming(StatementTokenizer statementTokenizer) {
		ArrayList<SqlCharacter> sbSegment=new ArrayList<>();
		sbSegment.add(statementTokenizer.charAt(0));
		statementTokenizer.deleteCharAt(0);
		while(true) {
			if(statementTokenizer.charAt(0).getChar()=='"') {
				sbSegment.add(statementTokenizer.charAt(0));
				statementTokenizer.deleteCharAt(0);
				if(statementTokenizer.charAt(0).getChar()=='"') {
					continue;
				}else {
					break;
				}
			}
			sbSegment.add(statementTokenizer.charAt(0));
			statementTokenizer.deleteCharAt(0);
		}
		return new Token(sbSegment);
	}

	private static Token createBacktickSegmentByConsuming(StatementTokenizer statementTokenizer) {
		ArrayList<SqlCharacter> sbSegment=new ArrayList<>();
		sbSegment.add(new SqlCharacter('[',-1,-1,-1));
		statementTokenizer.deleteCharAt(0);
		while(true) {
			if(statementTokenizer.charAt(0).getChar()=='`') {
				if(statementTokenizer.charAt(1).getChar()=='`') {
					sbSegment.add(statementTokenizer.charAt(0));
					sbSegment.add(statementTokenizer.charAt(1));
					statementTokenizer.deleteCharAt(0);
					statementTokenizer.deleteCharAt(0);
					continue;
				}else {
					sbSegment.add(new SqlCharacter(']',-1,-1,-1));
					statementTokenizer.deleteCharAt(0);
					break;
				}
			}
			sbSegment.add(statementTokenizer.charAt(0));
			statementTokenizer.deleteCharAt(0);
		}
		return new Token(sbSegment);
	}

	private static Token createSpaceSegmentByConsuming(StatementTokenizer statementTokenizer) {
		ArrayList<SqlCharacter> sbSegment=new ArrayList<>();
		sbSegment.add(statementTokenizer.charAt(0));
		statementTokenizer.deleteCharAt(0);
		while(true) {
			if(statementTokenizer.charAt(0).isSpace()) {
				statementTokenizer.deleteCharAt(0);
				if(statementTokenizer.charAt(0).isSpace()) {
					continue;
				}else {
					break;
				}
			}
			sbSegment.add(statementTokenizer.charAt(0));
			statementTokenizer.deleteCharAt(0);
		}
		return new Token(sbSegment);
	}

	public String getDatabaseName() {
		return databaseName.toString();
	}

	public String getSchemaName() {
		return schemaName.toString();
	}

	public String getTableName() {
		return tableName.toString();
	}

	public String getDatabaseNameWithoutDelimiterAsString() {
		String databaseNameString=getDatabaseName();
		if(databaseNameString.startsWith("[") && databaseNameString.endsWith("]")) {
			return databaseNameString.substring(1, databaseNameString.length()-1);
		}else if(databaseNameString.startsWith("\"") && databaseNameString.endsWith("\"")) {
			return databaseNameString.substring(1, databaseNameString.length()-1);
		}else {
			return databaseNameString;
		}
	}

	public String getSchemaNameWithoutDelimiterAsString() {
		String schemaNameString=getSchemaName();
		if(schemaNameString.startsWith("[") && schemaNameString.endsWith("]")) {
			return schemaNameString.substring(1, schemaNameString.length()-1);
		}else if(schemaNameString.startsWith("\"") && schemaNameString.endsWith("\"")) {
			return schemaNameString.substring(1, schemaNameString.length()-1);
		}else {
			return schemaNameString;
		}
	}

	public String getTableNameWithoutDelimiterAsString() {
		String tableNameString=getTableName();
		if(tableNameString.startsWith("[") && tableNameString.endsWith("]")) {
			return tableNameString.substring(1, tableNameString.length()-1);
		}else if(tableNameString.startsWith("\"") && tableNameString.endsWith("\"")) {
			return tableNameString.substring(1, tableNameString.length()-1);
		}else {
			return tableNameString;
		}
	}

	public String getFullName() {
		StringBuffer sb=new StringBuffer();
		if(databaseName!=null) {
			sb.append(databaseName);
			sb.append('.');
		}
		if(schemaName!=null) {
			sb.append(schemaName);
			sb.append('.');
		}
		sb.append(tableName);
		return sb.toString();
	}

	public String getFullSchemaAndName() {
		StringBuffer sb=new StringBuffer();
		if(schemaName!=null) {
			sb.append(schemaName);
			sb.append('.');
		}
		sb.append(tableName);
		return sb.toString();
	}

	public String getFullNameWithoutDelimiter() {
		StringBuffer sb=new StringBuffer();
		if(databaseName!=null) {
			sb.append(getDatabaseNameWithoutDelimiterAsString());
			sb.append('.');
		}
		if(schemaName!=null) {
			sb.append(getSchemaNameWithoutDelimiterAsString());
			sb.append('.');
		}
		sb.append(getTableNameWithoutDelimiterAsString());
		return sb.toString();
	}

	public boolean isMySql() {
		return mySql;
	}

	@Override
	public String toString() {
		return String.format("Table: DatabaseName=%s, SchemaName=%s, TableName=%s",databaseName,schemaName,tableName);
	}
}
