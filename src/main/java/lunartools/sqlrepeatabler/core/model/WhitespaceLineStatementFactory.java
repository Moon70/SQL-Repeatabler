package lunartools.sqlrepeatabler.core.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhitespaceLineStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(WhitespaceLineStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().length()==0;
	}

	@Override
	public Statement createStatement(SqlScript sqlScript){
		if(!match(sqlScript.peekLineAsString())) {
			throw new RuntimeException("Illegal factory call");
		}
		logger.debug("Statement: whitespace line");
		int linecount=1;
		String line;
		while((line=sqlScript.readNextLineAsString())!=null) {
			if(!match(line)) {
				break;
			}
			linecount++;
		}
		return new WhitespaceLineStatement(linecount);
	}

}
