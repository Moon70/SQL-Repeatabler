package lunartools.sqlrepeatabler.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.ImageTools;
import lunartools.ObservableJFrame;
import lunartools.sqlrepeatabler.About;
import lunartools.sqlrepeatabler.MenubarController;
import lunartools.sqlrepeatabler.SimpleEvents;
import lunartools.sqlrepeatabler.SqlRepeatablerController;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;

public class SqlRepeatablerView extends ObservableJFrame implements Observer{
	private static Logger logger = LoggerFactory.getLogger(SqlRepeatablerView.class);
	public static final double SECTIOAUREA=1.6180339887;
	public static final int WINDOW_MINIMUM_WIDTH=800;
	public static final int WINDOW_MINIMUM_HEIGHT=(int)(WINDOW_MINIMUM_WIDTH/SECTIOAUREA);

	private SqlRepeatablerModel model;
	private SqlRepeatablerController controller;
	private MenubarController menubarController;
	private MainPanel mainPanel;

	public SqlRepeatablerView(SqlRepeatablerModel model, SqlRepeatablerController controller) {
		super.setTitle(SqlRepeatablerModel.PROGRAMNAME+" "+SqlRepeatablerModel.determineProgramVersion());
		setBounds(model.getFrameBounds());
		setMinimumSize(new Dimension(WINDOW_MINIMUM_WIDTH,WINDOW_MINIMUM_HEIGHT));
		setResizable(false);
		this.model=model;
		this.model.addObserver(this);
		this.controller=controller;
		//		this.setLayout(null);

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent event){
				sendMessage(SimpleEvents.EXIT);
			}
		});

		this.menubarController=new MenubarController(this.model,this.controller,this);
		setMenuBar(this.menubarController.createMenubar());

		try {
			List<Image> icons=new ArrayList<Image>();
			icons.add(ImageTools.createImageFromResource("/icons/ProgramIcon64x64.png"));
			icons.add(ImageTools.createImageFromResource("/icons/ProgramIcon56x56.png"));
			icons.add(ImageTools.createImageFromResource("/icons/ProgramIcon48x48.png"));
			icons.add(ImageTools.createImageFromResource("/icons/ProgramIcon40x40.png"));
			icons.add(ImageTools.createImageFromResource("/icons/ProgramIcon32x32.png"));
			icons.add(ImageTools.createImageFromResource("/icons/ProgramIcon24x24.png"));
			icons.add(ImageTools.createImageFromResource("/icons/ProgramIcon16x16.png"));
			this.setIconImages(icons);
		} catch (IOException e) {
			System.err.println("error loading frame icon");
			e.printStackTrace();
		}

		mainPanel=new MainPanel(model);
		add(mainPanel);

		pack();
	}

	@Override
	public void update(Observable observable, Object object) {
		if(logger.isTraceEnabled()) {
			logger.trace("update: "+observable+", "+object);
		}
		if(object==SimpleEvents.MODEL_SQLINPUTFILESCHANGED) {
			refreshGui();
			this.repaint();
		}else if(object==SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED) {
			refreshGui();
			this.repaint();
		}
	}

	private void refreshGui() {
		menubarController.refresh();
	}

	public void sendMessage(Object message) {
		setChanged();
		notifyObservers(message);
	}

	public void showMessageboxAbout() {
		logger.debug("opening about dialogue...");
		About.showAboutDialog(this);
	}

}
