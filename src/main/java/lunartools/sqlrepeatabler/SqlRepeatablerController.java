package lunartools.sqlrepeatabler;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.Settings;
import lunartools.sqlrepeatabler.gui.SqlRepeatablerView;
import lunartools.sqlrepeatabler.gui.actions.ActionFactory;
import lunartools.sqlrepeatabler.worker.ConvertSqlFileWorker;

public class SqlRepeatablerController{
	private static Logger logger = LoggerFactory.getLogger(SqlRepeatablerController.class);
	private SqlRepeatablerModel model;
	private SqlRepeatablerView view;

	public SqlRepeatablerController(SqlRepeatablerModel model,SqlRepeatablerView view) {
		Settings settings=SqlRepeatablerSettings.getSettings();
		this.model=model;
		this.view=view;
		this.view.setActionFactory(new ActionFactory(this));
		this.view.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent event){
				shutdown();
			}
		});

		Rectangle frameBounds=fixScreenBounds(settings.getRectangle(SqlRepeatablerSettings.VIEW_BOUNDS, SqlRepeatablerModel.getDefaultFrameBounds()),SqlRepeatablerModel.getDefaultFrameSize());
		model.setFrameBounds(frameBounds);
		model.addChangeListener(this::updateModelChanges);
	}

	private Rectangle fixScreenBounds(Rectangle screenBounds, Dimension defaultFrameSize) {
		int centerX=screenBounds.x+screenBounds.width>>1;
		int centerY=screenBounds.y+screenBounds.height>>1;
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();  
		GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();
		int numberOfGraphicsDevices=graphicsDevices.length;
		for(int i=0;i<numberOfGraphicsDevices;i++){
			GraphicsDevice graphicsDevice=graphicsDevices[i];
			Rectangle graphicsDeviceBounds = graphicsDevice.getDefaultConfiguration().getBounds();
			if(
					centerX>=graphicsDeviceBounds.x &&
					centerY>=graphicsDeviceBounds.y &&
					centerX<=graphicsDeviceBounds.x+graphicsDeviceBounds.width &&
					centerY<=graphicsDeviceBounds.y+graphicsDeviceBounds.height
					) {
				return screenBounds;
			}
		}
		GraphicsDevice graphicsDevice=graphicsDevices[0];
		Rectangle graphicsDeviceBounds = graphicsDevice.getDefaultConfiguration().getBounds();
		int marginX=(graphicsDeviceBounds.width-defaultFrameSize.width)>>1;
					int marginY=(graphicsDeviceBounds.height-defaultFrameSize.height)>>1;
					return new Rectangle(graphicsDeviceBounds.x+marginX,graphicsDeviceBounds.y+marginY,defaultFrameSize.width,defaultFrameSize.height);
	}

	public SqlRepeatablerModel getModel() {
		return model;
	}

	public SqlRepeatablerView getView() {
		return view;
	}

	public void updateModelChanges(Object object) {
		if(logger.isTraceEnabled()) {
			logger.trace("update: "+object);
		}
		if(object==SimpleEvents.EXIT) {
			shutdown();
		}else if(object==SimpleEvents.MODEL_SQLINPUTFILESCHANGED) {
			ConvertSqlFileWorker worker=new ConvertSqlFileWorker(model);
			worker.execute();
			view.refreshView();
		}else if(object==SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED) {
			view.refreshView();
		}
	}

	public void shutdown() {
		Settings settings=SqlRepeatablerSettings.getSettings();
		settings.setRectangle(SqlRepeatablerSettings.VIEW_BOUNDS, view.getBounds());
		try {
			settings.saveSettings();
		} catch (IOException e) {
			logger.error("error while saving settings",e);
		}
		view.setVisible(false);
		view.dispose();
	}

	public void openAboutDialogue() {
		view.showMessageboxAbout();
	}

}
