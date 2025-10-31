package lunartools.sqlrepeatabler.parser;

import java.util.ArrayList;

public class SqlString {
    public static final SqlString EMPTY_LINE=SqlString.createSqlStringFromString("",Category.UNCATEGORIZED);
	private ArrayList<SqlCharacter> sqlCharacters=new ArrayList<>();

    public static SqlString createSqlStringFromString(String string, Category category, Token... tokens){
        String[] stringFragments;
        if(category==Category.COMMENT) {
            stringFragments=new String[1];
            stringFragments[0]=string;
        }else {
            stringFragments=string.split("%s",-1);
        }
        if(stringFragments.length!=tokens.length+1) {
            throw new IllegalArgumentException("String fragment count ("+stringFragments.length+") does not match token count ("+tokens.length+")");
        }
        
        ArrayList<SqlCharacter> characters=new ArrayList<>();
        for(int i=0;i<stringFragments.length;i++) {
            String s=stringFragments[i];
            for(int k=0;k<s.length();k++) {
                SqlCharacter sqlCharacter=new SqlCharacter(s.charAt(k),-1,-1,-1);
                sqlCharacter.setCategory(category);
                characters.add(sqlCharacter);
            }
            if(i<stringFragments.length-1) {
                characters.addAll(tokens[i].getCharacters());
            }
            
        }
        return  new SqlString(characters);
    }
    
	public SqlString(ArrayList<SqlCharacter> sqlCharacters) {
		this.sqlCharacters=sqlCharacters;
	}
	
	public SqlCharacter getFirstCharacter() {
		if(sqlCharacters.size()==0) {
			return null;
		}
		return sqlCharacters.get(0);
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

	public StringBuilder toHtml() {
		StringBuilder sbResult=new StringBuilder();
		StringBuilder sbFragement=new StringBuilder();
		Category category=Category.UNCATEGORIZED;
		for(int i=0;i<sqlCharacters.size();i++) {
			SqlCharacter sqlCharacter=sqlCharacters.get(i);
			if(category!=sqlCharacter.getCategory()) {
				appendHtmlFragment(category,sbResult,sbFragement);
				sbFragement=new StringBuilder();
				category=sqlCharacter.getCategory();
			}
			char c=sqlCharacter.getChar();
			if(c==' ') {
				sbFragement.append("&nbsp;");
			}else if(c=='\t') {
				sbFragement.append("&nbsp;&nbsp;&nbsp;&nbsp;");
			}else {
				sbFragement.append(c);
			}
		}
		appendHtmlFragment(category,sbResult,sbFragement);
		return sbResult;
	}

	private void appendHtmlFragment(Category category, StringBuilder sbHtml, StringBuilder sbFragement) {
		sbHtml.append("<span class=\""+category.toString()+"\">");
		sbHtml.append(sbFragement);
		sbHtml.append("</span>");
	}
	
	public void setCategory(Category category) {
        for(SqlCharacter sqlCharacter:sqlCharacters) {
            sqlCharacter.setCategory(category);
        }
	}
	
	public void append(ArrayList<SqlCharacter> characters) {
	    sqlCharacters.addAll(characters);
	}
	
	public void append(SqlCharacter sqlCharacter) {
        sqlCharacters.add(sqlCharacter);
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
