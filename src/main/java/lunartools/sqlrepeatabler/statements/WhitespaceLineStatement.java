package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import lunartools.sqlrepeatabler.parser.SqlString;

public class WhitespaceLineStatement implements Statement{
	private int numberOfEmptyLines;

	public WhitespaceLineStatement(int numberOfEmptyLines) {
		this.numberOfEmptyLines=numberOfEmptyLines;
	}

	@Override
	public void toSqlCharacters(ArrayList<SqlString> sqlCharacterLines) throws Exception {
		for(int i=0;i<numberOfEmptyLines;i++) {
			sqlCharacterLines.add(SqlString.EMPTY_LINE);
		}
	}

}
