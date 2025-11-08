package lunartools.sqlrepeatabler.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

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
	private JTabbedPane tabbedPane = new JTabbedPane();
	private Image imageBackground;
	private JSplitPane jSplitPaneVertical;

	public MainPanel(SqlRepeatablerModel model,JTextArea logTextArea) {
		this.model=model;
		jSplitPaneVertical=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		jSplitPaneVertical.setOpaque(false);
		jSplitPaneVertical.setTopComponent(tabbedPane);

		LogPanelTextarea logPanel=new LogPanelTextarea(model,logTextArea);
		logPanel.setMinimumSize(new Dimension(0,100));
		jSplitPaneVertical.setBottomComponent(logPanel);

		jSplitPaneVertical.setDividerLocation(700);
		jSplitPaneVertical.setResizeWeight(1.0);

		//		add(jSplitPaneVertical);

		setLayout(new BorderLayout());
		add(jSplitPaneVertical,BorderLayout.CENTER);

		jSplitPaneVertical.setVisible(false);
		imageBackground=DailyBackgroundProvider.getImage();		
		setBackground(Color.black);
		model.addChangeListener(this::updateModelChanges);
	}

	public void updateModelChanges(Object object) {
		if(logger.isTraceEnabled()) {
			logger.trace("update: "+object);
		}
		if(object==SimpleEvents.MODEL_SQLINPUTFILESCHANGED) {
			jSplitPaneVertical.setVisible(model.hasSqlInputFiles());
			revalidate();
			repaint();
		}else if(object==SimpleEvents.MODEL_RESET) {
			jSplitPaneVertical.setVisible(false);
			revalidate();
			repaint();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(imageBackground!=null) {
			int imageWidth=imageBackground.getWidth(null);
			int imageHeight=imageBackground.getHeight(null);
			if(1.0*imageWidth/imageHeight < 1.0*getWidth()/getHeight()) {
				int newImageWidth=(int)(imageWidth*getHeight()/imageHeight);
				int delta=(getWidth()-newImageWidth)/2;
				g.drawImage(imageBackground,delta,0,delta+newImageWidth,getHeight()-1,0,0,imageWidth,imageHeight,null);
			}else {
				int newImageHeight=(int)(imageHeight*getWidth()/imageWidth);
				int delta=(getHeight()-newImageHeight)/2;
				g.drawImage(imageBackground,0,delta,getWidth(),delta+newImageHeight,0,0,imageWidth,imageHeight,null);
			}
		}
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void addTab(String title, IOPanel ioPanel) {
		tabbedPane.addTab(title, ioPanel);
	}

	public IOPanel[] getIoPanels() {
		return ioPanels;
	}

	public void setIoPanels(IOPanel[] ioPanels) {
		this.ioPanels = ioPanels;
	}

}
