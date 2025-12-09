package lunartools.sqlrepeatabler.settings;

import java.util.HashMap;
import java.util.Map;

public enum Theme {
	LIGHT(		"FlatLightLaf",		"Light",	false),
	DARK(		"FlatDarkLaf",		"Dark",		true),
	INTELLIJ(	"FlatIntelliJLaf",	"IntelliJ",	false),
	DARCULA(	"FlatDarculaLaf",	"Darcula",	true);

	private final String label;
	private final String key;
	private final boolean isDark;

	private static final Map<String, Theme> BY_KEY = new HashMap<>();

	Theme(String key,String label, boolean isDark) {
		this.key = key;
		this.label = label;
		this.isDark=isDark;
	}

	static {
		for (Theme theme : values()) {
			BY_KEY.put(theme.key, theme);
		}
	}

	public String getKey() {
		return key;
	}

	public String getLabel() {
		return label;
	}

	public boolean isDark() {
		return isDark;
	}

	public static Theme fromKey(String key) {
		return BY_KEY.get(key);
	}
}

