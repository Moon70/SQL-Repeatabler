package lunartools.sqlrepeatabler.settings;

import java.util.HashMap;
import java.util.Map;

public enum ProcessingOrder {
	ASADDED(		"asAdded",			"as added"),
	CREATIONDATE(	"creationDate",		"by creation date"),
	ALPHABETICALLY(	"alphabetically",	"alphabetically");

	private final String label;
	private final String key;
	private static final Map<String, ProcessingOrder> BY_KEY = new HashMap<>();

	ProcessingOrder(String key,String label) {
		this.key = key;
		this.label = label;
	}

	static {
		for (ProcessingOrder s : values()) {
			BY_KEY.put(s.key, s);
		}
	}

	public String getKey() {
		return key;
	}

	public String getLabel() {
		return label;
	}

	public static ProcessingOrder fromKey(String key) {
		return BY_KEY.get(key);
	}
}

