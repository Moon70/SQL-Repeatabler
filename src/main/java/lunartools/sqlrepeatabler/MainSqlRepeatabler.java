package lunartools.sqlrepeatabler;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.ThresholdFilter;
import lunartools.sqlrepeatabler.common.SwingBufferingLogBackAppender;
import lunartools.sqlrepeatabler.gui.SqlRepeatablerView;

/*
 * Copyright (c) 2025 Thomas Mattel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
				logger.info(SqlRepeatablerModel.PROGRAMNAME+" "+SqlRepeatablerModel.getProgramVersion());
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
