package lunartools.sqlrepeatabler.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.SwingTools;
import lunartools.sqlrepeatabler.About;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.gui.actions.ActionFactory;

public class SqlRepeatablerView extends JFrame{
	private static Logger logger = LoggerFactory.getLogger(SqlRepeatablerView.class);
	public static final int WINDOW_MINIMUM_WIDTH=1600;
	public static final int WINDOW_MINIMUM_HEIGHT=(int)(WINDOW_MINIMUM_WIDTH/SwingTools.SECTIOAUREA);

	private SqlRepeatablerModel model;
	private MainPanel mainPanel;
	private MenuModel menuModel;

	public SqlRepeatablerView(SqlRepeatablerModel model,JTextArea logTextArea) {
		super.setTitle(SqlRepeatablerModel.PROGRAMNAME+" "+SqlRepeatablerModel.getProgramVersion());
		setBounds(model.getFrameBounds());
		//setMinimumSize(new Dimension(WINDOW_MINIMUM_WIDTH,WINDOW_MINIMUM_HEIGHT));
		setSize(new Dimension(WINDOW_MINIMUM_WIDTH,WINDOW_MINIMUM_HEIGHT));
		setResizable(true);
		this.model=model;
		this.setIconImages(SwingTools.getDefaultIconImages());

		setLayout(new BorderLayout());
		setBackground(Color.black);

		mainPanel=new MainPanel(model,logTextArea);
		add(mainPanel);
		
		JPanel glass=(JPanel)getGlassPane();
		glass.setVisible(true);
		glass.setOpaque(false);
		glass.setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					@SuppressWarnings("unchecked")
					List<File> droppedFiles = (List<File>)evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					ArrayList<File> arraylistAcceptedFiles=new ArrayList<>();
					for (File file : droppedFiles) {
						if(file.getName().toLowerCase().endsWith(".sql")) {
							arraylistAcceptedFiles.add(file);
						}else {
							logger.warn("ignoring unsupported file: "+file);
						}
					}
					if(arraylistAcceptedFiles.size()>0) {
						SqlRepeatablerView.this.model.addSqlInputFiles(arraylistAcceptedFiles);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		addComponentListener(new ComponentAdapter() {
		    @Override
		    public void componentShown(ComponentEvent e) {
		        repaint();
		    }
		});
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
		boolean convertedDataAvailable=model.hasSqlConvertedScripts();
		menuModel.getMenuFileItemSaveAs().setEnabled(convertedDataAvailable);
		menuModel.getMenuFileItemReset().setEnabled(convertedDataAvailable);
	}

	public void showMessageboxAbout() {
		logger.debug("opening about dialogue...");
		About.showAboutDialog(this);
	}
	
}
