package lunartools.sqlrepeatabler.core.model;

import java.io.EOFException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.core.processing.StatementTokenizer;

public class MultiLineCommentStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(MultiLineCommentStatementFactory.class);

	@Override
	public Statement createStatement(StatementTokenizer statementTokenizer) throws EOFException{
        if(!statementTokenizer.startsWithIgnoreCase(MultiLineCommentStatement.COMMAND)){
            return null;
        }
        logger.debug("Statement: multiline comment");
        
        ArrayList<SqlString> commentLines=new ArrayList<>();

        while(true) {
            if(statementTokenizer.size()==0) {
                throw new EOFException("Multiline comment not closed");
            }
            SqlString sqlScriptLine=statementTokenizer.consumeLine();
            commentLines.add(sqlScriptLine);
            sqlScriptLine.setCategory(Category.COMMENT);
            if(sqlScriptLine.endsWithIgnoreCase("*/")) {
                break;
            }
        }

		return new MultiLineCommentStatement(commentLines);
	}

}
