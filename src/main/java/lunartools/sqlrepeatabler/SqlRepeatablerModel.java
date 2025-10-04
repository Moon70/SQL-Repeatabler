package lunartools.sqlrepeatabler;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.spi.ILoggingEvent;
import lunartools.SwingTools;

public class SqlRepeatablerModel extends Observable{
	private static Logger logger = LoggerFactory.getLogger(SqlRepeatablerModel.class);
	public static final String PROGRAMNAME = "SQL-Repeatabler";
	private static String versionProgram=SwingTools.determineProgramVersion();

	public static final int DEFAULT_FRAME_WIDTH=1290;
	public static final int DEFAULT_FRAME_HEIGHT=(int)(DEFAULT_FRAME_WIDTH/SwingTools.SECTIOAUREA);
	private Rectangle frameBounds=new Rectangle(0,0,DEFAULT_FRAME_WIDTH,DEFAULT_FRAME_HEIGHT);

	private ArrayList<File> sqlInputFiles=new ArrayList<>();
	private ArrayList<StringBuffer> sqlConvertedScripts=new ArrayList<>();

	public static String getProgramVersion() {
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

	public void clearConvertedSqlScript() {
		sqlConvertedScripts=new ArrayList<>();
		sendMessage(SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED);
	}

	public boolean hasSqlConvertedScripts() {
		return sqlConvertedScripts.size()>0;
	}
	
	public StringBuffer getSingleConvertedSqlScript(int index) {
		return sqlConvertedScripts.get(index);
	}
	
	public void setConvertedSqlScripts(ArrayList<StringBuffer> sqlConvertedScripts) {
		this.sqlConvertedScripts=sqlConvertedScripts;
		sendMessage(SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED);
	}
	
	public StringBuffer getConvertedSqlScript() {
		StringBuffer sbConvertedScripts=new StringBuffer();
		sbConvertedScripts.append("-- "+SqlRepeatablerModel.PROGRAMNAME+" "+SqlRepeatablerModel.getProgramVersion()+System.lineSeparator());
		for(int i=0;i<sqlConvertedScripts.size();i++) {
			sbConvertedScripts.append(System.lineSeparator());
			sbConvertedScripts.append("-- Original: "+sqlInputFiles.get(i).getName());
			sbConvertedScripts.append(System.lineSeparator());
			sbConvertedScripts.append(sqlConvertedScripts.get(i));
		}
		return sbConvertedScripts;
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
		sqlConvertedScripts=new ArrayList<>();
		sendMessage(SimpleEvents.MODEL_RESET);
		sendMessage(SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED);
	}

	public void fireLogEvent(ILoggingEvent loggingEvent) {
		sendMessage(loggingEvent);
	}

	public void clearInputPanel() {
		sendMessage(SimpleEvents.MODEL_CLEARINPUTPANEL);
	}

}
