package lunartools.sqlrepeatabler.parser;

import java.util.ArrayList;

public class SqlScriptLine {
	private ArrayList<SqlCharacter> sqlCharacters=new ArrayList<>();

	public SqlScriptLine(ArrayList<SqlCharacter> sqlCharacters) {
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
	
	public boolean endsWithSemicolon() {
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
//		switch(category) {
//		case UNCATEGORIZED:
//			sbHtml.append("<span class=\""+category.toString()+"\">");
//			sbHtml.append(sbFragement);
//			sbHtml.append("</span>");
//			break;
//		case STATEMENT:
//			sbHtml.append("<span class=\""+category.toString()+"\">");
//			sbHtml.append(sbFragement);
//			sbHtml.append("</span>");
//			break;
//		default:
//			sbHtml.append("<span class=\""+category.toString()+"\">");
//			sbHtml.append(sbFragement);
//			sbHtml.append("</span>");
//		}
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
