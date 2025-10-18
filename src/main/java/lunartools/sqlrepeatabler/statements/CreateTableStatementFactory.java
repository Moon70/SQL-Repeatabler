package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.SqlScript;
import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.segments.TableSegment;
import lunartools.sqlrepeatabler.util.SqlParserTools;

public class CreateTableStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(CreateTableStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().toUpperCase().startsWith(CreateTableStatement.COMMAND);
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
		sbStatement.delete(0, CreateTableStatement.COMMAND.length()+1);
		SqlParserTools.stripSpace(sbStatement);

		TableName tableName=TableName.createInstanceByConsuming(sbStatement);
		logger.debug(tableName.toString());

		ArrayList<TableSegment> tableElements=new ArrayList<>();
		SqlParserTools.stripUntil(sbStatement,'(');
		sbStatement.deleteCharAt(0);
		SqlParserTools.stripSpace(sbStatement);
		int p=-1;
		while((p=SqlParserTools.indexOfNotInLiteral(sbStatement, ','))!=-1) {
			String s=sbStatement.substring(0, p);
			sbStatement.delete(0, p+1);
			SqlParserTools.stripSpace(sbStatement);
			tableElements.add(new TableSegment(s));
		}
		p=SqlParserTools.lastIndexOfNotInLiteral(sbStatement, ')');
		String s=sbStatement.substring(0, p).trim();
		sbStatement.delete(0, p+1);
		SqlParserTools.stripSpace(sbStatement);
		tableElements.add(new TableSegment(s));

		return new CreateTableStatement(tableName,tableElements);
	}

}
