package lunartools.sqlrepeatabler.core.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.core.processing.StatementTokenizer;

public class CreateTableStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(CreateTableStatementFactory.class);
	private static final List<String> CONSTRAINTS = List.of(
			"PRIMARY",
			"UNIQUE",
			"FOREIGN",
			"CHECK",
			"CONSTRAINT"
			);

	@Override
	public Statement createStatement(StatementTokenizer statementTokenizer) throws SqlParserException{
		if(!statementTokenizer.startsWithIgnoreCase(CreateTableStatement.COMMAND)) {
			return null;
		}
		logger.debug("Statement: "+statementTokenizer.toString());

		statementTokenizer.setBackgroundColor(null);

		Token tokenStatement=statementTokenizer.nextToken(CreateTableStatement.COMMAND);
		tokenStatement.setCategory(Category.STATEMENT);
		tokenStatement=tokenStatement.toUpperCase();

		TableName tableName=TableName.createInstanceByConsuming(statementTokenizer);
		logger.debug("Table: "+tableName.toString());

		ArrayList<Token> tokens=new ArrayList<>();

		Token allCollumnsToken=statementTokenizer.nextToken('(',')');
		allCollumnsToken.removeEnclosing('(',')');
		Token[] columns=allCollumnsToken.split(',');
		for(int i=0;i<columns.length;i++) {
			columns[i].fixMySql();
			Token[] columnTokens=columns[i].split(' ');
			if(!CONSTRAINTS.contains(columnTokens[0].toString().toUpperCase())) {
				verifyColumnName(columnTokens[0]);
			}
			tokens.add(columns[i]);
		}

		Token tokenEngineParameters=statementTokenizer.nextTokenUntil(';');
		if(tokenEngineParameters!=null) {
			tokenEngineParameters.setCategory(Category.IGNORED);
		}
		return new CreateTableStatement(tokenStatement,tableName,tokens);
	}

}
