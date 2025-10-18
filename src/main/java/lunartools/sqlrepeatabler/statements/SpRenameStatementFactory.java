package lunartools.sqlrepeatabler.statements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.SqlScript;
import lunartools.sqlrepeatabler.util.SqlParserTools;

public class SpRenameStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(SpRenameStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().toUpperCase().startsWith(SpRenameStatement.COMMAND);
	}

	@Override
	public Statement createSqlSegment(SqlScript sqlScript) throws Exception{
		if(!match(sqlScript.peekLine())) {
			throw new Exception("Illegal factory call");
		}
		if(logger.isTraceEnabled()) {
			logger.trace("parsing statement");
		}

		StringBuilder sbStatement=sqlScript.consumeStatement();
		logger.info("statement: "+sbStatement.toString());
		sbStatement.delete(0, SpRenameStatement.COMMAND.length()+1);
		SqlParserTools.stripSpace(sbStatement);


		String source=consumeTokenIgnoreSpaceAndComma(sbStatement);
		String newName=consumeTokenIgnoreSpaceAndComma(sbStatement);
		String type=consumeTokenIgnoreSpaceAndComma(sbStatement);
		String tableName=extractTableNameFromSource(source);
		String oldName=extractOldNameFromSource(source);

		return new SpRenameStatement(tableName,oldName,newName,type);
	}

	private String extractOldNameFromSource(String source) {
		int start=source.lastIndexOf('.')+1;
		int end=source.length();
		if(source.charAt(end-1)=='\'') {
			end--;
		}
		return source.substring(start,end);
	}

	private String extractTableNameFromSource(String source) {
		int start=0;
		int end=source.lastIndexOf('.');
		if(source.charAt(0)=='\'') {
			start++;
		}
		return source.substring(start,end);
	}

}
