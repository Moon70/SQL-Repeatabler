package lunartools.sqlrepeatabler;

import java.io.File;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.ThresholdFilter;
import lunartools.sqlrepeatabler.common.SwingBufferingLogBackAppender;
import lunartools.sqlrepeatabler.gui.SqlRepeatablerView;

public class MainSqlRepeatabler {
	private static Logger logger = LoggerFactory.getLogger(MainSqlRepeatabler.class);

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				SwingBufferingLogBackAppender swingAppender = setupLogger(Level.INFO);
				
				SqlRepeatablerModel model=new SqlRepeatablerModel();
				SqlRepeatablerView view=new SqlRepeatablerView(model);
				new SqlRepeatablerController(model,view,swingAppender);
				view.setVisible(true);
				logger.info(SqlRepeatablerModel.PROGRAMNAME+" "+SqlRepeatablerModel.getProgramVersion());
model.addSqlInputFile(new File("c:/temp/test3.sql"));
			}catch(Throwable e){
				if(logger.isErrorEnabled()) {
					logger.error("Unexpected error",e);
				}else {
					e.printStackTrace();
				}
			}
		});
	}

	public static SwingBufferingLogBackAppender setupLogger(Level level) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        SwingBufferingLogBackAppender swingBufferingAppender = new SwingBufferingLogBackAppender();
        swingBufferingAppender.setContext(loggerContext);
        swingBufferingAppender.setName(SqlRepeatablerModel.PROGRAMNAME);

        ThresholdFilter tresholdFilter = new ThresholdFilter();
        tresholdFilter.setLevel(level.toString());
        tresholdFilter.setContext(loggerContext);
        tresholdFilter.start();

        swingBufferingAppender.addFilter(tresholdFilter);
        swingBufferingAppender.start();

		ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(swingBufferingAppender);

        return swingBufferingAppender;
    }
	
}
