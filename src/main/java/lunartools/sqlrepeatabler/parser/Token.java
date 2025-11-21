package lunartools.sqlrepeatabler.parser;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Token extends SqlString{
	private static Logger logger = LoggerFactory.getLogger(Token.class);
	
	public Token(ArrayList<SqlCharacter> charactersOfToken) {
		this.sqlCharacters=charactersOfToken;
		trim();
	}

	public Token(SqlString sqlString) {
		this.sqlCharacters=sqlString.getCharacters();
		trim();
	}

	public Token(String s,Category category) {
		ArrayList<SqlCharacter> characters=new ArrayList<>();
		for(int k=0;k<s.length();k++) {
			SqlCharacter sqlCharacter=new SqlCharacter(s.charAt(k));
			sqlCharacter.setCategory(category);
			characters.add(sqlCharacter);
		}
		this.sqlCharacters=characters;
		trim();
	}

	public Token[] split(char c) {
		ArrayList<Token> tokens=new ArrayList<>();
		ArrayList<SqlCharacter> charactersOfSubtoken=new ArrayList<>();
		for(int i=0;i<sqlCharacters.size();i++) {
			SqlCharacter character=sqlCharacters.get(i);
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
				sqlCharacters.get(0).getChar()==cLeft &&
				sqlCharacters.get(sqlCharacters.size()-1).getChar()==cRight
				) {
			sqlCharacters.remove(sqlCharacters.size()-1);
			sqlCharacters.remove(0);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Token clone() throws CloneNotSupportedException {
		return new Token((ArrayList<SqlCharacter>)sqlCharacters.clone());
	}

	public Token cloneWithoutDelimiters() throws CloneNotSupportedException {
		Token token=(Token)this.clone();
		token.removeEnclosing('[',']');
		token.removeEnclosing('"');
		return token;
	}

	private void trim() {
		while(sqlCharacters.size()>0 && sqlCharacters.get(0).isWhiteSpace()) {
			sqlCharacters.remove(0);
		}
		while(sqlCharacters.size()>0 && sqlCharacters.get(sqlCharacters.size()-1).isWhiteSpace()) {
			sqlCharacters.remove(sqlCharacters.size()-1);
		}
	}

	public Token setCategory(Category category) {
		super.setCategory(category);
		return this;
	}

	public Token append(Token token) {
		sqlCharacters.addAll(token.getCharacters());
		return this;
	}

	public Token append(SqlCharacter character) {
		super.append(character);
		return this;
	}

	public boolean fixMySql(){
		boolean isMySql=false;
		isMySql=true;
		boolean openBracket=false;
		for(int i=0;i<sqlCharacters.size();i++) {
			SqlCharacter sqlCharacter=sqlCharacters.get(i);
			if(sqlCharacter.getChar()=='`') {
				if(openBracket) {
					sqlCharacter.setChar(']');
				}else {
					sqlCharacter.setChar('[');
				}
				openBracket=!openBracket;
			}
		}
		CharacterLocation searchStringLocation=replaceIfExists("auto_increment","identity");
		if(searchStringLocation!=null) {
			logger.warn(String.format("It smells like MySql! Replacing 'auto_increment' with 'identity'! %s", searchStringLocation) );
		}

		return isMySql;
	}

	private CharacterLocation replaceIfExists(String search,String replace) {
		int p=toString().toLowerCase().indexOf(search);
		if(p==-1) {
			return null;
		}
		CharacterLocation searchStringLocation=sqlCharacters.get(0).getLocation();
		boolean isMySql=p!=-1;
		if(isMySql) {
			isMySql=true;
			Category category=sqlCharacters.get(0).getCategory();
			for(int i=0;i<search.length();i++) {
				sqlCharacters.get(p).setCategory(Category.IGNORED);
				sqlCharacters.remove(p);
			}
			SqlString sqlString=SqlString.createSqlStringFromString(replace, category);
			sqlCharacters.addAll(p, sqlString.getCharacters());
		}
		return searchStringLocation;
	}

	public Token toUpperCase() {
		ArrayList<SqlCharacter> charactersUppercase=new ArrayList<>();
		for(SqlCharacter sqlCharacter:sqlCharacters) {
			SqlCharacter sqlCharacterUppercase=new SqlCharacter(Character.toUpperCase(sqlCharacter.getChar()),sqlCharacter.getLocation(),sqlCharacter.getCategory());
			sqlCharacterUppercase.setBackgroundColor(sqlCharacter.getBackgroundColor());
			charactersUppercase.add(sqlCharacterUppercase);
		}
		return new Token(charactersUppercase);
	}

	public boolean equalsIgnoreCase(String string) {
		if(sqlCharacters.size()!=string.length()) {
			return false;
		}
		String stringLowerCase=string.toLowerCase();
		for(int i=0;i<sqlCharacters.size();i++) {
			if(Character.toLowerCase(sqlCharacters.get(i).getChar())!=stringLowerCase.charAt(i)) {
				return false;
			}
		}
		return true;
	}

}
