package lunartools.sqlrepeatabler.gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

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

	private JMenu menuHelp;
	private JMenuItem menuHelpItemAbout;
	
	public MenuModel(ActionFactory actionFactory) {
		this.actionFactory=actionFactory;
		menuBar=new JMenuBar();
		menuBar.add(createFileMenu());
		menuBar.add(createHelpMenu());
	}
    
    private JMenu createFileMenu(){
        menuFile=new JMenu("File");

        menuFileItemOpen=new JMenuItem(actionFactory.createOpenAction());
        menuFileItemOpen.setEnabled(true);
        menuFileItemOpen.setIcon(ImageTools.createImageIcon("/icons/file_open_16.png"));
        menuFile.add(menuFileItemOpen);

        menuFileItemSaveAs=new JMenuItem(actionFactory.createSaveAsAction());
        menuFileItemSaveAs.setEnabled(true);
        menuFileItemSaveAs.setIcon(ImageTools.createImageIcon("/icons/save_as_16.png"));
        menuFile.add(menuFileItemSaveAs);

        menuFileItemReload=new JMenuItem(actionFactory.createReloadAction());
        menuFileItemReload.setEnabled(true);
        menuFileItemReload.setIcon(ImageTools.createImageIcon("/icons/refresh_16.png"));
        menuFile.add(menuFileItemReload);

        menuFileItemReset=new JMenuItem(actionFactory.createResetAction());
        menuFileItemReset.setEnabled(true);
        menuFileItemReset.setIcon(ImageTools.createImageIcon("/icons/restart_alt_16.png"));
        menuFile.add(menuFileItemReset);

        menuFileItemExitProgram=new JMenuItem(actionFactory.createExitProgramAction());
        menuFileItemExitProgram.setIcon(ImageTools.createImageIcon("/icons/logout_16.png"));
        menuFile.add(menuFileItemExitProgram);
        return menuFile;
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
