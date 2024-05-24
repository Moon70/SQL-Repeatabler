package lunartools.sqlrepeatabler;

import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainSqlRepeatabler {
	private static Logger logger = LoggerFactory.getLogger(MainSqlRepeatabler.class);

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
