package lunartools.sqlrepeatabler;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.gui.SqlRepeatablerView;

public class MenubarController implements ActionListener{
	private static Logger logger = LoggerFactory.getLogger(MenubarController.class);
	private static final String ACTIONCOMMAND_OPEN = "open";
	private static final String ACTIONCOMMAND_SAVEAS = "saveAs";
	private static final String ACTIONCOMMAND_RESET = "reset";
	private static final String ACTIONCOMMAND_ABOUT = "about";
	private static final String ACTIONCOMMAND_EXIT = "exit";

	private SqlRepeatablerModel model;
	private SqlRepeatablerController controller;
	private SqlRepeatablerView view;

	private MenuItem menuItem_SelectFolder;
	protected MenuItem menuItem_SaveAs;
	protected MenuItem menuItem_Reset;

	public MenubarController(SqlRepeatablerModel model,SqlRepeatablerController controller,SqlRepeatablerView view) {
		this.model=model;
		this.controller=controller;
		this.view=view;
	}

	public MenuBar createMenubar() {
		MenuBar menuBar=new MenuBar();
		menuBar.add(createFileMenu());
		menuBar.add(createHelpMenu());
		refresh();
		return menuBar;
	}

	private Menu createFileMenu(){
		Menu menu=new Menu("File");
		menu.addActionListener(this);

		menuItem_SelectFolder=new MenuItem("Open");
		menuItem_SelectFolder.setActionCommand(ACTIONCOMMAND_OPEN);
		menuItem_SelectFolder.setEnabled(true);
		menu.add(menuItem_SelectFolder);

		menuItem_SaveAs=new MenuItem("Save As");
		menuItem_SaveAs.setActionCommand(ACTIONCOMMAND_SAVEAS);
		menu.add(menuItem_SaveAs);

		menuItem_Reset=new MenuItem("Reset");
		menuItem_Reset.setActionCommand(ACTIONCOMMAND_RESET);
		menu.add(menuItem_Reset);

		MenuItem menuItem=new MenuItem("Exit");
		menuItem.setActionCommand(ACTIONCOMMAND_EXIT);
		menu.add(menuItem);

		return menu;
	}

	private Menu createHelpMenu(){
		Menu menu=new Menu("?");
		MenuItem menuItem=new MenuItem("About "+SqlRepeatablerModel.PROGRAMNAME);
		menuItem.setActionCommand(ACTIONCOMMAND_ABOUT);
		menu.add(menuItem);
		menu.addActionListener(this);
		return menu;
	}

	@Override
	public void actionPerformed(ActionEvent event){
		if(logger.isTraceEnabled()) {
			logger.trace("actionPerformed: "+event);
		}
		String actionCommand=event.getActionCommand();
		if(actionCommand.equals(ACTIONCOMMAND_EXIT)){
			view.sendMessage(SimpleEvents.EXIT);
		}else if(actionCommand.equals(ACTIONCOMMAND_OPEN)){
			controller.action_FileOpen();
		}else if(actionCommand.equals(ACTIONCOMMAND_SAVEAS)){
			controller.action_FileSaveAs();
		}else if(actionCommand.equals(ACTIONCOMMAND_RESET)){
			controller.action_Reset();
		}else if(actionCommand.equals(ACTIONCOMMAND_ABOUT)){
			view.showMessageboxAbout();
		}
	}

	public void refresh() {
		boolean convertesDataAvailable=model.getConvertedSqlScript().length()>0;
		menuItem_SaveAs.setEnabled(convertesDataAvailable);
		menuItem_Reset.setEnabled(convertesDataAvailable);
	}

}
