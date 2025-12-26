package lunartools.sqlrepeatabler.main;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JEditorPane;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;

public class LogEditorPane extends JEditorPane{
	private final String htmlIntro;
	private final String htmlOutro;
	private StringBuilder sbHtmlLines=new StringBuilder();

	public LogEditorPane(SqlRepeatablerModel model) {
		setBackground(Color.black);
		setEditable(false);
		setContentType("text/html");
		setMargin(new Insets(8, 16, 8, 16));

		StringBuilder sb=new StringBuilder();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<meta charset=\"UTF-8\">");
		sb.append("<style>");
		sb.append("body {font-family: \"Courier New\", Courier, monospace;}");
		sb.append(".debug {color: #aaaaaa;}");
		sb.append(".info {color: #eeeeee;font-weight: bold;}");
		sb.append(".warn {color: #ff961e;font-weight: bold;}");
		sb.append(".error {color: #ff1122;font-weight: bold;}");
		sb.append(".stacktrace {color: #dddd22;font-weight: bold;}");
		sb.append(".stacktraceIndent {color: #dddd22;margin-left: 24px;}");
		sb.append("</style>");
		sb.append("</head>");
		sb.append("<body>");
		htmlIntro=sb.toString();

		sb=new StringBuilder();
		sb.append("</body>");
		sb.append("</html>");
		htmlOutro=sb.toString();
	}

	public void consumeLogEvent(ILoggingEvent loggingEvent) {
		switch(loggingEvent.getLevel().levelInt) {
		case Level.DEBUG_INT:
			sbHtmlLines.append("<div class=\"debug\">");
			sbHtmlLines.append(loggingEvent.getFormattedMessage());
			sbHtmlLines.append("</div>");
			break;
		case Level.INFO_INT:
			sbHtmlLines.append("<div class=\"info\">");
			sbHtmlLines.append(loggingEvent.getFormattedMessage());
			sbHtmlLines.append("</div>");
			break;
		case Level.WARN_INT:
			sbHtmlLines.append("<div class=\"warn\">");
			sbHtmlLines.append(loggingEvent.getFormattedMessage());
			sbHtmlLines.append("</div>");
			break;
		case Level.ERROR_INT:
			sbHtmlLines.append("<div class=\"error\">");
			sbHtmlLines.append(loggingEvent.getFormattedMessage());
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
			sbHtmlLines.append(loggingEvent.getFormattedMessage());
			sbHtmlLines.append("</div>");
			break;
		}

		StringBuilder sb=new StringBuilder();
		sb.append(htmlIntro);
		sb.append(sbHtmlLines);
		sb.append(htmlOutro);
		setText(sb.toString());
		setCaretPosition(getDocument().getLength());
	}
}
