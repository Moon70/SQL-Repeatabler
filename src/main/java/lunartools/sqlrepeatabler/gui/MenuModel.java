package lunartools.sqlrepeatabler.gui;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;

import lunartools.ImageTools;
import lunartools.sqlrepeatabler.gui.actions.ActionFactory;

public class MenuModel {
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
	private JRadioButton radioButton1;
	private JRadioButton radioButton2;

	private JMenu menuHelp;
	private JMenuItem menuHelpItemAbout;
	
	public MenuModel(ActionFactory actionFactory) {
		this.actionFactory=actionFactory;
		menuBar=new JMenuBar();
		menuBar.add(createFileMenu());
		menuBar.add(createPreferencesMenu());
		menuBar.add(createHelpMenu());
	}
    
    private JMenu createFileMenu(){
        menuFile=new JMenu("File");

        menuFileItemOpen=new JMenuItem(actionFactory.createOpenAction());
        menuFileItemOpen.setIcon(ImageTools.createImageIcon("/icons/file_open_16.png"));
        menuFileItemOpen.setEnabled(true);
        menuFile.add(menuFileItemOpen);

        menuFileItemSaveAs=new JMenuItem(actionFactory.createSaveAsAction());
        menuFileItemSaveAs.setIcon(ImageTools.createImageIcon("/icons/save_as_16.png"));
        menuFileItemSaveAs.setEnabled(true);
        menuFile.add(menuFileItemSaveAs);

        menuFileItemReload=new JMenuItem(actionFactory.createReloadAction());
        menuFileItemReload.setIcon(ImageTools.createImageIcon("/icons/refresh_16.png"));
        menuFileItemReload.setEnabled(true);
        menuFile.add(menuFileItemReload);

        menuFileItemReset=new JMenuItem(actionFactory.createResetAction());
        menuFileItemReset.setIcon(ImageTools.createImageIcon("/icons/restart_alt_16.png"));
        menuFileItemReset.setEnabled(true);
        menuFile.add(menuFileItemReset);

        menuFileItemExitProgram=new JMenuItem(actionFactory.createExitProgramAction());
        menuFileItemExitProgram.setIcon(ImageTools.createImageIcon("/icons/logout_16.png"));
        menuFile.add(menuFileItemExitProgram);
        return menuFile;
    }
    
	private JMenu createPreferencesMenu(){
        menuPreferences=new JMenu("Preferences");

        menuProcessFiles=new JMenu("Process files-");
        menuPreferences.add(menuProcessFiles);
		ButtonGroup buttonGroup = new ButtonGroup();
		
		radioButton1=new JRadioButton("by creation date");
		menuProcessFiles.add(radioButton1);
		buttonGroup.add(radioButton1);
		
		radioButton2=new JRadioButton("alphabetically");
		menuProcessFiles.add(radioButton2);
		buttonGroup.add(radioButton2);

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

}
