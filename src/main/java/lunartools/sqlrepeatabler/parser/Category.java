package lunartools.sqlrepeatabler.parser;

public enum Category {
	UNCATEGORIZED("black",null),
	STATEMENT("red","bold"),
	TABLE("blue","bold"),
	COMMAND("orange","bold"),
	COLUMN("purple","bold"),
	COLUMNPARAMETER("#555555",null),
	COMMENT("green",null),
	PARAMETER("#643939","bold"),
	INSERTED("ffff88","bold");
	
	private final String color;
	private final String fontWeight;
//	private final
	
	private Category(String color, String fontWeight) {
		this.color=color;
		this.fontWeight=fontWeight;
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
		sb.append("}");
		return sb.toString();
	}
	
}
