package lunartools.sqlrepeatabler;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class TexttareaAppender extends AppenderBase<ILoggingEvent> {
	private final SqlRepeatablerModel model;

	public TexttareaAppender(SqlRepeatablerModel model) {
		this.model=model;
	}

	@Override
	protected void append(ILoggingEvent eventObject) {
		model.fireLogEvent(eventObject);
	}

}
