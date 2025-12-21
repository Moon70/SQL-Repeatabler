package lunartools.sqlrepeatabler.gui;

import java.awt.Insets;

import javax.swing.JEditorPane;

import lunartools.sqlrepeatabler.common.ui.ThemeManager;
import lunartools.sqlrepeatabler.main.SqlRepeatablerModel;

public class SqlEditorPane extends JEditorPane{

	public SqlEditorPane(SqlRepeatablerModel model, int sqlFileIndex, boolean isOutputPane) {
		setEditable(false);
		setContentType("text/html");
		setMargin(new Insets(8, 16, 8, 16));
	}

	@Override
	public void setText(String text) {
		ThemeManager themeManager=ThemeManager.getInstance();
		StringBuilder sb=new StringBuilder();
		sb.append(themeManager.getHtmlIntro());
		sb.append(text);
		sb.append(themeManager.getHtmlOutro());
		super.setText(sb.toString());
		setCaretPosition(0);
	}

}
