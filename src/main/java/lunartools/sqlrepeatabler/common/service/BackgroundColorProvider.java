package lunartools.sqlrepeatabler.common.service;

import lunartools.sqlrepeatabler.common.util.ThemeManager;

public class BackgroundColorProvider {
	public static final String WARN="ffaa22";
    public static final String ERROR="ff4444";

	private static BackgroundColorProvider instance;
	private int primaryIndex;
	private int secondaryIndex;

	private BackgroundColorProvider() {
		reset();
	}

	public static BackgroundColorProvider getInstance() {
		if(instance==null) {
			instance=new BackgroundColorProvider();
		}
		return instance;
	}

	public synchronized String getNextPrimaryColor() {
	    String[] backgroundColors=ThemeManager.getInstance().getBackgroundColors();
	    int index=primaryIndex++;
	    primaryIndex=primaryIndex % backgroundColors.length;
		return backgroundColors[index];
	}

	public synchronized String getNextSecondaryColor() {
        String[] backgroundColors=ThemeManager.getInstance().getBackgroundColors();
        if(--secondaryIndex<0) {
            secondaryIndex+=backgroundColors.length;
        }
		return ThemeManager.getInstance().getBackgroundColors()[secondaryIndex];
	}

	public void reset(){
		primaryIndex=0;
		secondaryIndex=ThemeManager.getInstance().getBackgroundColors().length;
	}
}
