package lunartools.sqlrepeatabler.statements;

import lunartools.sqlrepeatabler.SqlParser;

public class WhitespaceLineStatement implements Statement{
	private int numberOfEmptyLines;

	public WhitespaceLineStatement(int numberOfEmptyLines) {
		this.numberOfEmptyLines=numberOfEmptyLines;
	}

	public void toSql(StringBuilder sb) {
		for(int i=0;i<numberOfEmptyLines;i++) {
			sb.append(SqlParser.CRLF);
		}
	}

}
