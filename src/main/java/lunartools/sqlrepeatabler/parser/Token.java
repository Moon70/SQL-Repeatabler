package lunartools.sqlrepeatabler.parser;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Token {
	private static Logger logger = LoggerFactory.getLogger(Token.class);
	private ArrayList<SqlCharacter> charactersOfToken;

	public Token(SqlString sqlString) {
		this.charactersOfToken=sqlString.getCharacters();
		trim();
	}

	public Token(ArrayList<SqlCharacter> charactersOfToken) {
		this.charactersOfToken=charactersOfToken;
		trim();
	}

	public Token(String s,Category category) {
		ArrayList<SqlCharacter> characters=new ArrayList<>();
		for(int k=0;k<s.length();k++) {
			SqlCharacter sqlCharacter=new SqlCharacter(s.charAt(k),-1,-1,-1);
			sqlCharacter.setCategory(category);
			characters.add(sqlCharacter);
		}
		this.charactersOfToken=characters;
		trim();
	}

	public Token[] split(char c) {
		ArrayList<Token> tokens=new ArrayList<>();
		ArrayList<SqlCharacter> charactersOfSubtoken=new ArrayList<>();
		for(int i=0;i<charactersOfToken.size();i++) {
			SqlCharacter character=charactersOfToken.get(i);
			if(character.getChar()==c) {
				Token token=new Token(charactersOfSubtoken);
				token.trim();
				tokens.add(token);
				charactersOfSubtoken=new ArrayList<>();
			}else {
				charactersOfSubtoken.add(character);
			}
		}
		Token token=new Token(charactersOfSubtoken);
		token.trim();
		tokens.add(token);
		return tokens.toArray(new Token[0]);
	}

	public void removeEnclosing(char c) {
		removeEnclosing(c, c);
	}

	public void removeEnclosing(char cLeft, char cRight) {
		if(
				charactersOfToken.get(0).getChar()==cLeft &&
				charactersOfToken.get(charactersOfToken.size()-1).getChar()==cRight
				) {
			charactersOfToken.remove(charactersOfToken.size()-1);
			charactersOfToken.remove(0);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Token clone() throws CloneNotSupportedException {
		return new Token((ArrayList<SqlCharacter>)charactersOfToken.clone());
	}

	public Token cloneWithoutDelimiters() throws CloneNotSupportedException {
		Token token=(Token)this.clone();
		token.removeEnclosing('[',']');
		token.removeEnclosing('"');
		return token;
	}

	private void trim() {
		while(charactersOfToken.size()>0 && charactersOfToken.get(0).isWhiteSpace()) {
			charactersOfToken.remove(0);
		}
		while(charactersOfToken.size()>0 && charactersOfToken.get(charactersOfToken.size()-1).isWhiteSpace()) {
			charactersOfToken.remove(charactersOfToken.size()-1);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for(SqlCharacter character:charactersOfToken) {
			sb.append(character.getChar());
		}
		return sb.toString();
	}

	public void setCategory(Category category) {
		for(SqlCharacter sqlCharacter:charactersOfToken) {
			sqlCharacter.setCategory(category);
		}
	}

	public ArrayList<SqlCharacter> getCharacters() {
		return charactersOfToken;
	}

	public void append(Token token) {
		charactersOfToken.addAll(token.getCharacters());
	}

	public void append(SqlCharacter character) {
		charactersOfToken.add(character);
	}

	public boolean fixMySqlDelimiter(){
		boolean isMySql=false;
		isMySql=true;
		boolean openBracket=false;
		for(int i=0;i<charactersOfToken.size();i++) {
			SqlCharacter sqlCharacter=charactersOfToken.get(i);
			if(sqlCharacter.getChar()=='`') {
				if(openBracket) {
					sqlCharacter.setChar(']');
				}else {
					sqlCharacter.setChar('[');
				}
				openBracket=!openBracket;
			}
		}
		if(replaceIfExists("auto_increment","identity")) {
			logger.warn("Script is most likely MySql flavour! Replacing 'auto_increment' with 'identity'!");
		}

		return isMySql;
	}

	private boolean replaceIfExists(String search,String replace) {
		int p=toString().toLowerCase().indexOf(search);
		boolean isMySql=p!=-1;
		if(isMySql) {
			isMySql=true;
			Category category=charactersOfToken.get(0).getCategory();
			for(int i=0;i<search.length();i++) {
				charactersOfToken.get(p).setCategory(Category.IGNORED);
				charactersOfToken.remove(p);
			}
			SqlString sqlString=SqlString.createSqlStringFromString(replace, category);
			charactersOfToken.addAll(p, sqlString.getCharacters());
		}
		return isMySql;
	}

	public Token toUpperCase() {
		ArrayList<SqlCharacter> charactersUppercase=new ArrayList<>();
		for(SqlCharacter sqlCharacter:charactersOfToken) {
			SqlCharacter sqlCharacterUppercase=new SqlCharacter(Character.toUpperCase(sqlCharacter.getChar()),sqlCharacter.getRow(),sqlCharacter.getColumn(),sqlCharacter.getIndex(),sqlCharacter.getCategory());
			sqlCharacterUppercase.setBackgroundColor(sqlCharacter.getBackgroundColor());
			charactersUppercase.add(sqlCharacterUppercase);
		}
		return new Token(charactersUppercase);
	}

	public SqlCharacter getFirstCharacter() {
		if(charactersOfToken.size()==0) {
			return null;
		}
		return charactersOfToken.get(0);
	}

	public boolean equalsIgnoreCase(String string) {
		if(charactersOfToken.size()!=string.length()) {
			return false;
		}
		String stringLowerCase=string.toLowerCase();
		for(int i=0;i<charactersOfToken.size();i++) {
			if(Character.toLowerCase(charactersOfToken.get(i).getChar())!=stringLowerCase.charAt(i)) {
				return false;
			}
		}
		return true;
	}

}
