package lunartools.sqlrepeatabler;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlRepeatablerModel extends Observable{
	private static Logger logger = LoggerFactory.getLogger(SqlRepeatablerModel.class);
	public static final String PROGRAMNAME = "SQL-Repeatabler";
	private static String versionProgram;

	public static final double SECTIOAUREA=1.6180339887;
	public static final int DEFAULT_FRAME_WIDTH=1290;
	public static final int DEFAULT_FRAME_HEIGHT=(int)(DEFAULT_FRAME_WIDTH/SECTIOAUREA);
	private Rectangle frameBounds=new Rectangle(0,0,DEFAULT_FRAME_WIDTH,DEFAULT_FRAME_HEIGHT);
	
	private ArrayList<File> sqlInputFiles=new ArrayList<>();
	private StringBuffer sbConvertedSqlScript=new StringBuffer();

	public static String determineProgramVersion() {
		if(versionProgram==null) {
			versionProgram="";
			Properties properties = new Properties();
			InputStream inputStream=SqlRepeatablerModel.class.getClassLoader().getResourceAsStream("project.properties");
			if(inputStream==null) {
				System.err.println("project.properties not found");
				return "";
			}
			try {
				properties.load(inputStream);
				versionProgram=properties.getProperty("version");
			} catch (IOException e) {
				System.err.println("error loading project.properties");
				e.printStackTrace();
			}
			if("${project.version}".equals(versionProgram)) {
				versionProgram="";
			}
		}
		return versionProgram;
	}

	private void sendMessage(Object message) {
		setChanged();
		notifyObservers(message);
	}

	public ArrayList<File> getSqlInputFiles() {
		return sqlInputFiles;
	}

	public void addSqlInputFile(File file) {
		if(!this.sqlInputFiles.contains(file)) {
			this.sqlInputFiles.add(file);
			sendMessage(SimpleEvents.MODEL_SQLINPUTFILESCHANGED);
		}
	}

	public void addSqlInputFiles(ArrayList<File> files) {
		boolean changed=false;
		for(int i=0;i<files.size();i++) {
			File file=files.get(i);
			if(!this.sqlInputFiles.contains(file)) {
				this.sqlInputFiles.add(file);
				changed=true;
			}
		}
		if(changed) {
			sendMessage(SimpleEvents.MODEL_SQLINPUTFILESCHANGED);
		}
	}

	public void addConvertedSqlScript(String s) {
		sbConvertedSqlScript.append(s);
		sendMessage(SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED);
	}
	
	public void clearConvertedSqlScript() {
		sbConvertedSqlScript=new StringBuffer();
		sendMessage(SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED);
	}
	
	public StringBuffer getConvertedSqlScript() {
		return sbConvertedSqlScript;
	}
	
	public static Rectangle getDefaultFrameBounds() {
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();  
		GraphicsDevice defaultGraphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
		Rectangle graphicsDeviceBounds = defaultGraphicsDevice.getDefaultConfiguration().getBounds();
		int marginX=(graphicsDeviceBounds.width-DEFAULT_FRAME_WIDTH)>>1;
		int marginY=(graphicsDeviceBounds.height-DEFAULT_FRAME_HEIGHT)>>1;
		return new Rectangle(graphicsDeviceBounds.x+marginX,graphicsDeviceBounds.y+marginY,DEFAULT_FRAME_WIDTH,DEFAULT_FRAME_HEIGHT);
	}

	public static Dimension getDefaultFrameSize() {
		return new Dimension(DEFAULT_FRAME_WIDTH,DEFAULT_FRAME_HEIGHT);
	}

	public void setFrameBounds(Rectangle frameBounds) {
		this.frameBounds = frameBounds;
		sendMessage(SimpleEvents.MODEL_FRAMESIZECHANGED);
	}

	public Rectangle getFrameBounds() {
		return frameBounds;
	}
	
	public void reset() {
		sqlInputFiles=new ArrayList<>();
		sbConvertedSqlScript=new StringBuffer();
		sendMessage(SimpleEvents.MODEL_RESET);
		sendMessage(SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED);
	}
	
}
