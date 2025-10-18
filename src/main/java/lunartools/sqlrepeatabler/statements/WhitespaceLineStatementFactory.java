package lunartools.sqlrepeatabler.statements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.parser.SqlScript;

public class WhitespaceLineStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(WhitespaceLineStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().length()==0;
	}

	@Override
	public Statement createSqlSegment(SqlScript sqlScript) throws Exception{
		if(!match(sqlScript.peekLine())) {
			throw new Exception("Illegal factory call");
		}
		if(logger.isTraceEnabled()) {
			logger.trace("parsing statement");
		}

		logger.info("statement: whitespace");
		int linecount=1;
		String line;
		while((line=sqlScript.readNextLine())!=null) {
			if(!match(line)) {
				break;
			}
			linecount++;
		}
		return new WhitespaceLineStatement(linecount);
	}

}
