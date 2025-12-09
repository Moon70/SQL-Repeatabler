package lunartools.sqlrepeatabler;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import lunartools.AbstractSettings;
import lunartools.sqlrepeatabler.common.SwingBufferingLogBackAppender;
import lunartools.sqlrepeatabler.gui.IOPanel;
import lunartools.sqlrepeatabler.gui.IOPanelController;
import lunartools.sqlrepeatabler.gui.LogEditorPane;
import lunartools.sqlrepeatabler.gui.MainPanel;
import lunartools.sqlrepeatabler.gui.SqlRepeatablerView;
import lunartools.sqlrepeatabler.gui.actions.ActionFactory;
import lunartools.sqlrepeatabler.settings.Settings;
import lunartools.sqlrepeatabler.settings.Theme;
import lunartools.sqlrepeatabler.worker.ConvertSqlFileWorker;

public class SqlRepeatablerController{
	private static Logger logger = LoggerFactory.getLogger(SqlRepeatablerController.class);
	private SqlRepeatablerModel model;
	private SqlRepeatablerView view;
	private ArrayList<IOPanelController> ioPanelControllers=new ArrayList<>();

	public SqlRepeatablerController(SqlRepeatablerModel model,SqlRepeatablerView view,SwingBufferingLogBackAppender swingAppender) {
		Settings settings=Settings.getInstance();
		this.model=model;
		this.view=view;
		view.setBounds(settings.getViewBounds());
		this.view.setActionFactory(new ActionFactory(this));
		this.view.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent event){
				shutdown();
			}

			@Override
			public void windowOpened(WindowEvent e) {
				SwingUtilities.invokeLater(() -> {
					view.revalidate();
					view.repaint();
				});
			}
		});

		LogEditorPane logPanelEditorpane=view.getMainPanel().getLogPanel();
		swingAppender.attachConsumer(event ->{
			logPanelEditorpane.consumeLogEvent(event);
		});

		model.addChangeListener(this::updateModelChanges);
		ThemeManager.getInstance().addChangeListener(this::updateThemeChanges);
		activateTheme();
		view.validate();
		view.setVisible(true);
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
	public void updateThemeChanges(Object object) {
		if(logger.isTraceEnabled()) {
			logger.trace("update: "+object);
		}
		if(object==SimpleEvents.THEMEMANAGER_THEME_CHANGED) {
			activateTheme();
			model.reload();
		}
	}

	private void activateTheme() {
		Theme theme=ThemeManager.getInstance().getTheme();
		switch(theme) {
		case LIGHT:
			FlatLaf.setup(new FlatLightLaf());
			break;
		case DARK:
			FlatLaf.setup(new FlatDarkLaf());
			break;
		case INTELLIJ:
			FlatLaf.setup(new FlatIntelliJLaf());
			break;
		case DARCULA:
			FlatLaf.setup(new FlatDarculaLaf());
			break;
		default:
			break;
		}
		FlatLaf.updateUI();
	}

	public void shutdown() {
		AbstractSettings settings=Settings.getInstance();
		settings.setRectangle(Settings.VIEW_BOUNDS, view.getBounds());
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
