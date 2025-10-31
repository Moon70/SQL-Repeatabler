package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import lunartools.sqlrepeatabler.parser.SqlParser;
import lunartools.sqlrepeatabler.parser.SqlString;

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

    @Override
    public void toSqlCharacters(ArrayList<SqlString> sqlCharacterLines) throws Exception {
        // TODO Auto-generated method stub
        
    }

}
