package lunartools.sqlrepeatabler.parser;

public class SqlCharacter {
	public static final SqlCharacter SEPARATOR=new SqlCharacter(' ');
	private char c;
	private CharacterLocation characterLocation;
	private Category category=Category.UNCATEGORIZED;
	private String backgroundColor;
	
	
	public SqlCharacter(char c) {
		this.c=c;
	}
	
	public SqlCharacter(char c, CharacterLocation characterLocation) {
		this(c);
		this.characterLocation=characterLocation;
	}
	
    public SqlCharacter(char c, CharacterLocation characterLocation, Category category) {
    	this(c,characterLocation);
        this.category=category;
    }
    
    public SqlCharacter(char c,Category category) {
		this(c);
        this.category=category;
    }
	
	public char getChar() {
		return c;
	}

	public CharacterLocation getCharacterLocation() {
		return characterLocation;
	}
	
	public boolean isSpace() {
		return c==' ';
	}

    public boolean isWhiteSpace() {
        return c==' ' || c=='\t';
    }

	public boolean isSemicolon() {
		return c==';';
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setChar(char c) {
		this.c=c;
	}
	
	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	@Override
	public String toString() {
		return String.format("'%s', %s, %s",c,(int)c,characterLocation.toString());
	}
}
