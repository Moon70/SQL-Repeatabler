package lunartools.sqlrepeatabler.gui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.parser.Category;

public class ScriptPanelEditorPanel extends JPanel{
	private JEditorPane logEditorPane;
	private final String htmlIntro;
	private final String htmlOutro;
	private StringBuffer sbHtmlLines;
	private JScrollPane scrollPane;

	private int width=750;
	private int height=500;
	
	public ScriptPanelEditorPanel(SqlRepeatablerModel model) {
		Font font=new Font("Courier New", Font.PLAIN,12);
		setPreferredSize(new Dimension(width, height));
		logEditorPane=new JEditorPane();
		logEditorPane.setEditable(false);
		logEditorPane.setFont(font);
		logEditorPane.setContentType("text/html");
		logEditorPane.setPreferredSize(new Dimension(width, height));

		scrollPane=new JScrollPane(logEditorPane);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane);

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

		sbHtmlLines=new StringBuffer();

		//model.addChangeListener(this::updateModelChanges);
	}

	public void setText(String s) {
		StringBuilder sb=new StringBuilder();
		sb.append(htmlIntro);
		sb.append(s);
		sb.append(htmlOutro);
		logEditorPane.setText(sb.toString());
		repaint();
	}
	
}
