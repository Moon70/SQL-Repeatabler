package lunartools.sqlrepeatabler.core.model;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.core.processing.StatementTokenizer;

public class OneLineCommentStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(OneLineCommentStatementFactory.class);

	@Override
	public Statement createStatement(StatementTokenizer statementTokenizer){
        if(!statementTokenizer.startsWithIgnoreCase(OneLineCommentStatement.COMMAND)) {
            return null;
        }
        logger.debug("Statement: singleline comment");
        
        ArrayList<SqlString> commentLines=new ArrayList<>();

        while(statementTokenizer.startsWithIgnoreCase(OneLineCommentStatement.COMMAND)) {
            SqlString sqlScriptLine=statementTokenizer.consumeLine();
            commentLines.add(sqlScriptLine);
            sqlScriptLine.setCategory(Category.COMMENT);
        }
		return new OneLineCommentStatement(commentLines);
	}

}
