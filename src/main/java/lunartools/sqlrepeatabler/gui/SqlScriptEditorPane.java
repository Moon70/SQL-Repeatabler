package lunartools.sqlrepeatabler.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.parser.Category;

public class SqlScriptEditorPane extends JEditorPane{
	private final String htmlIntro;
	private final String htmlOutro;

	private int width=750;
	private int height=500+160;

	public SqlScriptEditorPane(SqlRepeatablerModel model) {
		Font font=new Font("Courier New", Font.PLAIN,12);
		setPreferredSize(new Dimension(width, height));
		setEditable(false);
		setFont(font);
		setContentType("text/html");
		setMargin(new Insets(10, 20, 10, 20));
		
//		scrollPane=new JScrollPane(sqlScriptEditorPane);
//		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
//		scrollPane.setPreferredSize(new Dimension(width, height));
//		add(scrollPane);

		StringBuffer sb=new StringBuffer();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<meta charset=\"UTF-8\">");
		sb.append("<style>");
		sb.append("body {");
		sb.append("font-family: \"Courier New\", Courier, monospace;");
		sb.append("}");

		for(Category category:Category.values()) {
			sb.append(category.getCss());
		}

		sb.append("</style>");
		sb.append("</head>");
		sb.append("<body>");
		htmlIntro=sb.toString();

		sb=new StringBuffer();
		sb.append("</body>");
		sb.append("</html>");
		htmlOutro=sb.toString();

		//model.addChangeListener(this::updateModelChanges);
	}

	@Override
	public void setText(String s) {
		StringBuilder sb=new StringBuilder();
		sb.append(htmlIntro);
		sb.append(s);
		sb.append(htmlOutro);
		super.setText(sb.toString());
		repaint();
	}

}
