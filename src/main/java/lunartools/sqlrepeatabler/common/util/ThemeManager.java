package lunartools.sqlrepeatabler.common.util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.ChangeListenerSupport;
import lunartools.sqlrepeatabler.common.model.SimpleEvents;
import lunartools.sqlrepeatabler.common.ui.Theme;
import lunartools.sqlrepeatabler.infrastructure.config.Settings;
import lunartools.sqlrepeatabler.parser.Category;

public class ThemeManager implements ChangeListenerSupport{
	private static Logger logger = LoggerFactory.getLogger(ThemeManager.class);
	private static ThemeManager instance;
	private final List<ChangeListener> listeners = new CopyOnWriteArrayList<>();


	private String htmlIntro;
	private String htmlOutro;
	private String[] backgroundColors;

	public static ThemeManager getInstance() {
		if(instance==null) {
			instance=new ThemeManager();
		}
		return instance;
	}

	private ThemeManager() {
		prepareThemeData(Settings.getInstance().getTheme());
	}

	@Override
	public List<ChangeListener> getListeners() {
		return listeners;
	}

	public Theme getTheme() {
		return Settings.getInstance().getTheme();
	}

	public void applyTheme(Theme theme) {
		Settings settings=Settings.getInstance();
		if(settings.getTheme()!=theme) {
			logger.info(String.format("Theme changed: %s", theme.getLabel()));
			settings.setTheme(theme);
			prepareThemeData(theme);

			notifyListeners(SimpleEvents.THEMEMANAGER_THEME_CHANGED);
		}
	}

	private void prepareThemeData(Theme theme) {
		calculateHtmlIntroAndOuttro(theme.isDark());
		if(theme.isDark()) {
			backgroundColors=new String[]{
					"606060",
					"605050",
					"506050",
					"505060",
					"606050",
					"506060",
					"605060",

					"707070",
					"705050",
					"507050",
					"505070",
					"707050",
					"507070",
					"705070",

					"706050",
					"607050",
					"605070",

					"705060",
					"507060",
					"506070",

					"706060",
					"607060",
					"606070",
					"707060",
					"607070",
					"706070"
			};
		}else {
			backgroundColors=new String[]{
					"e8e8e8",
					"eeffff",
					"ffeeff",
					"ffffee",
					"eeeeff",
					"ffeeee",
					"eeffee",

					"d8d8d8",
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
		}
	}

	private void calculateHtmlIntroAndOuttro(boolean isDarkTheme) {
		StringBuilder sb=new StringBuilder();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<meta charset=\"UTF-8\">");
		sb.append("<style>");
		sb.append("body {font-family: \"Courier New\", Courier, monospace;}");
		for(Category category:Category.values()) {
			sb.append(category.getCss(isDarkTheme));
		}
		sb.append("</style>");
		sb.append("</head>");
		sb.append("<body>");
		htmlIntro=sb.toString();

		sb=new StringBuilder();
		sb.append("</body>");
		sb.append("</html>");
		htmlOutro=sb.toString();
	}

	public String getHtmlIntro() {
		return htmlIntro;
	}

	public String getHtmlOutro() {
		return htmlOutro;
	}

	public String[] getBackgroundColors() {
		return backgroundColors;
	}

}
