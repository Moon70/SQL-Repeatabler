package lunartools.sqlrepeatabler.statements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.util.SqlParserTools;

public class SetIdentityInsertStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(SetIdentityInsertStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().toUpperCase().startsWith(SetIdentityInsertStatement.COMMAND);
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
		sbStatement.delete(0, SetIdentityInsertStatement.COMMAND.length()+1);
		SqlParserTools.stripSpace(sbStatement);

		TableName tableName=TableName.createInstanceByConsuming(sbStatement);
		logger.debug(tableName.toString());

		SqlParserTools.stripSpaceRight(sbStatement);
		if(sbStatement.charAt(sbStatement.length()-1)==';') {
			sbStatement.deleteCharAt(sbStatement.length()-1);
		}
		String parameters=sbStatement.toString();

		return new SetIdentityInsertStatement(tableName,parameters);
	}

}
