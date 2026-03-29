package lunartools.sqlrepeatabler.core.model;

public enum Category {
	//				colorLight	colorDark	weight		style		decoration
	UNCATEGORIZED(	"#808080",	"#CBCBCB",	null,		null,		null),
	IGNORED(		"#808080",	"#a0a0a0",	null,		null,		"line-through"),
	STATEMENT(		"#0000ff",	"#EEEEEE",	"bold",		null,		null),
	TABLE(			"#b22222",	"#FF90A0",	"bold",		null,		null),
	COMMAND(		"#005500",	"#40EDFF",	"bold",		null,		null),
	COLUMN(			"#FF8C00",	"#FFAC20",	"bold",		null,		null),
	COLUMNPARAMETER("#2a895",	"#92D2E7",	null,		null,		null),
	COMMENT(		"#008000",	"#59CC60",	null,		"italic",	null),
	PARAMETER(		"#8B008B",	"#EDD3B6",	"bold",		null,		null),
	INSERTED(       "#4682B4",  "#78A8D0",  "bold",     null,       null), 
	WARN(           "#222222",  "#222222",  "bold",     null,       null),
	ERROR(			"#222222",	"#000000",	"bold",		null,		null);

	private final String colorLight;
	private final String colorDark;
	private final String fontWeight;
	private final String fontStyle;
	private final String textDecoration;

	private Category(String colorLight, String colorDark, String fontWeight, String fontStyle, String textDecoration) {
		this.colorLight=colorLight;
		this.colorDark=colorDark;
		this.fontWeight=fontWeight;
		this.fontStyle=fontStyle;
		this.textDecoration=textDecoration;
	}

	public String getFontWeight() {
		return fontWeight;
	}

	public String getCss(boolean isDarkTheme) {
		StringBuilder sb=new StringBuilder();
		sb.append(".").append(this.toString()).append(" {");
		sb.append("color: ").append(isDarkTheme?colorDark:colorLight).append(";");
		if(this.fontWeight!=null) {
			sb.append("font-weight: ").append(this.fontWeight).append(";");
		}
		if(this.fontStyle!=null) {
			sb.append("font-style: ").append(this.fontStyle).append(";");
		}
		if(this.textDecoration!=null) {
			sb.append("text-decoration: ").append(this.textDecoration).append(";");
		}
		sb.append("}");
		return sb.toString();
	}

}
