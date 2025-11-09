package lunartools.sqlrepeatabler.statements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.parser.SqlString;

public class MultiLineCommentStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(MultiLineCommentStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().startsWith(MultiLineCommentStatement.COMMAND);
	}

	@Override
	public Statement createStatement(SqlScript sqlScript) throws Exception{
        SqlString sqlScriptLine=sqlScript.readLine();
		String line=sqlScriptLine.toString();
		if(!match(line)) {
			throw new Exception("Illegal factory call");
		}
		if(logger.isTraceEnabled()) {
			logger.trace("parsing statement");
		}

		sqlScriptLine.setCategory(Category.COMMENT);
		logger.info("Statement: comment");
		int endIndex=sqlScript.getIndex();
		int startIndex=endIndex-1;
		if(!line.endsWith("*/")) {
			while(true) {
			    sqlScriptLine=sqlScript.readLine();
		        sqlScriptLine.setCategory(Category.COMMENT);
			    line=sqlScriptLine.toString();
				
				if(line==null) {
					throw new Exception("Unexpected end of data");
				}
				endIndex++;
				if(line.endsWith("*/")) {
					break;
				}
			}
		}
		return new MultiLineCommentStatement(sqlScript,startIndex,endIndex);
	}

}
