package lunartools.sqlrepeatabler.gui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.SwingTools;
import lunartools.sqlrepeatabler.About;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.gui.actions.ActionFactory;

public class SqlRepeatablerView extends JFrame{
	private static Logger logger = LoggerFactory.getLogger(SqlRepeatablerView.class);
	public static final double SECTIOAUREA=1.6180339887;
	public static final int WINDOW_MINIMUM_WIDTH=1600;
	public static final int WINDOW_MINIMUM_HEIGHT=(int)(WINDOW_MINIMUM_WIDTH/SECTIOAUREA);

	private SqlRepeatablerModel model;
	private MainPanel mainPanel;
	private MenuModel menuModel;

	public SqlRepeatablerView(SqlRepeatablerModel model,JTextArea logTextArea) {
		super.setTitle(SqlRepeatablerModel.PROGRAMNAME+" "+SqlRepeatablerModel.getProgramVersion());
		setBounds(model.getFrameBounds());
		setMinimumSize(new Dimension(WINDOW_MINIMUM_WIDTH,WINDOW_MINIMUM_HEIGHT));
		setSize(new Dimension(WINDOW_MINIMUM_WIDTH,WINDOW_MINIMUM_HEIGHT));
		setResizable(false);
		this.model=model;
		this.setIconImages(SwingTools.getDefaultIconImages());

		mainPanel=new MainPanel(model,logTextArea);
		add(mainPanel);
		pack();
	}

	public MenuModel getMenuModel() {
		return menuModel;
	}

	public void setActionFactory(ActionFactory actionFactory) {
		this.menuModel=new MenuModel(actionFactory);
		setJMenuBar(menuModel.getMenuBar());
		refreshView();
	}

	public void refreshView() {
		boolean convertedDataAvailable=model.getConvertedSqlScript().length()>0;
		menuModel.getMenuFileItemSaveAs().setEnabled(convertedDataAvailable);
		menuModel.getMenuFileItemReset().setEnabled(convertedDataAvailable);
	}

	public void showMessageboxAbout() {
		logger.debug("opening about dialogue...");
		About.showAboutDialog(this);
	}

}
