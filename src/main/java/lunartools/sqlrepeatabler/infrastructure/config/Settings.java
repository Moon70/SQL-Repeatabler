package lunartools.sqlrepeatabler.infrastructure.config;

import java.awt.Dimension;
import java.awt.Rectangle;

import lunartools.AbstractSettings;
import lunartools.SwingTools;
import lunartools.sqlrepeatabler.main.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.main.SqlRepeatablerView;

public class Settings extends AbstractSettings{
	private static Settings instance;

	public static final String VIEW_BOUNDS =			"ViewBounds";
	public static final String FILE_LOADFOLDER =		"LoadFolder";
	public static final String FILE_SAVEFOLDER =		"SaveFolder";

	public static final String PROCESSING_ORDER =		"ProcessingOrder";
	public static final String DIVIDERLOCATION_CONSOLE=	"DividerLocationConsole";
	public static final String DIVIDERLOCATION_SCRIPT=	"DividerLocationScript";
	public static final String BACKGROUND_COLOR=		"BackgroundColor";
	public static final String THEME =					"Theme";

	public static Settings getInstance() {
		if(instance==null) {
			instance=new Settings(SqlRepeatablerModel.PROGRAMNAME,SqlRepeatablerModel.getProgramVersion());
		}
		return instance;
	}

	private Settings(String programName, String version) {
		super(programName, version);
	}

	public Dimension getDefaultViewSize() {
		return SqlRepeatablerView.MINIMUM_FRAME_SIZE;
	}

	public Rectangle getViewBounds() {
		if(!containsKey(VIEW_BOUNDS)) {
			return SwingTools.getBoundsForCenteredDimension(getDefaultViewSize());
		}
		Rectangle rectangleViewBounds=getRectangle(VIEW_BOUNDS);
		Rectangle fixedViewBounds=SwingTools.fixScreenBounds(rectangleViewBounds, getDefaultViewSize());

		return fixedViewBounds;
	}

	public ProcessingOrder getProcessingOrder() {
		String processingOrderString=getString(PROCESSING_ORDER, ProcessingOrder.ASADDED.getKey());
		ProcessingOrder processingOrder=ProcessingOrder.fromKey(processingOrderString);
		if(processingOrder==null) {
			return ProcessingOrder.ASADDED;
		}
		return processingOrder;
	}

	public void setProcessingOrder(ProcessingOrder processingOrder) {
		setString(PROCESSING_ORDER, processingOrder.getKey());
	}
	
	public Theme getTheme() {
		String themeString=getString(THEME, Theme.LIGHT.getKey());
		Theme theme=Theme.fromKey(themeString);
		if(theme==null) {
			return Theme.LIGHT;
		}
		return theme;
	}

	public void setTheme(Theme theme) {
		setString(THEME, theme.getKey());
	}

	public int getDividerlocationConsole() {
		return getInt(DIVIDERLOCATION_CONSOLE,0);
	}

	public void setDividerlocationConsole(int dividerlocationConsole) {
		setInt(DIVIDERLOCATION_CONSOLE, dividerlocationConsole);
	}

	public int getDividerlocationScript() {
		return getInt(DIVIDERLOCATION_SCRIPT,0);
	}

	public void setDividerlocationScript(int dividerlocationScript) {
		setInt(DIVIDERLOCATION_SCRIPT, dividerlocationScript);
	}

	public boolean isBackgroundColorEnabled() {
		String s=getString(BACKGROUND_COLOR,"true");
		return s.equalsIgnoreCase("true");
	}

	public void enableBackgroundColor(boolean isBackgroundColorEnabled) {
		setString(BACKGROUND_COLOR, isBackgroundColorEnabled?"true":"false");
	}

}
