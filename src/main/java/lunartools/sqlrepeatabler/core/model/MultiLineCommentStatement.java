package lunartools.sqlrepeatabler.core.model;

import java.util.ArrayList;

public class MultiLineCommentStatement implements Statement{
	public static final String COMMAND="/*";
	private ArrayList<SqlString> commentLines;

	public MultiLineCommentStatement(ArrayList<SqlString> commentLines) {
		this.commentLines=commentLines;
	}

	@Override
	public void toSqlCharacters(SqlBlock sqlBlock){
	    for(SqlString sqlString:commentLines) {
	        sqlBlock.add(sqlString);
	    }
	}

}
