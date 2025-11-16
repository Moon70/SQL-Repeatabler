package lunartools.sqlrepeatabler.settings;

import java.awt.Dimension;
import java.awt.Rectangle;

import lunartools.AbstractSettings;
import lunartools.SwingTools;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.gui.SqlRepeatablerView;

public class Settings extends AbstractSettings{
	private static Settings instance;

	public static final String VIEW_BOUNDS =		"ViewBounds";
	public static final String FILE_LOADFOLDER =	"LoadFolder";
	public static final String FILE_SAVEFOLDER =	"SaveFolder";

	public static final String PROCESSING_ORDER =	"ProcessingOrder";
	
	private ProcessingOrder processingOrder=ProcessingOrder.ASADDED;

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
		return processingOrder;
	}

	public void setProcessingOrder(ProcessingOrder processingOrder) {
		this.processingOrder = processingOrder;
	}

}
