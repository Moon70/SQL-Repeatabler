package lunartools.sqlrepeatabler.core.model;

public class WhitespaceLineStatement implements Statement{
	private int numberOfEmptyLines;

	public WhitespaceLineStatement(int numberOfEmptyLines) {
		this.numberOfEmptyLines=numberOfEmptyLines;
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock){
		for(int i=0;i<numberOfEmptyLines;i++) {
			sqlBlock.add(SqlString.EMPTY_LINE);
		}
	}

}
