package lunartools.sqlrepeatabler.parser;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableName {
	private static Logger logger = LoggerFactory.getLogger(TableName.class);
	private Token databaseName;
	private Token schemaName;
	private Token tableName;

	private TableName(ArrayList<Token> tokens) {
		this.databaseName=tokens.get(0);
		this.schemaName=tokens.get(1);
		this.tableName=tokens.get(2);
	}

	public TableName(Token schemaName, Token tableName) {
		this.schemaName=schemaName;
		this.tableName=tableName;
		this.schemaName.setCategory(Category.TABLE);
		this.tableName.setCategory(Category.TABLE);
	}

	public TableName(Token table) {
		Token[] tokens=table.split('.');
		if(tokens.length==1) {
			schemaName=new Token("dbo",Category.TABLE);
			tableName=table;
		}else {
			schemaName=tokens[0];
			schemaName.setCategory(Category.TABLE);
			tableName=tokens[1];
		}
		tableName.setCategory(Category.TABLE);
	}

	public static TableName createInstanceByConsuming(StatementTokenizer statementTokenizer){
		boolean mySql=false;
		ArrayList<Token> tokens=new ArrayList<>();
		CharacterLocation statementLocation=statementTokenizer.getLocation();
		for(int i=0;i<3;i++) {//database name, schema name, table name
			statementTokenizer.stripWhiteSpaceLeft();
			SqlCharacter character=statementTokenizer.charAt(0);
			if(character.getChar()=='[') {
				Token token=createBracketEnclosedTokenByConsuming(statementTokenizer);
				token.setCategory(Category.TABLE);
				tokens.add(token);
			}else if(character.getChar()=='"') {
				Token token=createQuoteEnclosedTokenByConsuming(statementTokenizer);
				token.setCategory(Category.TABLE);
				tokens.add(token);
			}else if(character.getChar()=='`') {
				mySql=true;
				Token token=createBacktickEnclosedTokenByConsuming(statementTokenizer);
				token.setCategory(Category.TABLE);
				tokens.add(token);
			}else {
				Token token=createSpaceEnclosedTokenByConsuming(statementTokenizer);
				token.setCategory(Category.TABLE);
				tokens.add(token);
			}
			statementTokenizer.stripWhiteSpaceLeft();
			if(statementTokenizer.charAt(0).getChar()!='.') {
				break;
			}
			statementTokenizer.deleteCharAt(0);
		}
		if(mySql) {
			logger.warn(String.format("It smells like MySql! Replaced backtick delimiter in table declaration with square brackets, because backticks are not allowed in T-SQL. %s", statementLocation) );
		}
		while(tokens.size()<3) {
			tokens.add(0,null);
		}
		return new TableName(tokens);
	}

	private static Token createBracketEnclosedTokenByConsuming(StatementTokenizer statementTokenizer) {
		SqlString sqlString=new SqlString();
		sqlString.append(statementTokenizer.charAt(0));
		statementTokenizer.deleteCharAt(0);
		while(true) {
			if(statementTokenizer.charAt(0).getChar()==']') {
				sqlString.append(statementTokenizer.charAt(0));
				statementTokenizer.deleteCharAt(0);
				if(statementTokenizer.charAt(0).getChar()==']') {
					continue;
				}else {
					break;
				}
			}
			sqlString.append(statementTokenizer.charAt(0));
			statementTokenizer.deleteCharAt(0);
		}
		return new Token(sqlString);
	}

	private static Token createQuoteEnclosedTokenByConsuming(StatementTokenizer statementTokenizer) {
		SqlString sqlString=new SqlString();
		sqlString.append(statementTokenizer.charAt(0));
		statementTokenizer.deleteCharAt(0);
		while(true) {
			if(statementTokenizer.charAt(0).getChar()=='"') {
				sqlString.append(statementTokenizer.charAt(0));
				statementTokenizer.deleteCharAt(0);
				if(statementTokenizer.charAt(0).getChar()=='"') {
					continue;
				}else {
					break;
				}
			}
			sqlString.append(statementTokenizer.charAt(0));
			statementTokenizer.deleteCharAt(0);
		}
		return new Token(sqlString);
	}

	private static Token createBacktickEnclosedTokenByConsuming(StatementTokenizer statementTokenizer) {
		SqlString sqlString=new SqlString();
		sqlString.append(new SqlCharacter('['));
		statementTokenizer.deleteCharAt(0);
		while(true) {
			if(statementTokenizer.charAt(0).getChar()=='`') {
				if(statementTokenizer.charAt(1).getChar()=='`') {
					sqlString.append(statementTokenizer.charAt(0));
					sqlString.append(statementTokenizer.charAt(1));
					statementTokenizer.deleteCharAt(0);
					statementTokenizer.deleteCharAt(0);
					continue;
				}else {
					sqlString.append(new SqlCharacter(']'));
					statementTokenizer.deleteCharAt(0);
					break;
				}
			}
			sqlString.append(statementTokenizer.charAt(0));
			statementTokenizer.deleteCharAt(0);
		}
		return new Token(sqlString);
	}

	private static Token createSpaceEnclosedTokenByConsuming(StatementTokenizer statementTokenizer) {
		SqlString sqlString=new SqlString();
		sqlString.append(statementTokenizer.charAt(0));
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
			sqlString.append(statementTokenizer.charAt(0));
			statementTokenizer.deleteCharAt(0);
		}
		return new Token(sqlString);
	}

	public Token getDatabaseName() {
		return databaseName;
	}

	public Token getSchemaName() {
		return schemaName;
	}

	public Token getTableName() {
		return tableName;
	}

	public Token getDatabaseNameWithoutDelimiter() {
		if(databaseName!=null) {
			try {
				Token token = (Token)databaseName.clone();
				token.removeEnclosing('[',']');
				token.removeEnclosing('\"');
				return token;
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException("Unexpected error cloning a token",e);
			}
		}
		return null;
	}

	public Token getSchemaNameWithoutDelimiter(){
		if(schemaName!=null) {
			try {
				Token token=(Token)schemaName.clone();
				token.removeEnclosing('[',']');
				token.removeEnclosing('\"');
				return token;
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException("Unexpected error cloning a token",e);
			}
		}
		return null;
	}

	public Token getTableNameWithoutDelimiter(){
		if(tableName!=null) {
			try {
				Token token = (Token)tableName.clone();
				token.removeEnclosing('[',']');
				token.removeEnclosing('\"');
				return token;
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException("Unexpected error cloning a token",e);
			}
		}
		return null;
	}

	public Token getFullName() {
		Token tokenFullname=databaseName;
		if(tokenFullname==null) {
			tokenFullname=schemaName;
		}else {
			tokenFullname.append(new SqlCharacter('.'));
			tokenFullname.append(schemaName);
		}
		if(tokenFullname==null) {
			tokenFullname=tableName;
		}else {
			tokenFullname.append(new SqlCharacter('.'));
			tokenFullname.append(tableName);
		}
		return tokenFullname;
	}

	public Token getFullSchemaAndName() {
		Token tokenFullname=schemaName;
		if(tokenFullname==null) {
			tokenFullname=tableName;
		}else {
			tokenFullname.append(new SqlCharacter('.'));
			tokenFullname.append(tableName);
		}
		return tokenFullname;
	}

	public Token getFullNameWithoutDelimiter(){
		Token tokenFullname=getDatabaseNameWithoutDelimiter();
		if(tokenFullname==null) {
			tokenFullname=getSchemaNameWithoutDelimiter();
		}else {
			tokenFullname.append(new SqlCharacter('.'));
			tokenFullname.append(getSchemaNameWithoutDelimiter());
		}
		if(tokenFullname==null) {
			tokenFullname=getTableNameWithoutDelimiter();
		}else {
			tokenFullname.append(new SqlCharacter('.'));
			tokenFullname.append(getTableNameWithoutDelimiter());
		}
		return tokenFullname;
	}

	@Override
	public String toString() {
		return String.format("Table: DatabaseName=%s, SchemaName=%s, TableName=%s",databaseName,schemaName,tableName);
	}
}
