package lunartools.sqlrepeatabler.gui;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.ImageTools;
import lunartools.sqlrepeatabler.common.action.ActionFactory;
import lunartools.sqlrepeatabler.common.model.SimpleEvents;
import lunartools.sqlrepeatabler.common.ui.IconProvider;
import lunartools.sqlrepeatabler.common.ui.Icons;
import lunartools.sqlrepeatabler.common.ui.ThemeManager;
import lunartools.sqlrepeatabler.infrastructure.config.ProcessingOrder;
import lunartools.sqlrepeatabler.infrastructure.config.Settings;
import lunartools.sqlrepeatabler.infrastructure.config.Theme;
import lunartools.sqlrepeatabler.main.SqlRepeatablerModel;

public class MenuModel {
	private static Logger logger = LoggerFactory.getLogger(MenuModel.class);
	private SqlRepeatablerModel model;
	private ActionFactory actionFactory;
	private JMenuBar menuBar;

	private JMenu menuFile;
	private JMenuItem menuFileItemOpen;
	private JMenuItem menuFileItemSaveAs;
	private JMenuItem menuFileItemReload;
	private JMenuItem menuFileItemReset;
	private JMenuItem menuFileItemExitProgram;

	private JMenu menuPreferences;
	private JMenu menuProcessFiles;
	private JRadioButtonMenuItem radioButtonProcessAsAdded;
	private JRadioButtonMenuItem radioButtonProcessByCreationDate;
	private JRadioButtonMenuItem radioButtonProcessAlphabetically;
	private JCheckBoxMenuItem checkboxBackgroundColor;
	private JMenu menuTheme;
	private JRadioButtonMenuItem radioButtonThemeFlatLightLaf;
	private JRadioButtonMenuItem radioButtonThemeFlatDarkLaf;
	private JRadioButtonMenuItem radioButtonThemeFlatIntelliJLaf;
	private JRadioButtonMenuItem radioButtonThemeFlatDarculaLaf;

	private JMenu menuHelp;
	private JMenuItem menuHelpItemAbout;

	public MenuModel(SqlRepeatablerModel model, ActionFactory actionFactory) {
		this.model=model;
		this.actionFactory=actionFactory;
		menuBar=new JMenuBar();
		menuBar.add(createFileMenu());
		menuBar.add(createPreferencesMenu());
		menuBar.add(createHelpMenu());
		refreshMenuItems();
		refresh();
		model.addChangeListener(this::updateModelChanges);
	}

	private JMenu createFileMenu(){
		menuFile=new JMenu("File");

		menuFileItemOpen=new JMenuItem(actionFactory.createOpenAction());
		menuFileItemOpen.setIcon(IconProvider.getFlatSvgIcon(Icons.FILE_OPEN,menuFileItemOpen));
		menuFileItemOpen.setEnabled(true);
		menuFile.add(menuFileItemOpen);

		menuFileItemSaveAs=new JMenuItem(actionFactory.createSaveAsAction());
		menuFileItemSaveAs.setIcon(IconProvider.getFlatSvgIcon(Icons.SAVE_AS,menuFileItemSaveAs));
		menuFileItemSaveAs.setEnabled(true);
		menuFile.add(menuFileItemSaveAs);

		menuFileItemReload=new JMenuItem(actionFactory.createReloadAction());
		menuFileItemReload.setIcon(IconProvider.getFlatSvgIcon(Icons.REFRESH,menuFileItemReload));
		menuFile.add(menuFileItemReload);

		menuFileItemReset=new JMenuItem(actionFactory.createResetAction());
		menuFileItemReset.setIcon(IconProvider.getFlatSvgIcon(Icons.RESTART,menuFileItemReset));
		menuFile.add(menuFileItemReset);

		menuFileItemExitProgram=new JMenuItem(actionFactory.createExitProgramAction());
		menuFileItemExitProgram.setIcon(IconProvider.getFlatSvgIcon(Icons.EXIT,menuFileItemExitProgram));
		menuFile.add(menuFileItemExitProgram);

		return menuFile;
	}

	private JMenu createPreferencesMenu(){
		menuPreferences=new JMenu("Preferences");

		menuProcessFiles=new JMenu("File processing order-");
		menuPreferences.add(menuProcessFiles);
		menuProcessFiles.setIcon(IconProvider.getFlatSvgIcon(Icons.EXIT,menuProcessFiles));
		ButtonGroup buttonGroup = new ButtonGroup();

		radioButtonProcessAsAdded=new JRadioButtonMenuItem(actionFactory.createAsAddedRadioButtonAction());
		menuProcessFiles.add(radioButtonProcessAsAdded);
		buttonGroup.add(radioButtonProcessAsAdded);

		radioButtonProcessByCreationDate=new JRadioButtonMenuItem(actionFactory.createByCreationDateRadioButtonAction());
		menuProcessFiles.add(radioButtonProcessByCreationDate);
		buttonGroup.add(radioButtonProcessByCreationDate);

		radioButtonProcessAlphabetically=new JRadioButtonMenuItem(actionFactory.createAlphabeticallyRadioButtonAction());
		menuProcessFiles.add(radioButtonProcessAlphabetically);
		buttonGroup.add(radioButtonProcessAlphabetically);

		checkboxBackgroundColor=new JCheckBoxMenuItem(actionFactory.createBackgroundColorCheckboxAction());
		menuPreferences.add(checkboxBackgroundColor);

		menuTheme=new JMenu("Theme");
		menuPreferences.add(menuTheme);
		buttonGroup = new ButtonGroup();
		
		radioButtonThemeFlatLightLaf=new JRadioButtonMenuItem(actionFactory.createLightThemeRadioButtonAction());
		menuTheme.add(radioButtonThemeFlatLightLaf);
		buttonGroup.add(radioButtonThemeFlatLightLaf);
		
		radioButtonThemeFlatDarkLaf=new JRadioButtonMenuItem(actionFactory.createDarkThemeRadioButtonAction());
		menuTheme.add(radioButtonThemeFlatDarkLaf);
		buttonGroup.add(radioButtonThemeFlatDarkLaf);
		
		radioButtonThemeFlatIntelliJLaf=new JRadioButtonMenuItem(actionFactory.createIntellijThemeRadioButtonAction());
		menuTheme.add(radioButtonThemeFlatIntelliJLaf);
		buttonGroup.add(radioButtonThemeFlatIntelliJLaf);
		
		radioButtonThemeFlatDarculaLaf=new JRadioButtonMenuItem(actionFactory.createDarculaThemeRadioButtonAction());
		menuTheme.add(radioButtonThemeFlatDarculaLaf);
		buttonGroup.add(radioButtonThemeFlatDarculaLaf);
		
		return menuPreferences;
	}

	private JMenu createHelpMenu(){
		menuHelp=new JMenu("?");
		menuHelpItemAbout=new JMenuItem(actionFactory.createAboutAction());
		menuHelpItemAbout.setIcon(ImageTools.createImageIcon("/icons/ProgramIcon16.png"));
		menuHelp.add(menuHelpItemAbout);
		return menuHelp;
	}

	public JMenuBar getMenuBar() {
		return menuBar;
	}

	public JMenu getMenuFile() {
		return menuFile;
	}

	public JMenuItem getMenuFileItemOpen() {
		return menuFileItemOpen;
	}

	public JMenuItem getMenuFileItemSaveAs() {
		return menuFileItemSaveAs;
	}

	public JMenuItem getMenuFileItemReset() {
		return menuFileItemReset;
	}

	public JMenuItem getMenuFileItemExitProgram() {
		return menuFileItemExitProgram;
	}

	public JMenu getMenuHelp() {
		return menuHelp;
	}

	public JMenuItem getMenuHelpItemAbout() {
		return menuHelpItemAbout;
	}

	public void updateModelChanges(Object object) {
		if(logger.isTraceEnabled()) {
			logger.trace("update: "+object);
		}
		if(object==SimpleEvents.MODEL_SQLINPUTFILESCHANGED) {
			refreshMenuItems();
		}else if(object==SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED) {
			refreshMenuItems();
		}else if(object==SimpleEvents.MODEL_RESET) {
			refreshMenuItems();
		}
	}

	private void refreshMenuItems() {
		menuFileItemReload.setEnabled(model.hasSqlInputFiles());
		menuFileItemReset.setEnabled(model.hasSqlInputFiles());
		menuFileItemSaveAs.setEnabled(model.hasSqlConvertedScripts());
	}

	private void refresh() {
		Settings settings=Settings.getInstance();
		ProcessingOrder processingOrder=settings.getProcessingOrder();
		radioButtonProcessAsAdded.setSelected(processingOrder==ProcessingOrder.ASADDED);
		radioButtonProcessByCreationDate.setSelected(processingOrder==ProcessingOrder.CREATIONDATE);
		radioButtonProcessAlphabetically.setSelected(processingOrder==ProcessingOrder.ALPHABETICALLY);
		checkboxBackgroundColor.setSelected(settings.isBackgroundColorEnabled());
		Theme theme=ThemeManager.getInstance().getTheme();
		radioButtonThemeFlatLightLaf.setSelected(theme==Theme.LIGHT);
		radioButtonThemeFlatDarkLaf.setSelected(theme==Theme.DARK);
		radioButtonThemeFlatIntelliJLaf.setSelected(theme==Theme.INTELLIJ);
		radioButtonThemeFlatDarculaLaf.setSelected(theme==Theme.DARCULA);
	}
}
