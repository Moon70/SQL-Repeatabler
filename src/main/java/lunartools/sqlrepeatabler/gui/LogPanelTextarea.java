package lunartools.sqlrepeatabler.gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import lunartools.sqlrepeatabler.SimpleEvents;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;

public class LogPanelTextarea extends JPanel{
	private JTextArea logTextarea;
	private JScrollPane scrollPane;

	public LogPanelTextarea(SqlRepeatablerModel model,JTextArea logTextArea) {
		setLayout(new BorderLayout());
		Font font=new Font("Courier New", Font.PLAIN,12);
		this.logTextarea=logTextArea;
		logTextarea.setEditable(false);
		logTextarea.setFont(font);

		scrollPane=new JScrollPane(logTextarea);
		add(scrollPane);

		model.addChangeListener(this::updateModelChanges);
	}

	public void addLog(String s) {
	}

	public void updateModelChanges(Object object) {
//		if(logger.isTraceEnabled()) {
//			logger.trace("update: "+object);
//		}
		if(object instanceof ILoggingEvent) {
			ILoggingEvent loggingEvent=(ILoggingEvent)object;
			logTextarea.append(loggingEvent.getFormattedMessage());
			logTextarea.append(System.lineSeparator());
			if(loggingEvent.getLevel()==Level.ERROR) {
				IThrowableProxy throwableProxy=loggingEvent.getThrowableProxy();
				logTextarea.append(ThrowableProxyUtil.asString(throwableProxy));
				logTextarea.append(System.lineSeparator());

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						scrollPane.revalidate();
						JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
						verticalBar.setValue(verticalBar.getMaximum());
					}
				});
			}
		}else if(object == SimpleEvents.MODEL_RESET) {
			logTextarea.setText("");
		}
	}

}
