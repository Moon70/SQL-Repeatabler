package lunartools.sqlrepeatabler.gui;

import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

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

public class LogPanelTextarea extends JPanel implements Observer{
	private JTextArea logTextarea;
	private JScrollPane scrollPane;

	public LogPanelTextarea(SqlRepeatablerModel model) {
		Font font=new Font("Courier New", Font.PLAIN,12);
		logTextarea=new JTextArea(15,200);
		logTextarea.setEditable(false);
		logTextarea.setFont(font);

		scrollPane=new JScrollPane(logTextarea);
		add(scrollPane);

		model.addObserver(this);
	}

	public void addLog(String s) {
	}

	@Override
	public void update(Observable o, Object object) {
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
