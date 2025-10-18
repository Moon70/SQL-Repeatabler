package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.util.SqlParserTools;

public class InsertIntoStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(InsertIntoStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().toUpperCase().startsWith(InsertIntoStatement.COMMAND);
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
		sbStatement.delete(0, InsertIntoStatement.COMMAND.length()+1);
		SqlParserTools.stripSpace(sbStatement);

		TableName tableName=TableName.createInstanceByConsuming(sbStatement);
		logger.debug(tableName.toString());

		String columnNames=SqlParserTools.consumeTokensInParenthesis(sbStatement);

		if(!SqlParserTools.consumePrefixIgnoreCaseAndSpace(sbStatement, "VALUES")) {
			throw new Exception("Keyword VALUES not found");
		}

		ArrayList<String> columnValuesList=new ArrayList<>();
		while(sbStatement.length()>0 && sbStatement.charAt(0)!=';') {
			if(sbStatement.charAt(0)==',') {
				sbStatement.deleteCharAt(0);
			}
			String columnValues=SqlParserTools.consumeTokensInParenthesis(sbStatement);
			columnValuesList.add(columnValues);
		}

		return new InsertIntoStatement(tableName,columnNames,columnValuesList);
	}

}
