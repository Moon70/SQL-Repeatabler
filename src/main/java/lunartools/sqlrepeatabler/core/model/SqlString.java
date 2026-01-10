package lunartools.sqlrepeatabler.core.model;

import java.util.ArrayList;

import lunartools.sqlrepeatabler.common.service.BackgroundColorProvider;

public class SqlString {
	public static final SqlString EMPTY_LINE=SqlString.createSqlStringFromString("",Category.UNCATEGORIZED);
	ArrayList<SqlCharacter> sqlCharacters=new ArrayList<>();

	public static SqlString createSqlStringFromString(String string, Category category, Token... tokens){
		String[] stringFragments;
		if(category==Category.COMMENT) {
			stringFragments=new String[1];
			stringFragments[0]=string;
		}else {
			stringFragments=string.split("%s",-1);
		}
		if(stringFragments.length!=tokens.length+1) {
			throw new IllegalArgumentException(String.format("String fragment count (%d) does not match token count (%d)",stringFragments.length,tokens.length));
		}

		ArrayList<SqlCharacter> characters=new ArrayList<>();
		for(int i=0;i<stringFragments.length;i++) {
			String s=stringFragments[i];
			for(int k=0;k<s.length();k++) {
				SqlCharacter sqlCharacter=new SqlCharacter(s.charAt(k));
				sqlCharacter.setCategory(category);
				characters.add(sqlCharacter);
			}
			if(i<stringFragments.length-1) {
				characters.addAll(tokens[i].getCharacters());
			}

		}
		return  new SqlString(characters);
	}

	public SqlString() {}

	public SqlString(ArrayList<SqlCharacter> sqlCharacters) {
		this.sqlCharacters=sqlCharacters;
	}

	public SqlCharacter getFirstCharacter() {
		if(sqlCharacters.size()==0) {
			return null;
		}
		return sqlCharacters.get(0);
	}

	public CharacterLocation getLocation() {
		if(sqlCharacters.size()==0) {
			return null;
		}
		return sqlCharacters.get(0).getLocation();
	}

	public ArrayList<SqlCharacter> getCharacters(){
		return sqlCharacters;
	}

	public boolean endsWithSemicolonIgnoreWhiteSpace() {
		for(int i=sqlCharacters.size()-1;i>=0;i--) {
			SqlCharacter sqlCharacter=sqlCharacters.get(i);
			if(sqlCharacter.isSemicolon()) {
				return true;
			}
			if(sqlCharacter.isWhiteSpace()) {
				continue;
			}
			break;
		}
		return false;
	}

	public boolean startsWithIgnoreCase(String prefix) {
		if(prefix.length()>sqlCharacters.size()) {
			return false;
		}
		String prefixLowercase=prefix.toLowerCase();
		for(int i=0;i<prefix.length();i++) {
			if(prefixLowercase.charAt(i)!=Character.toLowerCase(sqlCharacters.get(i).getChar())) {
				return false;
			}
		}
		return true;
	}

	public SqlString setCategory(Category category) {
		for(SqlCharacter sqlCharacter:sqlCharacters) {
			sqlCharacter.setCategory(category);
		}
		return this;
	}

	public void append(ArrayList<SqlCharacter> characters) {
		sqlCharacters.addAll(characters);
	}

	public SqlString append(SqlCharacter sqlCharacter) {
		sqlCharacters.add(sqlCharacter);
		return this;
	}

	public void append(SqlString sqlString) {
		sqlCharacters.addAll(sqlString.getCharacters());
	}

	public void setBackgroundColor(String backgroundColor) {
		for(SqlCharacter sqlCharacter:sqlCharacters) {
			sqlCharacter.setBackgroundColor(backgroundColor);
		}
	}

    public void markWarn() {
        setCategory(Category.WARN);
        setBackgroundColor(BackgroundColorProvider.WARN);
    }

    public void markError() {
        setCategory(Category.ERROR);
        setBackgroundColor(BackgroundColorProvider.ERROR);
    }

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<sqlCharacters.size();i++) {
			sb.append(sqlCharacters.get(i).getChar());
		}
		return sb.toString();
	}
}
