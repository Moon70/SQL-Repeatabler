package lunartools.sqlrepeatabler.statements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.parser.SqlScript;

public class OneLineCommentStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(OneLineCommentStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().startsWith(OneLineCommentStatement.COMMAND);
	}

	@Override
	public Statement createStatement(SqlScript sqlScript) throws Exception{
		if(!match(sqlScript.readLine())) {
			throw new Exception("Illegal factory call");
		}
		if(logger.isTraceEnabled()) {
			logger.trace("parsing statement");
		}

		logger.info("statement: comment");
		int endIndex=sqlScript.getIndex();
		int startIndex=endIndex-1;
		String line;
		while((line=sqlScript.peekLineAsString())!=null) {
			if(!match(line)){
				break;
			}
			endIndex++;
			sqlScript.readLine();
		}
		return new OneLineCommentStatement(sqlScript,startIndex,endIndex);
	}

}
