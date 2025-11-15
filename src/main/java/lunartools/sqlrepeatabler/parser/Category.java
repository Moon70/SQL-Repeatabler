package lunartools.sqlrepeatabler.parser;

public enum Category {
	//				color		weight		style		decoration
	UNCATEGORIZED(	"#808080",	null,		null,		null),
	IGNORED(		"#808080",	null,		null,		"line-through"),
	STATEMENT(		"#0000ff",	"bold",		null,		null),
	TABLE(			"#b22222",	"bold",		null,		null),
	COMMAND(		"#005500",	"bold",		null,		null),
	COLUMN(			"#FF8C00",	"bold",		null,		null),
	COLUMNPARAMETER("#2a895",	null,		null,		null),
	COMMENT(		"#008000",	null,		"italic",	null),
	PARAMETER(		"#8B008B",	"bold",		null,		null),
	INSERTED(		"#4682B4",	"bold",		null,		null),
	ERROR(			"#222222",	"bold",		null,		null);
	
	private final String color;
	private final String fontWeight;
	private final String fontStyle;
	private final String textDecoration;
	
	private Category(String color, String fontWeight, String fontStyle, String textDecoration) {
		this.color=color;
		this.fontWeight=fontWeight;
		this.fontStyle=fontStyle;
		this.textDecoration=textDecoration;
	}

	public String getColor() {
		return color;
	}

	public String getFontWeight() {
		return fontWeight;
	}
	
	public String getCss() {
		StringBuilder sb=new StringBuilder();
		sb.append(".").append(this.toString()).append(" {");
		sb.append("color: ").append(this.color).append(";");
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
