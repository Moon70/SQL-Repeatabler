package lunartools.sqlrepeatabler.parser;

public class SqlCharacter {
	public static final SqlCharacter SEPARATOR=new SqlCharacter(' ',-1,-1,-1);
	private char c;
	private int row;
	private int column;
	private int indexInFile;
	private Category category=Category.UNCATEGORIZED;
	
	public SqlCharacter(char c, int row, int column, int indexInFile) {
		this.c=c;
		this.row=row;
		this.column=column;
		this.indexInFile=indexInFile;
	}
    
    public SqlCharacter(char c, int row, int column, int indexInFile,Category category) {
        this.c=c;
        this.row=row;
        this.column=column;
        this.indexInFile=indexInFile;
        this.category=category;
    }
    
    public SqlCharacter(char c,Category category) {
        this(c,-1,-1,-1);
        this.category=category;
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
		return indexInFile;
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
	
}
