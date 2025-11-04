package lunartools.sqlrepeatabler.statements;

import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlBlock;
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
	public void toSqlCharacters(SqlBlock sqlBlock) throws Exception {
		for(int i=startIndex;i<endIndex;i++) {
			SqlString sqlString=SqlString.createSqlStringFromString(sqlScript.getLineAt(i),Category.COMMENT);
			sqlBlock.add(sqlString);
		}
	}

}
