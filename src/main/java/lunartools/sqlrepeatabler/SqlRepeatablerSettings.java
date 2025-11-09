package lunartools.sqlrepeatabler;

import java.awt.Dimension;
import java.awt.Rectangle;

import lunartools.Settings;
import lunartools.SwingTools;
import lunartools.sqlrepeatabler.gui.SqlRepeatablerView;

public class SqlRepeatablerSettings extends Settings{
	private static SqlRepeatablerSettings instance;

	public static final String VIEW_BOUNDS =		"ViewBounds";
	public static final String FILE_LOADFOLDER =	"LoadFolder";
	public static final String FILE_SAVEFOLDER =	"SaveFolder";

	public static SqlRepeatablerSettings getInstance() {
		if(instance==null) {
			instance=new SqlRepeatablerSettings(SqlRepeatablerModel.PROGRAMNAME,SqlRepeatablerModel.getProgramVersion());
		}
		return instance;
	}

	private SqlRepeatablerSettings(String programName, String version) {
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

}
