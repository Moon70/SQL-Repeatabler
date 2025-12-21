package lunartools.sqlrepeatabler.common.ui;

public enum Icons {
	CLOSE		("tab_close_24px.svg"),
	COPY		("content_copy_24px.svg"),
	EXIT		("logout_24px.svg"),
	FILE_OPEN	("file_open_24px.svg"),
	REFRESH		("refresh_24px.svg"),
	RESTART		("restart_alt_24px.svg"),
	SAVE_AS		("save_as_24px.svg"),
	SORT		("sort_24px.svg");

	private static final String FOLDER="icons/material-symbols/";
	private final String fileName;

	Icons(String fileName) {
		this.fileName=fileName;
	}

	public String getPath() {
		return FOLDER.concat(fileName);
	}

}
