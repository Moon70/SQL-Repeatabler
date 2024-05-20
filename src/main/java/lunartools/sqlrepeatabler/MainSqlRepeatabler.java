package lunartools.sqlrepeatabler;

import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainSqlRepeatabler {
	private static Logger logger = LoggerFactory.getLogger(MainSqlRepeatabler.class);

	static {
		org.apache.log4j.ConsoleAppender console = new org.apache.log4j.ConsoleAppender();
		console.setLayout(new org.apache.log4j.PatternLayout("[%-5p] %c - %m%n")); 
		console.setThreshold(org.apache.log4j.Level.ALL);
		console.activateOptions();
		org.apache.log4j.Logger.getRootLogger().addAppender(console);
		org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.ALL);
	}

	public static void main(String[] args) {
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			new SqlRepeatablerController().openGUI();
		}catch(Throwable e){
			if(logger.isErrorEnabled()) {
				logger.error("Unexpected error",e);
			}else {
				e.printStackTrace();
			}
		}
	}

}
