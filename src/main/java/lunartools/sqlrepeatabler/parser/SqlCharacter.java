package lunartools.sqlrepeatabler.parser;

public class SqlCharacter {
	public static final SqlCharacter SEPARATOR=new SqlCharacter(' ');
	private char c;
	private int row;
	private int column;
	private int index;
	private Category category=Category.UNCATEGORIZED;
	private String backgroundColor;
	
	public SqlCharacter(char c, int row, int column, int index) {
		this.c=c;
		this.row=row;
		this.column=column;
		this.index=index;
	}
    
    public SqlCharacter(char c, int row, int column, int indexInFile,Category category) {
        this.c=c;
        this.row=row;
        this.column=column;
        this.index=indexInFile;
        this.category=category;
    }
    
    public SqlCharacter(char c,Category category) {
        this(c,-1,-1,-1);
        this.category=category;
    }
    
    public SqlCharacter(char c) {
        this(c,-1,-1,-1);
    }
	
	public char getChar() {
		return c;
	}

	public int getRow() {
		return row;
	}
	
	public int getColumn() {
	    return column;
	}

	public int getIndex() {
		return index;
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
	
}
