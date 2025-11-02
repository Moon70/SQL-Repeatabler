package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.parser.StatementTokenizer;
import lunartools.sqlrepeatabler.parser.Token;
import lunartools.sqlrepeatabler.segments.TableSegment;

public class CreateTableStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(CreateTableStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().toUpperCase().startsWith(CreateTableStatement.COMMAND);
	}

	@Override
	public Statement createStatement(SqlScript sqlScript) throws Exception{
		if(!match(sqlScript.peekLineAsString())) {
			throw new Exception("Illegal factory call");
		}
		if(logger.isTraceEnabled()) {
			logger.trace("parsing statement");
		}

		StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
		logger.info("statement: "+statementTokenizer.toString());

		statementTokenizer.nextToken().setCategory(Category.STATEMENT);//skip 'CREATE' token	
		statementTokenizer.nextToken().setCategory(Category.STATEMENT);//skip 'TABLE' token

		TableName tableName=TableName.createInstanceByConsuming(statementTokenizer);
		if(tableName.isMySql()) {
			logger.warn("Script is most likely MySql format! Replacing backticks with square brackets!");
		}
		logger.debug(tableName.toString());

		ArrayList<TableSegment> tableElements=new ArrayList<>();

		Token allCollumnsToken=statementTokenizer.nextToken('(',')');
		allCollumnsToken.removeEnclosing('(',')');
		Token[] columns=allCollumnsToken.split(',');
		for(int i=0;i<columns.length;i++) {
			if(tableName.isMySql()) {
				columns[i].fixMySqlDelimiter();
			}
			tableElements.add(new TableSegment(columns[i]));
		}

		return new CreateTableStatement(tableName,tableElements);
	}

}
