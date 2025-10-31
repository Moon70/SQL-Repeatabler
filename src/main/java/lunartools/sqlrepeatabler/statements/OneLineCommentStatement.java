package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import lunartools.sqlrepeatabler.parser.SqlParser;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.parser.SqlString;

public class OneLineCommentStatement implements Statement{
	public static final String COMMAND="--";
	private SqlScript sqlScript;
	private int startIndex;
	private int endIndex;

	public OneLineCommentStatement(SqlScript sqlScript, int startIndex,int endIndex) {
		this.sqlScript=sqlScript;
		this.startIndex=startIndex;
		this.endIndex=endIndex;
	}

	@Override
	public void toSql(StringBuilder sb) {
		for(int i=startIndex;i<endIndex;i++) {
			sb.append(sqlScript.getLineAt(i));
			sb.append(SqlParser.CRLF);
		}
	}

    @Override
    public void toSqlCharacters(ArrayList<SqlString> sqlCharacterLines) throws Exception {
        // TODO Auto-generated method stub
        
    }

}
