package lunartools.sqlrepeatabler.core.model;

public class SqlParserException extends Exception{
	private CharacterLocation characterLocation;

	public SqlParserException(String message, CharacterLocation characterLocation) {
		super(message);
		this.characterLocation=characterLocation;
	}

	@Override
	public String getMessage() {
		return String.format("%s (location: row=%d, column=%d, index=%d)",super.getMessage(),characterLocation.getRow()+1,characterLocation.getColumn()+1,characterLocation.getIndex());
	}

}
