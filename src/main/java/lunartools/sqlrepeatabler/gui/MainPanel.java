package lunartools.sqlrepeatabler.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.ImageTools;
import lunartools.sqlrepeatabler.SimpleEvents;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;

public class MainPanel extends JPanel implements Observer{
	private static Logger logger = LoggerFactory.getLogger(MainPanel.class);
	private SqlRepeatablerModel model;
	private IOPanel[] ioPanels;
	private JTabbedPane tabPanel = new JTabbedPane();
	private Image imageBackground;
	private JSplitPane jSplitPaneVertical;
	
	public MainPanel(SqlRepeatablerModel model,JTextArea logTextArea) {
		this.model=model;
		jSplitPaneVertical=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		jSplitPaneVertical.setOpaque(false);
		jSplitPaneVertical.setTopComponent(tabPanel);
		
		LogPanelTextarea logPanel=new LogPanelTextarea(model,logTextArea);
		jSplitPaneVertical.setBottomComponent(logPanel);

		this.setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
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
						MainPanel.this.model.addSqlInputFiles(arraylistAcceptedFiles);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		try {
			imageBackground = ImageTools.createImageFromResource("/Background01.jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		add(jSplitPaneVertical);
		jSplitPaneVertical.setVisible(false);
		model.addObserver(this);
	}

	@Override
	public void update(Observable observable, Object object) {
		if(logger.isTraceEnabled()) {
			logger.trace("update: "+observable+", "+object);
		}
		if(object==SimpleEvents.MODEL_SQLINPUTFILESCHANGED) {
			jSplitPaneVertical.setVisible(true);
			
			ArrayList<File> files=model.getSqlInputFiles();
			for(File file:files) {
				System.out.println(file);
			}
			if(ioPanels!=null) {
				for(IOPanel iopanel:ioPanels) {
					model.deleteObserver(iopanel);
				}
			}
			ioPanels=new IOPanel[files.size()];
			tabPanel.removeAll();
			for(int i=0;i<files.size();i++) {
				ioPanels[i]=new IOPanel(model,i);
				//model.addObserver(ioPanels[i]);
				tabPanel.addTab(files.get(i).getName(), ioPanels[i]);
			}
			this.revalidate();
			this.repaint();
		}else if(object==SimpleEvents.MODEL_RESET) {
			jSplitPaneVertical.setVisible(false);
			repaint();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(imageBackground!=null) {
			g.drawImage(imageBackground, 0, 0,this);
		}
	}
}
