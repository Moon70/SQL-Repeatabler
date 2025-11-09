package lunartools.sqlrepeatabler.parser;

import java.util.ArrayList;

public class HtmlRenderer {

	public String render(SqlBlock sqlBlock) {
		return render(sqlBlock.getSqlStrings());
	}

	public String render(ArrayList<SqlString> sqlStrings) {
		StringBuilder sbHtml=new StringBuilder();
		for(SqlString sqlString:sqlStrings) {
			//sbHtml.append(sqlString.toHtml());
			render(sbHtml,sqlString);
			sbHtml.append("<br>");
		}
		return sbHtml.toString();
	}

	private void render(StringBuilder sbHtml,SqlString sqlString) {
		ArrayList<SqlCharacter> sqlCharacters=sqlString.getCharacters();
		StringBuilder sbFragement=new StringBuilder();
		Category category=Category.UNCATEGORIZED;
		for(int i=0;i<sqlCharacters.size();i++) {
			SqlCharacter sqlCharacter=sqlCharacters.get(i);
			if(category!=sqlCharacter.getCategory()) {
				appendHtmlFragment(category,sbHtml,sbFragement);
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
		appendHtmlFragment(category,sbHtml,sbFragement);
	}

	private void appendHtmlFragment(Category category, StringBuilder sbHtml, StringBuilder sbFragement) {
		sbHtml.append("<span class=\""+category.toString()+"\" >");
		sbHtml.append(sbFragement);
		sbHtml.append("</span>");
	}

}
