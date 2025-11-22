package lunartools.sqlrepeatabler.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.parser.SqlString;

public class OneLineCommentStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(OneLineCommentStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().startsWith(OneLineCommentStatement.COMMAND);
	}

	@Override
	public Statement createStatement(SqlScript sqlScript){
		SqlString sqlScriptLine=sqlScript.readLine();
		if(!match(sqlScriptLine.toString())) {
			throw new RuntimeException("Illegal factory call");
		}

		sqlScriptLine.setCategory(Category.COMMENT);
		logger.info("Statement: comment");
		int endIndex=sqlScript.getIndex();
		int startIndex=endIndex-1;
		while((sqlScriptLine=sqlScript.peekLine())!=null) {
			if(!match(sqlScriptLine.toString())){
				break;
			}
			sqlScriptLine.setCategory(Category.COMMENT);
			endIndex++;
			sqlScript.readLineAsString();
		}
		return new OneLineCommentStatement(sqlScript,startIndex,endIndex);
	}

}
