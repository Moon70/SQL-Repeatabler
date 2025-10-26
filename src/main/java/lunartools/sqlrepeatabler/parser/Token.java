package lunartools.sqlrepeatabler.parser;

import java.util.ArrayList;

public class Token {
	private ArrayList<SqlCharacter> charactersOfToken;

	public Token(ArrayList<SqlCharacter> charactersOfToken) {
		this.charactersOfToken=charactersOfToken;
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
		if(
				charactersOfToken.get(0).getChar()==c &&
				charactersOfToken.get(charactersOfToken.size()-1).getChar()==c
				) {
			charactersOfToken.remove(charactersOfToken.size()-1);
			charactersOfToken.remove(0);
		}
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

	@Override
	public Token clone() throws CloneNotSupportedException {
		return new Token((ArrayList<SqlCharacter>)charactersOfToken.clone());
	}

	public void trim() {
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

	public void categorize(Category category) {
		for(SqlCharacter sqlCharacter:charactersOfToken) {
			sqlCharacter.setCategory(category);
		}
		
	}
}
