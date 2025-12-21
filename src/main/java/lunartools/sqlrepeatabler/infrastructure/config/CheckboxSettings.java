package lunartools.sqlrepeatabler.infrastructure.config;

import java.util.HashMap;
import java.util.Map;

public enum CheckboxSettings {
	BACKGROUND_COLOR(		"backgroundColor",	"Background color");

	private final String label;
	private final String key;
	private static final Map<String, CheckboxSettings> BY_KEY = new HashMap<>();

	CheckboxSettings(String key,String label) {
		this.key = key;
		this.label = label;
	}

	static {
		for (CheckboxSettings s : values()) {
			BY_KEY.put(s.key, s);
		}
	}

	public String getKey() {
		return key;
	}

	public String getLabel() {
		return label;
	}

	public static CheckboxSettings fromKey(String key) {
		return BY_KEY.get(key);
	}
}

