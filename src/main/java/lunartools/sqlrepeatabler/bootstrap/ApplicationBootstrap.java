package lunartools.sqlrepeatabler.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.ThresholdFilter;
import lunartools.sqlrepeatabler.common.action.ActionFactory;
import lunartools.sqlrepeatabler.common.ui.ContextMenuView;
import lunartools.sqlrepeatabler.common.ui.MenuView;
import lunartools.sqlrepeatabler.controller.FileController;
import lunartools.sqlrepeatabler.controller.MenuPresenter;
import lunartools.sqlrepeatabler.infrastructure.config.SwingBufferingLogBackAppender;
import lunartools.sqlrepeatabler.main.SqlRepeatablerController;
import lunartools.sqlrepeatabler.main.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.main.SqlRepeatablerView;
import lunartools.sqlrepeatabler.services.FileService;

public class ApplicationBootstrap {
	private static Logger logger = LoggerFactory.getLogger(ApplicationBootstrap.class);

	private ApplicationBootstrap() {}

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
