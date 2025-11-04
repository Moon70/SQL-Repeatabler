package lunartools.sqlrepeatabler;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ch.qos.logback.classic.spi.ILoggingEvent;
import lunartools.ChangeListenerSupport;
import lunartools.SwingTools;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlScript;

public class SqlRepeatablerModel implements ChangeListenerSupport{
	public static final String PROGRAMNAME = "SQL-Repeatabler";
	private static String versionProgram=SwingTools.determineProgramVersion();
	private final List<ChangeListener> listeners = new CopyOnWriteArrayList<>();

	public static final int DEFAULT_FRAME_WIDTH=1290;
	public static final int DEFAULT_FRAME_HEIGHT=(int)(DEFAULT_FRAME_WIDTH/SwingTools.SECTIOAUREA);
	private Rectangle frameBounds=new Rectangle(0,0,DEFAULT_FRAME_WIDTH,DEFAULT_FRAME_HEIGHT);

	private ArrayList<File> sqlInputFiles=new ArrayList<>();
	private ArrayList<SqlScript> sqlScripts=new ArrayList<>();
	private ArrayList<SqlBlock> sqlConvertedScriptBlocks=new ArrayList<>();

	public static String getProgramVersion() {
		return versionProgram;
	}

	@Override
	public List<ChangeListener> getListeners() {
		return listeners;
	}

	public ArrayList<File> getSqlInputFiles() {
		return sqlInputFiles;
	}

	public void addSqlInputFile(File file) {
		if(!this.sqlInputFiles.contains(file)) {
			this.sqlInputFiles.add(file);
			notifyListeners(SimpleEvents.MODEL_SQLINPUTFILESCHANGED);
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
			notifyListeners(SimpleEvents.MODEL_SQLINPUTFILESCHANGED);
		}
	}

	public void clearConvertedSqlScriptBlocks() {
		sqlConvertedScriptBlocks=new ArrayList<>();
		notifyListeners(SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED);
	}

	public boolean hasSqlConvertedScripts() {
		return sqlConvertedScriptBlocks.size()>0;
	}

	public SqlBlock getSingleConvertedSqlScriptBlock(int index) {
		if(index>=sqlConvertedScriptBlocks.size()) {
			return new SqlBlock();
		}
		return sqlConvertedScriptBlocks.get(index);
	}
	
	public SqlScript getSqlScript(int index) {
		return sqlScripts.get(index);
	}
	
	public void setSqlScripts(ArrayList<SqlScript> sqlScripts) {
		this.sqlScripts=sqlScripts;
	}

	public void setConvertedSqlScriptBlocks(ArrayList<SqlBlock> sqlBlocks) {
		this.sqlConvertedScriptBlocks=sqlBlocks;
		notifyListeners(SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED);
	}
	
	public StringBuffer getConvertedSqlScriptCharactersAsStringBuffer() {
		StringBuffer sbConvertedScripts=new StringBuffer();
		sbConvertedScripts.append("-- "+SqlRepeatablerModel.PROGRAMNAME+" "+SqlRepeatablerModel.getProgramVersion()+System.lineSeparator());
		for(int i=0;i<sqlConvertedScriptBlocks.size();i++) {
			SqlBlock sqlBlock=sqlConvertedScriptBlocks.get(i);
			sbConvertedScripts.append(System.lineSeparator());
			sbConvertedScripts.append("-- Original: "+sqlInputFiles.get(i).getName());
			sbConvertedScripts.append(System.lineSeparator());
			sbConvertedScripts.append(sqlBlock.toString());
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
		notifyListeners(SimpleEvents.MODEL_FRAMESIZECHANGED);
	}

	public Rectangle getFrameBounds() {
		return frameBounds;
	}

	public void reload() {
		notifyListeners(SimpleEvents.MODEL_SQLINPUTFILESCHANGED);
	}

	public void reset() {
		sqlInputFiles=new ArrayList<>();
		sqlConvertedScriptBlocks=new ArrayList<>();
		notifyListeners(SimpleEvents.MODEL_RESET);
		notifyListeners(SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED);
	}

	public void fireLogEvent(ILoggingEvent loggingEvent) {
		notifyListeners(loggingEvent);
	}

	public void clearInputPanel() {
		notifyListeners(SimpleEvents.MODEL_CLEARINPUTPANEL);
	}

}
