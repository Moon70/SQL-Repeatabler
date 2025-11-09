package lunartools.sqlrepeatabler.common;

import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class SwingBufferingLogBackAppender extends AppenderBase<ILoggingEvent> {
	private final ArrayList<ILoggingEvent> buffer=new ArrayList<>();
	private volatile Consumer<ILoggingEvent> outputConsumer;

	@Override
	protected synchronized void append(ILoggingEvent loggingEvent) {

		if(outputConsumer!=null) {
			SwingUtilities.invokeLater(() -> outputConsumer.accept(loggingEvent));
		}else {
			buffer.add(loggingEvent);
		}
	}

	public synchronized void attachConsumer(Consumer<ILoggingEvent> consumer) {
		this.outputConsumer=consumer;
		for (ILoggingEvent event : buffer) {
			SwingUtilities.invokeLater(() -> consumer.accept(event));
		}
		buffer.clear();
	}
}
