package lunartools.sqlrepeatabler.statement;

import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlString;

public class WhitespaceLineStatement implements Statement{
	private int numberOfEmptyLines;

	public WhitespaceLineStatement(int numberOfEmptyLines) {
		this.numberOfEmptyLines=numberOfEmptyLines;
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock) throws Exception {
		for(int i=0;i<numberOfEmptyLines;i++) {
			sqlBlock.add(SqlString.EMPTY_LINE);
		}
	}

}
