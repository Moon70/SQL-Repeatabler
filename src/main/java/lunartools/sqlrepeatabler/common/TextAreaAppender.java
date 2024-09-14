package lunartools.sqlrepeatabler.common;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.AppenderBase;

public class TextAreaAppender extends AppenderBase<ch.qos.logback.classic.spi.ILoggingEvent> {
	private JTextArea jTextArea;

	public void setJTextArea(JTextArea jTextArea) {
		if(jTextArea==null) {
			throw new IllegalArgumentException("JTextArea must not be null");
		}
		this.jTextArea=jTextArea;
	}

	@Override
	protected void append(ILoggingEvent loggingEvent) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				jTextArea.append(loggingEvent.getFormattedMessage());
				jTextArea.append(System.lineSeparator());

				IThrowableProxy throwableProxy = loggingEvent.getThrowableProxy();
				if (throwableProxy != null && throwableProxy instanceof ThrowableProxy) {
					Throwable throwable = ((ThrowableProxy) throwableProxy).getThrowable();
					jTextArea.append(getStackTraceAsString(throwable));
					jTextArea.append(System.lineSeparator());
				}
			}  
		});
	}

	private String getStackTraceAsString(Throwable throwable) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		throwable.printStackTrace(printWriter);
		return stringWriter.toString();
	}

}
