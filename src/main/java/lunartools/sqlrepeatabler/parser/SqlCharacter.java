package lunartools.sqlrepeatabler.parser;

public class SqlCharacter {
	public static final SqlCharacter SEPARATOR=new SqlCharacter(' ',-1,-1,-1);
	private char c;
	private int row;
	private int column;
	private int indexInFile;
	
	public SqlCharacter(char c, int row, int column, int indexInFile) {
		this.c=c;
		this.row=row;
		this.column=column;
		this.indexInFile=indexInFile;
	}
	
	public char getChar() {
		return c;
	}

	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	public int getIndexInFile() {
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
}
