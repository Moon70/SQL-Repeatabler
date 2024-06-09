package lunartools.sqlrepeatabler;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class TextareaAppender extends AppenderBase<ILoggingEvent> {
	private final SqlRepeatablerModel model;

	public TextareaAppender(SqlRepeatablerModel model) {
		this.model=model;
	}

	@Override
	protected void append(ILoggingEvent eventObject) {
		model.fireLogEvent(eventObject);
	}

}
