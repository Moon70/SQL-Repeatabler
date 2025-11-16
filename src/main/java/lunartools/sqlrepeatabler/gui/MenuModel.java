package lunartools.sqlrepeatabler.gui;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import lunartools.ImageTools;
import lunartools.sqlrepeatabler.gui.actions.ActionFactory;
import lunartools.sqlrepeatabler.settings.ProcessingOrder;
import lunartools.sqlrepeatabler.settings.Settings;

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
	private JRadioButtonMenuItem radioButtonProcessAsAdded;
	private JRadioButtonMenuItem radioButtonProcessByCreationDate;
	private JRadioButtonMenuItem radioButtonProcessAlphabetically;

	private JMenu menuHelp;
	private JMenuItem menuHelpItemAbout;
	
	public MenuModel(ActionFactory actionFactory) {
		this.actionFactory=actionFactory;
		menuBar=new JMenuBar();
		menuBar.add(createFileMenu());
		menuBar.add(createPreferencesMenu());
		menuBar.add(createHelpMenu());
        refresh();
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

        menuProcessFiles=new JMenu("File processing order-");
        menuPreferences.add(menuProcessFiles);
        menuProcessFiles.setIcon(ImageTools.createImageIcon("/icons/sort_16.png"));
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

	private void refresh() {
		Settings settings=Settings.getInstance();
		ProcessingOrder processingOrder=settings.getProcessingOrder();
		radioButtonProcessAsAdded.setSelected(processingOrder==ProcessingOrder.ASADDED);
		radioButtonProcessByCreationDate.setSelected(processingOrder==ProcessingOrder.CREATIONDATE);
		radioButtonProcessAlphabetically.setSelected(processingOrder==ProcessingOrder.ALPHABETICALLY);
	}
}
