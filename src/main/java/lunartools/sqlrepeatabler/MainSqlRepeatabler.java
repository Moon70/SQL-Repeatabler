package lunartools.sqlrepeatabler;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.filter.ThresholdFilter;
import lunartools.sqlrepeatabler.common.TextAreaAppender;
import lunartools.sqlrepeatabler.gui.SqlRepeatablerView;

public class MainSqlRepeatabler {
	private static Logger logger = LoggerFactory.getLogger(MainSqlRepeatabler.class);

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				JTextArea jTextAreaLog=new JTextArea(14,220);
				configureLogger(jTextAreaLog);
				SqlRepeatablerModel model=new SqlRepeatablerModel();
				SqlRepeatablerView view=new SqlRepeatablerView(model,jTextAreaLog);
				new SqlRepeatablerController(model,view);
				view.setVisible(true);
				logger.info(SqlRepeatablerModel.PROGRAMNAME+" "+SqlRepeatablerModel.getProgramVersion());
//model.addSqlInputFile(new File("c:/temp/test3.sql"));
			}catch(Throwable e){
				if(logger.isErrorEnabled()) {
					logger.error("Unexpected error",e);
				}else {
					e.printStackTrace();
				}
			}
		});
	}

	private static void configureLogger(JTextArea logTextArea) {
		ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		TextAreaAppender textAreaAppender = new TextAreaAppender();
		textAreaAppender.setJTextArea(logTextArea);

		ThresholdFilter thresholdFilter = new ThresholdFilter();
		thresholdFilter.setLevel(Level.INFO.toString());
		thresholdFilter.start();

		textAreaAppender.addFilter(thresholdFilter);
		textAreaAppender.start();

		rootLogger.addAppender(textAreaAppender);
	}

}
