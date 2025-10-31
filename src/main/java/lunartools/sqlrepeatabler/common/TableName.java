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
	
	private TableName(ArrayList<Token> tokens, boolean mySql) {
		this.databaseName=tokens.get(0);
		this.schemaName=tokens.get(1);
		this.tableName=tokens.get(2);
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
				token.setCategory(Category.TABLE);
				tokens.add(token);
			}else if(character.getChar()=='"') {
				Token token=createQuoteSegmentByConsuming(statementTokenizer);
				token.setCategory(Category.TABLE);
				tokens.add(token);
			}else if(character.getChar()=='`') {
				mySql=true;
				Token token=createBacktickSegmentByConsuming(statementTokenizer);
				token.setCategory(Category.TABLE);
				tokens.add(token);
			}else {
				Token token=createSpaceSegmentByConsuming(statementTokenizer);
				token.setCategory(Category.TABLE);
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

    public Token getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseNameAsString() {
        return databaseName.toString();
    }

    public Token getSchemaName() {
        return schemaName;
    }

    public String getSchemaNameAsString() {
        return schemaName.toString();
    }

    public Token getTableName() {
        return tableName;
    }

    public String getTableNameAsString() {
        return tableName.toString();
    }

    public Token getDatabaseNameWithoutDelimiter() {
        Token token=getDatabaseName();
        if(token!=null) {
            token.removeEnclosing('[',']');
            token.removeEnclosing('\"');
        }
        return token;
    }

    public String getDatabaseNameWithoutDelimiterAsString() {
        String databaseNameString=getDatabaseNameAsString();
        if(databaseNameString.startsWith("[") && databaseNameString.endsWith("]")) {
            return databaseNameString.substring(1, databaseNameString.length()-1);
        }else if(databaseNameString.startsWith("\"") && databaseNameString.endsWith("\"")) {
            return databaseNameString.substring(1, databaseNameString.length()-1);
        }else {
            return databaseNameString;
        }
    }

    public Token getSchemaNameWithoutDelimiter() {
        Token token=getSchemaName();
        if(token!=null) {
            token.removeEnclosing('[', ']');
            token.removeEnclosing('\"');
        }
        return token;
    }

    public String getSchemaNameWithoutDelimiterAsString() {
        String schemaNameString=getSchemaNameAsString();
        if(schemaNameString.startsWith("[") && schemaNameString.endsWith("]")) {
            return schemaNameString.substring(1, schemaNameString.length()-1);
        }else if(schemaNameString.startsWith("\"") && schemaNameString.endsWith("\"")) {
            return schemaNameString.substring(1, schemaNameString.length()-1);
        }else {
            return schemaNameString;
        }
    }

    public Token getTableNameWithoutDelimiter() {
        Token token=getTableName();
        if(token!=null) {
            token.removeEnclosing('[', ']');
            token.removeEnclosing('\"');
        }
        return token;
    }

    public String getTableNameWithoutDelimiterAsString() {
        String tableNameString=getTableNameAsString();
        if(tableNameString.startsWith("[") && tableNameString.endsWith("]")) {
            return tableNameString.substring(1, tableNameString.length()-1);
        }else if(tableNameString.startsWith("\"") && tableNameString.endsWith("\"")) {
            return tableNameString.substring(1, tableNameString.length()-1);
        }else {
            return tableNameString;
        }
    }

    public Token getFullName() {
        Token tokenFullname=getDatabaseName();
        if(tokenFullname==null) {
            tokenFullname=getSchemaName();
        }else {
            tokenFullname.append(new SqlCharacter('.',-1,-1,-1));
            tokenFullname.append(getSchemaName());
        }
        if(tokenFullname==null) {
            tokenFullname=getTableName();
        }else {
            tokenFullname.append(new SqlCharacter('.',-1,-1,-1));
            tokenFullname.append(getTableName());
        }
        return tokenFullname;
    }

    public String getFullNameAsString() {
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

    public Token getFullNameWithoutDelimiter() {
        Token tokenFullname=getDatabaseNameWithoutDelimiter();
        if(tokenFullname==null) {
            tokenFullname=getSchemaNameWithoutDelimiter();
        }else {
            tokenFullname.append(new SqlCharacter('.',-1,-1,-1));
            tokenFullname.append(getSchemaNameWithoutDelimiter());
        }
        if(tokenFullname==null) {
            tokenFullname=getTableNameWithoutDelimiter();
        }else {
            tokenFullname.append(new SqlCharacter('.',-1,-1,-1));
            tokenFullname.append(getTableNameWithoutDelimiter());
        }
        return tokenFullname;
    }

    public String getFullNameWithoutDelimiterAsString() {
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
