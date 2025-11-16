package lunartools.sqlrepeatabler.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.DailyBackgroundProvider;
import lunartools.sqlrepeatabler.SimpleEvents;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.settings.Settings;

public class MainPanel extends JPanel{
	private static Logger logger = LoggerFactory.getLogger(MainPanel.class);
	private SqlRepeatablerModel model;
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JScrollPane scrollPaneLog;
	private Image imageBackground;
	private JSplitPane jSplitPaneVertical;
	private LogEditorPane logPanel;

	public MainPanel(SqlRepeatablerModel model) {
		this.model=model;
		setLayout(new BorderLayout());
		jSplitPaneVertical=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		jSplitPaneVertical.setOneTouchExpandable(true);
		jSplitPaneVertical.setDividerSize(8);
		jSplitPaneVertical.setOpaque(false);
		jSplitPaneVertical.setTopComponent(tabbedPane);

		logPanel=new LogEditorPane(model);
		logPanel.setMinimumSize(new Dimension(0,100));
		scrollPaneLog=new JScrollPane(logPanel);
		scrollPaneLog.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jSplitPaneVertical.setBottomComponent(scrollPaneLog);

		jSplitPaneVertical.setDividerLocation(700);
		jSplitPaneVertical.setResizeWeight(1.0);

		add(jSplitPaneVertical,BorderLayout.CENTER);

		jSplitPaneVertical.setVisible(false);
		imageBackground=DailyBackgroundProvider.getImage();		
		setBackground(Color.black);
		
		
		model.addChangeListener(this::updateModelChanges);
		SwingUtilities.invokeLater(() -> {
			jSplitPaneVertical.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, evt -> {
			    int newLocation=(int)evt.getNewValue();
			    Settings.getInstance().setDividerlocationConsole(newLocation);
			});
		});
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

	public LogEditorPane getLogPanel() {
		return logPanel;
	}

	public void applyDividerLocation() {
		int min=tabbedPane.getMinimumSize().height;
		int max=jSplitPaneVertical.getHeight()-scrollPaneLog.getMinimumSize().height;
		int location=Settings.getInstance().getDividerlocationConsole();
		if(location!=0 && location>=min && location<=max) {
			jSplitPaneVertical.setDividerLocation(location);
		}
	}

}
