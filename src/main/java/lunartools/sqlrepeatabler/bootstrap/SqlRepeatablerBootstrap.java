package lunartools.sqlrepeatabler.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.ThresholdFilter;
import lunartools.sqlrepeatabler.common.view.ContextMenuView;
import lunartools.sqlrepeatabler.common.view.MenuView;
import lunartools.sqlrepeatabler.core.SqlRepeatablerController;
import lunartools.sqlrepeatabler.core.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.core.SqlRepeatablerView;
import lunartools.sqlrepeatabler.core.controller.FileController;
import lunartools.sqlrepeatabler.core.controller.MenuPresenter;
import lunartools.sqlrepeatabler.core.service.FileService;
import lunartools.sqlrepeatabler.core.ui.action.ActionFactory;
import lunartools.sqlrepeatabler.infrastructure.util.SwingBufferingLogBackAppender;

public class SqlRepeatablerBootstrap {
	private static Logger logger = LoggerFactory.getLogger(SqlRepeatablerBootstrap.class);

	private SqlRepeatablerBootstrap() {}

	public static void start() {
		SwingBufferingLogBackAppender swingAppender = setupLogger(Level.INFO);

		SqlRepeatablerModel model=new SqlRepeatablerModel();
		SqlRepeatablerView view=new SqlRepeatablerView(model);

		FileService fileService=new FileService();
		FileController fileController=new FileController(model,fileService);

		SqlRepeatablerController controller=new SqlRepeatablerController(model,view,fileController,swingAppender);

		ActionFactory actionFactory=new ActionFactory(controller);
		MenuView menuView=new MenuView(actionFactory);
		view.setMenuView(menuView);
		new MenuPresenter(model,menuView);
		
		ContextMenuView contextMenuView=new ContextMenuView(actionFactory);
		controller.setContextMenuView(contextMenuView);

		view.setVisible(true);
		logger.info(SqlRepeatablerModel.PROGRAMNAME+" "+SqlRepeatablerModel.getProgramVersion());
	}

	private static SwingBufferingLogBackAppender setupLogger(Level level) {
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
