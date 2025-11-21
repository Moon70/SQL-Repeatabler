package lunartools.sqlrepeatabler.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.BackgroundColorProvider;

public class StatementTokenizer {
    private static Logger logger = LoggerFactory.getLogger(StatementTokenizer.class);
	private ArrayList<SqlCharacter> charactersOfStatement;
	
	public StatementTokenizer(ArrayList<SqlCharacter> charactersOfStatement) {
		this.charactersOfStatement=charactersOfStatement;
	}
	
	private static Token consumeNextToken(ArrayList<SqlCharacter> charactersOfStatement, char tokenDelimiter) throws Exception{
		boolean doubleQuoteOpen=false;
		boolean singleQuoteOpen=false;
		int parenthesisOpen=0;
		
		ArrayList<SqlCharacter> tokenCharacters=new ArrayList<>();
		
		while(charactersOfStatement.size()>0) {
			SqlCharacter character=charactersOfStatement.get(0);
			if(character.getChar()=='"') {
				doubleQuoteOpen=!doubleQuoteOpen;
			}else if(character.getChar()=='\'') {
				singleQuoteOpen=!singleQuoteOpen;
			}else if(singleQuoteOpen||doubleQuoteOpen) {
				//characters inside quotes or double quotes
			}else if(character.getChar()=='(') {
				parenthesisOpen++;
			}else if(character.getChar()==')') {
				parenthesisOpen--;
			}else if(parenthesisOpen!=0) {
				//characters inside parenthesis
			}else if(character.isSemicolon()) {
				return new Token(tokenCharacters);
			}else if(character.getChar()==tokenDelimiter || character.getChar()==',') {
				charactersOfStatement.remove(0);
				stripWhiteSpaceLeft(charactersOfStatement);
				return new Token(tokenCharacters);
			}
			tokenCharacters.add(character);
			charactersOfStatement.remove(0);
		}
		logger.warn("Reached end of file while scanning for statement terminating semicolon. Treating eof as semicolon!");
		return new Token(tokenCharacters);
	}
	
	public void stripWhiteSpaceLeft() {
		while(hasNext() && charactersOfStatement.get(0).isWhiteSpace()) {
			charactersOfStatement.remove(0);
		}
	}
	
	public void stripWhiteSpaceRight() {
		while(charactersOfStatement.get(charactersOfStatement.size()-1).isWhiteSpace()) {
			charactersOfStatement.remove(charactersOfStatement.size()-1);
		}
	}
	
	public int length() {
		return charactersOfStatement.size();
	}
	
	private static void stripWhiteSpaceLeft(ArrayList<SqlCharacter> characters) {
		while(characters.get(0).isWhiteSpace()) {
			characters.remove(0);
		}
	}
	
	public void stripUntil(char c) {
		while(charactersOfStatement.size()>0 && charAt(0).getChar()!=c) {
			charactersOfStatement.remove(0);
		}
	}
	
	public Token nextToken() throws Exception {
		if(charactersOfStatement.size()==0 || charactersOfStatement.get(0).isSemicolon()) {
			return null;
		}
		return consumeNextToken(charactersOfStatement,' ');
	}

	public Token nextToken(String text) throws Exception {
		if(charactersOfStatement.size()==0 || charactersOfStatement.get(0).isSemicolon()) {
			return null;
		}
		stripWhiteSpaceLeft();
		ArrayList<SqlCharacter> tokenCharacters=new ArrayList<>();
		SqlCharacter sqlCharacter=getFirstCharacter();
		for(int i=0;i<text.length();i++) {
			if(compareCharIgnoreCase(text.charAt(i),getFirstCharacter().getChar())) {
				tokenCharacters.add(getFirstCharacter());
				deleteCharAt(0);
			}else {
				throw new SqlParserException(String.format("Expected '%s'", text),sqlCharacter.getLocation());
			}
		}
		return new Token(tokenCharacters);
	}
	
	private boolean compareCharIgnoreCase(char c1,char c2) {
		return Character.toLowerCase(c1)==Character.toLowerCase(c2);
	}

	public Token nextTokenUntil(char c) throws Exception {
		if(charactersOfStatement.size()==0 || charactersOfStatement.get(0).isSemicolon()) {
			return null;
		}
		return consumeNextToken(charactersOfStatement,c);
	}
	
	public boolean hasNext() {
		return charactersOfStatement.size()>0 && !charAt(0).isSemicolon();
	}
	
	public SqlCharacter charAt(int i) {
		return charactersOfStatement.get(i);
	}
	
	public Token toToken() {
		return new Token(charactersOfStatement);
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for(SqlCharacter sqlCharacter:charactersOfStatement) {
			sb.append(sqlCharacter.getChar());
		}
		return sb.toString();
	}

	public void deleteCharAt(int i) {
		charactersOfStatement.remove(i);
	}

	public Token nextToken(char left, char right) throws Exception {
		int openParenthesis=0;
		stripWhiteSpaceLeft();
		if(charAt(0).getChar()!=left) {
			SqlCharacter character=charAt(0);
			throw new SqlParserException(String.format("Error parsing tokens in parenthesis! Expected: >(<. Found: >%s<",character.getChar()),character.getLocation());
		}
		int index=1;
		while(true) {
			SqlCharacter character=charAt(index);
			if(character.getChar()==')'){
				if(openParenthesis==0) {
					break;
				}else{
					openParenthesis--;
				}
			}else if(character.getChar()=='(') {
				openParenthesis++;
			}
			index++;
		}
		
		List<SqlCharacter> list=charactersOfStatement.subList(0, index+1);
		ArrayList<SqlCharacter> arrayList=new ArrayList<>();
		arrayList.addAll(list);
		Token token=new Token(arrayList);
		for(int i=0;i<index+1;i++) {
			charactersOfStatement.remove(0);
		}
		stripWhiteSpaceLeft();
		return token;
	}

	public boolean consumePrefixIgnoreCaseAndSpace(String prefix) {
		if(startsWithIgnoreCase(prefix)) {
			for(int i=0;i<prefix.length();i++) {
				charactersOfStatement.remove(0);
			}
			stripWhiteSpaceLeft();
			return true;
		}else {
			return false;
		}
	}

	public boolean consumeCommandIgnoreCaseAndSpace(String command) {
		if(startsWithIgnoreCase(command)) {
			for(int i=0;i<command.length();i++) {
				charactersOfStatement.get(0).setCategory(Category.COMMAND);
				charactersOfStatement.remove(0);
			}
			stripWhiteSpaceLeft();
			return true;
		}else {
			return false;
		}
	}
	
	public boolean startsWithIgnoreCase(String s) {
		s=s.toLowerCase();
		for(int i=0;i<s.length();i++) {
			if(Character.toLowerCase(charactersOfStatement.get(i).getChar())!=s.charAt(i)) {
				return false;
			}
		}
		return true;
	}

    public SqlCharacter getFirstCharacter() {
        if(charactersOfStatement.size()==0) {
            return null;
        }
        return charactersOfStatement.get(0);
    }

    public CharacterLocation getLocation() {
        if(charactersOfStatement.size()==0) {
            return null;
        }
        return charactersOfStatement.get(0).getLocation();
    	
    }
    
	public void setCategory(Category category) {
		for(SqlCharacter sqlCharacter:charactersOfStatement) {
			sqlCharacter.setCategory(category);
		}
	}

	public void setBackgroundColor(String backgroundColor) {
		if(backgroundColor==null) {
			backgroundColor=BackgroundColorProvider.getInstance().getNextPrimaryColor();
		}
		for(SqlCharacter sqlCharacter: charactersOfStatement) {
			sqlCharacter.setBackgroundColor(backgroundColor);
		}
	}
	
}
