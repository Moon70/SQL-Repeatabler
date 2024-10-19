package lunartools.sqlrepeatabler;

import lunartools.Settings;

public class SqlRepeatablerSettings {
	public static final String VIEW_BOUNDS =		"ViewBounds";
	public static final String FILE_LOADFOLDER =	"LoadFolder";
	public static final String FILE_SAVEFOLDER =	"SaveFolder";

	private static Settings settings;

	public static Settings getSettings() {
		if(settings==null) {
			settings=new Settings(SqlRepeatablerModel.PROGRAMNAME,SqlRepeatablerModel.getProgramVersion());
		}
		return settings;
	}

}
