package lunartools.sqlrepeatabler.parser;

import java.util.ArrayList;

import lunartools.sqlrepeatabler.settings.Settings;

public class HtmlRenderer {
	private boolean isBackgroundColorEnabled;

	public String render(SqlBlock sqlBlock) {
		return render(sqlBlock.getSqlStrings());
	}

	public String render(ArrayList<SqlString> sqlStrings) {
		StringBuilder sbHtml=new StringBuilder();
		for(SqlString sqlString:sqlStrings) {
			render(sbHtml,sqlString);
			sbHtml.append("<br>");
		}
		return sbHtml.toString();
	}

	private void render(StringBuilder sbHtml,SqlString sqlString) {
		isBackgroundColorEnabled=Settings.getInstance().isBackgroundColorEnabled();
		ArrayList<SqlCharacter> sqlCharacters=sqlString.getCharacters();
		StringBuilder sbFragement=new StringBuilder();
		Category category=Category.UNCATEGORIZED;
		String backgroundColor=null;
		for(int i=0;i<sqlCharacters.size();i++) {
			SqlCharacter sqlCharacter=sqlCharacters.get(i);
			if(
					category!=sqlCharacter.getCategory()
					|| (sqlCharacter.getBackgroundColor() !=null && !sqlCharacter.getBackgroundColor().equals(backgroundColor))
					) {
				appendHtmlFragment(category,sbHtml,sbFragement,backgroundColor);
				sbFragement=new StringBuilder();
				backgroundColor=sqlCharacter.getBackgroundColor();
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
		appendHtmlFragment(category,sbHtml,sbFragement,backgroundColor);
	}

	private void appendHtmlFragment(Category category, StringBuilder sbHtml, StringBuilder sbFragement,String back) {
		if(isBackgroundColorEnabled && back!=null) {
			sbHtml.append(String.format("<span class=\"%s\" style='background-color: %s;' >",category.toString(),back));
		}else {
			sbHtml.append(String.format("<span class=\"%s\" >",category.toString()));
		}
		sbHtml.append(sbFragement);
		sbHtml.append("</span>");
	}

}
