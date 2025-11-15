package lunartools.sqlrepeatabler.common;

public class BackgroundColorProvider {
	public static final String ERROR="ff2222";
	private final String[] colors= {
			"eeeeee",
			"eeffff",
			"ffeeff",
			"ffffee",
			"eeeeff",
			"ffeeee",
			"eeffee",

			"dddddd",
			"ddffff",
			"ffddff",
			"ffffdd",
			"ddddff",
			"ffdddd",
			"ddffdd",

			"ddeeff",
			"eeddff",
			"eeffdd",

			"ddffee",
			"ffddee",
			"ffeedd",

			"ddeeee",
			"eeddee",
			"eeeedd",
			"ddddee",
			"eedddd",
			"ddeedd"

	};

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
		return colors[primaryIndex++];
	}

	public synchronized String getNextSecondaryColor() {
		return colors[--secondaryIndex];
	}

	public void reset(){
		primaryIndex=0;
		secondaryIndex=colors.length;
	}
}
