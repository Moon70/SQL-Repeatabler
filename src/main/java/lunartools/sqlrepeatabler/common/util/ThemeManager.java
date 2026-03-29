package lunartools.sqlrepeatabler.common.util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.ChangeListenerSupport;
import lunartools.sqlrepeatabler.common.model.SimpleEvents;
import lunartools.sqlrepeatabler.common.ui.Theme;
import lunartools.sqlrepeatabler.core.model.Category;
import lunartools.sqlrepeatabler.infrastructure.config.Settings;

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
					"606060",//00
					"605050",//01
					"506050",//02
					"505060",//03
					"606050",//04
					"506060",//05
					"605060",//06
					"707070",//07
					"705050",//08
					"507050",//09
					"505070",//10
					"707050",//11
					"507070",//12
					"705070",//13
					"506070",//14
					"706060",//15
					"607060",//16
					"705060",//17
					"507060",//18
					"706050",//19
					"607050",//20
					"605070",//21
					"707060",//22
					"606070",//23
					"607070",//24
					"706070"//25
			};
		}else {
			backgroundColors=new String[]{
					"e8e8e8",//00
					"eeffff",//01
					"ffeeff",//02
					"ffffee",//03
					"eeeeff",//04
					"ffeeee",//05
					"eeffee",//06
					"e2e2e2",//07
					"ddffff",//08
					"ffddff",//09
					"ffffdd",//10
					"ffddee",//11
					"ddddff",//12
					"ddeedd",//13
					"eeddff",//14
					"ddeeee",//15
					"ddffdd",//16
					"eeddee",//17
					"ddeeff",//18
					"eeeedd",//19
					"ffdddd",//20
					"ddddee",//21
					"eeffdd",//22
					"eedddd",//23
					"ddffee",//24
					"ffeedd"//25
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
