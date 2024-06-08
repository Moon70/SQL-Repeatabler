package lunartools.sqlrepeatabler.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;

public class LogPanelEditorpane extends JPanel implements Observer{
	private JEditorPane logEditorPane;
	private final String htmlIntro;
	private final String htmlOutro;
	private StringBuffer sbHtmlLines;
	private JScrollPane scrollPane;

	public LogPanelEditorpane(SqlRepeatablerModel model) {
		Font font=new Font("Courier New", Font.PLAIN,12);
		setPreferredSize(new Dimension(1400, 280));
		logEditorPane=new JEditorPane();
		logEditorPane.setEditable(false);
		logEditorPane.setFont(font);
		logEditorPane.setContentType("text/html");
		logEditorPane.setPreferredSize(new Dimension(1400, 280));

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

		sb.append(".debug {");
		sb.append("color: #666666;");
		sb.append("}");

		sb.append(".info {");
		sb.append("color: black;");
		sb.append("}");

		sb.append(".warn {");
		sb.append("color: blue;");
		sb.append("font-weight: bold;");
		sb.append("}");

		sb.append(".error {");
		sb.append("color: red;");
		sb.append("font-weight: bold;");
		sb.append("}");

		sb.append(".stacktrace {");
		sb.append("color: black;");
		sb.append("}");

		sb.append(".stacktraceIndent {");
		sb.append("color: black;");
		sb.append("margin-left: 24px;");
		sb.append("}");

		sb.append("</style>");
		sb.append("</head>");
		sb.append("<body>");
		htmlIntro=sb.toString();

		sb=new StringBuffer();
		sb.append("</body>");
		sb.append("</html>");
		htmlOutro=sb.toString();

		sbHtmlLines=new StringBuffer();

		model.addObserver(this);
	}

	@Override
	public void update(Observable o, Object object) {
		if(object instanceof ILoggingEvent) {
			ILoggingEvent loggingEvent=(ILoggingEvent)object;
			switch(loggingEvent.getLevel().levelInt) {
			case Level.DEBUG_INT:
				sbHtmlLines.append("<div class=\"debug\">");
				sbHtmlLines.append(loggingEvent.getMessage());
				sbHtmlLines.append("</div>");
				break;
			case Level.INFO_INT:
				sbHtmlLines.append("<div class=\"info\">");
				sbHtmlLines.append(loggingEvent.getMessage());
				sbHtmlLines.append("</div>");
				break;
			case Level.WARN_INT:
				sbHtmlLines.append("<div class=\"warn\">");
				sbHtmlLines.append(loggingEvent.getMessage());
				sbHtmlLines.append("</div>");
				break;
			case Level.ERROR_INT:
				sbHtmlLines.append("<div class=\"error\">");
				sbHtmlLines.append(loggingEvent.getMessage());
				sbHtmlLines.append("</div>");
				IThrowableProxy throwableProxy=loggingEvent.getThrowableProxy();
				String[] sa=ThrowableProxyUtil.asString(throwableProxy).split("\t");
				sbHtmlLines.append("<div class=\"stacktrace\">");
				sbHtmlLines.append(sa[0]);
				sbHtmlLines.append("</div>");
				for(int i=1;i<sa.length;i++) {
					sbHtmlLines.append("<div class=\"stacktraceIndent\">");
					sbHtmlLines.append(sa[i]);
					sbHtmlLines.append("</div>");
				}
				break;
			default:
				sbHtmlLines.append("<div class=\"debug\">");
				sbHtmlLines.append(loggingEvent.getMessage());
				sbHtmlLines.append("</div>");
				break;
			}

			StringBuffer sb=new StringBuffer();
			sb.append(htmlIntro);
			sb.append(sbHtmlLines);
			sb.append(htmlOutro);
			logEditorPane.setText(sb.toString());

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					scrollPane.revalidate();
					JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
					verticalBar.setValue(verticalBar.getMaximum());
				}
			});
		}
	}

}
