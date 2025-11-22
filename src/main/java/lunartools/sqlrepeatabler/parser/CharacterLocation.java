package lunartools.sqlrepeatabler.parser;

public class CharacterLocation {
	private int row;
	private int column;
	private int index;

	public CharacterLocation(int row, int column, int index) {
		this.row=row;
		this.column=column;
		this.index=index;
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

	@Override
	public String toString() {
		return String.format("Location: row=%d, column=%d, index=%d",row,column,index);
	}
}
