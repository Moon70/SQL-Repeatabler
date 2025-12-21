package lunartools.sqlrepeatabler.common.ui;

public class BackgroundColorProvider {
	public static final String WARN="ffaa22";
    public static final String ERROR="ff4444";

	private static BackgroundColorProvider instance;
	private int primaryIndex;
	private int secondaryIndex;
	private int numberOfColorsUsed;

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
		numberOfColorsUsed++;
		return ThemeManager.getInstance().getBackgroundColors()[primaryIndex++];
	}

	public synchronized String getNextSecondaryColor() {
		numberOfColorsUsed++;
		return ThemeManager.getInstance().getBackgroundColors()[--secondaryIndex];
	}

	public int getNumberOfColorsUsed() {
		return numberOfColorsUsed;
	}

	public void reset(){
		primaryIndex=numberOfColorsUsed=0;
		secondaryIndex=ThemeManager.getInstance().getBackgroundColors().length;
	}
}
