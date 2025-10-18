package lunartools.sqlrepeatabler.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.DailyBackgroundProvider;
import lunartools.sqlrepeatabler.SimpleEvents;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;

public class MainPanel extends JPanel{
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

		imageBackground=DailyBackgroundProvider.getImage();		
		
		add(jSplitPaneVertical);
		jSplitPaneVertical.setVisible(false);
		model.addChangeListener(this::updateModelChanges);
	}

	public void updateModelChanges(Object object) {
		if(logger.isTraceEnabled()) {
			logger.trace("update: "+object);
		}
		if(object==SimpleEvents.MODEL_SQLINPUTFILESCHANGED) {
			jSplitPaneVertical.setVisible(true);
			
			ArrayList<File> files=model.getSqlInputFiles();
			for(File file:files) {
				System.out.println(file);
			}
			if(ioPanels!=null) {
				for(IOPanel iopanel:ioPanels) {
					model.removeChangeListener(iopanel::updateModelChanges);
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
