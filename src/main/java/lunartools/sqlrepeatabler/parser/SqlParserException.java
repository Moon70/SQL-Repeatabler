package lunartools.sqlrepeatabler.parser;

public class SqlParserException extends Exception{
	private int row;
	private int column;
	private int index;
	
	public SqlParserException(String message, int row, int column, int index) {
		super(message);
		this.row=row+1;
		this.column=column+1;
		this.index=index+1;
	}

    public SqlParserException(String message, SqlCharacter character) {
        super(message);
        if(character!=null) {
            this.row=character.getRow()+1;
            this.column=character.getColumn()+1;
            this.index=character.getIndexInFile()+1;
        }
    }

	@Override
	public String getMessage() {
		return String.format("%s (location: row=%d, column=%d, index=%d)",super.getMessage(),row,column,index);
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

}
