package lunartools.sqlrepeatabler.gui;

import java.awt.Insets;

import javax.swing.JEditorPane;

import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.parser.Category;

public class SqlEditorPane extends JEditorPane{
	private final String htmlIntro;
	private final String htmlOutro;

	public SqlEditorPane(SqlRepeatablerModel model, int sqlFileIndex, boolean isOutputPane) {
		setEditable(false);
		setContentType("text/html");
		setMargin(new Insets(8, 16, 8, 16));

		StringBuilder sb=new StringBuilder();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<meta charset=\"UTF-8\">");
		sb.append("<style>");
		sb.append("body {font-family: \"Courier New\", Courier, monospace;}");
		for(Category category:Category.values()) {
			sb.append(category.getCss());
		}
		sb.append("</style>");
		sb.append("</head>");
		sb.append("<body>");
		htmlIntro=sb.toString();

		sb=new StringBuilder();
		sb.append("</body>");
		sb.append("</html>");
		htmlOutro=sb.toString();
	}

	@Override
	public void setText(String text) {
		StringBuilder sb=new StringBuilder();
		sb.append(htmlIntro);
		sb.append(text);
		sb.append(htmlOutro);
		super.setText(sb.toString());
	}

}
