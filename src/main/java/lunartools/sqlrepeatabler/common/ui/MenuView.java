package lunartools.sqlrepeatabler.common.ui;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import lunartools.ImageTools;
import lunartools.sqlrepeatabler.common.action.ActionFactory;

public class MenuView {
	private final ActionFactory actionFactory;
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

	public MenuView(ActionFactory actionFactory) {
		this.actionFactory=actionFactory;
		menuBar=new JMenuBar();
		menuBar.add(createFileMenu());
		menuBar.add(createPreferencesMenu());
		menuBar.add(createHelpMenu());
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
		menuProcessFiles.setIcon(IconProvider.getFlatSvgIcon(Icons.SORT,menuProcessFiles));
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


	public JMenuItem getMenuFileItemSaveAs() {
		return menuFileItemSaveAs;
	}

	public JMenuItem getMenuFileItemReload() {
		return menuFileItemReload;
	}

	public JMenuItem getMenuFileItemReset() {
		return menuFileItemReset;
	}

	public JRadioButtonMenuItem getRadioButtonProcessAsAdded() {
		return radioButtonProcessAsAdded;
	}

	public JRadioButtonMenuItem getRadioButtonProcessByCreationDate() {
		return radioButtonProcessByCreationDate;
	}

	public JRadioButtonMenuItem getRadioButtonProcessAlphabetically() {
		return radioButtonProcessAlphabetically;
	}

	public JCheckBoxMenuItem getCheckboxBackgroundColor() {
		return checkboxBackgroundColor;
	}

	public JRadioButtonMenuItem getRadioButtonThemeFlatLightLaf() {
		return radioButtonThemeFlatLightLaf;
	}

	public JRadioButtonMenuItem getRadioButtonThemeFlatDarkLaf() {
		return radioButtonThemeFlatDarkLaf;
	}

	public JRadioButtonMenuItem getRadioButtonThemeFlatIntelliJLaf() {
		return radioButtonThemeFlatIntelliJLaf;
	}

	public JRadioButtonMenuItem getRadioButtonThemeFlatDarculaLaf() {
		return radioButtonThemeFlatDarculaLaf;
	}

}
