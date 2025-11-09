package lunartools.sqlrepeatabler;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.Settings;
import lunartools.sqlrepeatabler.common.SwingBufferingLogBackAppender;
import lunartools.sqlrepeatabler.gui.IOPanel;
import lunartools.sqlrepeatabler.gui.IOPanelController;
import lunartools.sqlrepeatabler.gui.LogEditorPane;
import lunartools.sqlrepeatabler.gui.MainPanel;
import lunartools.sqlrepeatabler.gui.SqlRepeatablerView;
import lunartools.sqlrepeatabler.gui.actions.ActionFactory;
import lunartools.sqlrepeatabler.worker.ConvertSqlFileWorker;

public class SqlRepeatablerController{
	private static Logger logger = LoggerFactory.getLogger(SqlRepeatablerController.class);
	private SqlRepeatablerModel model;
	private SqlRepeatablerView view;
	private ArrayList<IOPanelController> ioPanelControllers=new ArrayList<>();

	public SqlRepeatablerController(SqlRepeatablerModel model,SqlRepeatablerView view,SwingBufferingLogBackAppender swingAppender) {
		SqlRepeatablerSettings settings=SqlRepeatablerSettings.getInstance();
		this.model=model;
		this.view=view;
		view.setBounds(settings.getViewBounds());
		this.view.setActionFactory(new ActionFactory(this));
		this.view.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent event){
				shutdown();
			}
		});

		LogEditorPane logPanelEditorpane=view.getMainPanel().getLogPanel();
		swingAppender.attachConsumer(event ->{
			logPanelEditorpane.consumeLogEvent(event);
		});

		model.addChangeListener(this::updateModelChanges);
	}

	public SqlRepeatablerModel getModel() {
		return model;
	}

	public SqlRepeatablerView getView() {
		return view;
	}

	public void updateModelChanges(Object object) {
		if(logger.isTraceEnabled()) {
			logger.trace("update: "+object);
		}
		if(object==SimpleEvents.EXIT) {
			shutdown();
		}else if(object==SimpleEvents.MODEL_SQLINPUTFILESCHANGED) {
			MainPanel mainPanel=view.getMainPanel();
			for(IOPanelController ioPanelController:ioPanelControllers) {
				model.removeChangeListener(ioPanelController::updateModelChanges);
			}
			ioPanelControllers.clear();
			ArrayList<File> files=model.getSqlInputFiles();
			mainPanel.getTabbedPane().removeAll();
			for(int i=0;i<files.size();i++) {
				IOPanel ioPanel=new IOPanel(model,i);
				ioPanelControllers.add(new IOPanelController(model, ioPanel));
				mainPanel.addTab(files.get(i).getName(), ioPanel);
			}
			mainPanel.revalidate();
			mainPanel.repaint();

			ConvertSqlFileWorker worker=new ConvertSqlFileWorker(model);
			worker.execute();
			view.refreshView();
		}else if(object==SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED) {
			view.refreshView();
		}else if(object==SimpleEvents.MODEL_RESET) {
			for(IOPanelController ioPanelController:ioPanelControllers) {
				model.removeChangeListener(ioPanelController::updateModelChanges);
			}
			ioPanelControllers.clear();
		}
	}

	public void shutdown() {
		Settings settings=SqlRepeatablerSettings.getInstance();
		settings.setRectangle(SqlRepeatablerSettings.VIEW_BOUNDS, view.getBounds());
		try {
			settings.saveSettings();
		} catch (IOException e) {
			logger.error("error while saving settings",e);
		}
		view.setVisible(false);
		view.dispose();
	}

	public void openAboutDialogue() {
		view.showMessageboxAbout();
	}

}
