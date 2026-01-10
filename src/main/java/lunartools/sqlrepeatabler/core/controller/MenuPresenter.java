package lunartools.sqlrepeatabler.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.model.SimpleEvents;
import lunartools.sqlrepeatabler.common.ui.Theme;
import lunartools.sqlrepeatabler.common.util.ThemeManager;
import lunartools.sqlrepeatabler.common.view.MenuView;
import lunartools.sqlrepeatabler.core.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.infrastructure.config.ProcessingOrder;
import lunartools.sqlrepeatabler.infrastructure.config.Settings;

public class MenuPresenter {
	private static Logger logger = LoggerFactory.getLogger(MenuPresenter.class);
	private SqlRepeatablerModel model;
	private MenuView menuView;

	public MenuPresenter(SqlRepeatablerModel model,MenuView menuView) {
		this.model=model;
		this.menuView=menuView;
		updateMenuItemVisibility();
		updateMenuItemSelections();
		model.addChangeListener(this::updateModelChanges);
	}

	public void updateModelChanges(Object object) {
		if(logger.isTraceEnabled()) {
			logger.trace("update: "+object);
		}
		if(object==SimpleEvents.MODEL_SQLINPUTFILESCHANGED) {
			updateMenuItemVisibility();
		}else if(object==SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED) {
			updateMenuItemVisibility();
		}else if(object==SimpleEvents.MODEL_RESET) {
			updateMenuItemVisibility();
		}
	}

	private void updateMenuItemVisibility() {
		menuView.getMenuFileItemReload().setEnabled(model.hasSqlInputFiles());
		menuView.getMenuFileItemReset().setEnabled(model.hasSqlInputFiles());
		menuView.getMenuFileItemSaveAs().setEnabled(model.hasSqlConvertedScripts());
	}

	private void updateMenuItemSelections() {
		Settings settings=Settings.getInstance();
		ProcessingOrder processingOrder=settings.getProcessingOrder();
		menuView.getRadioButtonProcessAlphabetically().setSelected(processingOrder==ProcessingOrder.ALPHABETICALLY);
		menuView.getRadioButtonProcessAsAdded().setSelected(processingOrder==ProcessingOrder.ASADDED);
		menuView.getRadioButtonProcessByCreationDate().setSelected(processingOrder==ProcessingOrder.CREATIONDATE);

		menuView.getCheckboxBackgroundColor().setSelected(settings.isBackgroundColorEnabled());

		Theme theme=ThemeManager.getInstance().getTheme();
		menuView.getRadioButtonThemeFlatLightLaf().setSelected(theme==Theme.LIGHT);
		menuView.getRadioButtonThemeFlatDarkLaf().setSelected(theme==Theme.DARK);
		menuView.getRadioButtonThemeFlatIntelliJLaf().setSelected(theme==Theme.INTELLIJ);
		menuView.getRadioButtonThemeFlatDarculaLaf().setSelected(theme==Theme.DARCULA);
	}

}
