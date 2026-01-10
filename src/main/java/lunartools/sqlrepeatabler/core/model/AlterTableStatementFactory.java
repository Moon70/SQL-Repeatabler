package lunartools.sqlrepeatabler.core.model;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.core.processing.StatementTokenizer;

public class AlterTableStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(AlterTableStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().toUpperCase().startsWith(AlterTableStatement.COMMAND);
	}

	@Override
	public Statement createStatement(SqlScript sqlScript) throws SqlParserException{
		if(!match(sqlScript.peekLineAsString())) {
			throw new RuntimeException("Illegal factory call");
		}

		StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
		logger.debug("Statement: "+statementTokenizer.toString());
		statementTokenizer.setBackgroundColor(null);

		Token tokenStatement=statementTokenizer.nextToken(AlterTableStatement.COMMAND);
		tokenStatement.setCategory(Category.STATEMENT);
		tokenStatement=tokenStatement.toUpperCase();

		TableName tableName=TableName.createInstanceByConsuming(statementTokenizer);
		logger.debug("Table: "+tableName.toString());

		statementTokenizer.stripWhiteSpaceLeft();

		ArrayList<AlterTableAction> alterTableActions=null;
		if(statementTokenizer.consumeCommandIgnoreCaseAndSpace("ADD")) {
			alterTableActions=parseAddAction(statementTokenizer);
		}else if(statementTokenizer.consumeCommandIgnoreCaseAndSpace("DROP")) {
			alterTableActions=parseDropAction(statementTokenizer);
		}else if(statementTokenizer.consumeCommandIgnoreCaseAndSpace("ALTER COLUMN")) {
			alterTableActions=parseAlterColumnAction(statementTokenizer);
		}else if(statementTokenizer.startsWithIgnoreCase("MODIFY COLUMN")) {
			logger.warn(String.format("It smells like MySql! 'MODIFY COLUMN' is not supported in T-SQL, processing as 'ALTER COLUMN'. %s",statementTokenizer.getLocation()) );
			statementTokenizer.nextToken("MODIFY").setCategory(Category.IGNORED);
			statementTokenizer.stripWhiteSpaceLeft();
			statementTokenizer.consumeCommandIgnoreCaseAndSpace("COLUMN");
			alterTableActions=parseAlterColumnAction(statementTokenizer);
		}else {
			Token token=statementTokenizer.nextToken();
			token.markError();
			throw new SqlParserException(String.format("Unsupported ALTER TABLE action found: %s",token),token.getLocation());
		}

		return new AlterTableStatement(tokenStatement,tableName,alterTableActions);
	}

	public ArrayList<AlterTableAction> parseAddAction(StatementTokenizer statementTokenizer) throws SqlParserException{
		logger.debug("Parsing ADD action: ");
		ArrayList<AlterTableAction> alterTableActions=new ArrayList<>();
		while(statementTokenizer.hasNext()) {
			if(beginsWithSupportedCommand(statementTokenizer)) {
				break;
			}else if(statementTokenizer.consumeCommandIgnoreCaseAndSpace("CONSTRAINT")) {
				Token tokenConstraintName=statementTokenizer.nextToken();
				tokenConstraintName.setCategory(Category.PARAMETER);
				if(statementTokenizer.consumeCommandIgnoreCaseAndSpace("FOREIGN KEY")) {
					Token tokenForeignKey=statementTokenizer.nextToken();
					if(!statementTokenizer.consumeCommandIgnoreCaseAndSpace("REFERENCES")) {
						throw new SqlParserException("'REFERENCES' keyword not found.",statementTokenizer.getLocation());
					}
					Token tokenReferencesTable=statementTokenizer.nextToken();
					Token tokenReferencesColumn=statementTokenizer.nextToken();
					AlterTableAction alterTableAction=new AddForeignKeyConstraintAction(tokenConstraintName,tokenForeignKey,tokenReferencesTable,tokenReferencesColumn);
					alterTableActions.add(alterTableAction);
				}else if(statementTokenizer.consumeCommandIgnoreCaseAndSpace("UNIQUE")) {
					Token tokenReferencesColumn=statementTokenizer.nextToken();
					AlterTableAction alterTableAction=new AddUniqueConstraintAction(tokenConstraintName,tokenReferencesColumn);
					alterTableActions.add(alterTableAction);
				}else {
					throw new SqlParserException("Neither 'FOREIGN KEY' nor 'UNIQUE' keyword found",statementTokenizer.getLocation());
				}
			}else {
				if(statementTokenizer.startsWithIgnoreCase("COLUMN")){
					logger.warn(String.format("It smells like MySql! Ignoring COLUMN keyword which is not allowed in T-SQL. %s", statementTokenizer.getLocation()));
					Token token=statementTokenizer.nextToken();
					token.setCategory(Category.IGNORED);
				}
				Token tokenColumName=statementTokenizer.nextToken().setCategory(Category.COLUMN);
				Token tokenColumParameter=statementTokenizer.nextTokenUntil(',').setCategory(Category.COLUMNPARAMETER);

				AlterTableAction alterTableAction=new AddColumnAction(tokenColumName,tokenColumParameter);
				alterTableActions.add(alterTableAction);
				if(statementTokenizer.startsWithIgnoreCase("ADD")) {
					logger.warn("Ignoring illegal ADD command. In T-SQL, when adding multiple columns in a single ALTER TABLE...ADD statement, only use one ADD keyword!"+statementTokenizer.getLocation().toString());
					Token tokenIllegal=statementTokenizer.nextToken();
					tokenIllegal.setCategory(Category.IGNORED);
				}
			}
		}
		return alterTableActions;
	}

	public ArrayList<AlterTableAction> parseDropAction(StatementTokenizer statementTokenizer) throws SqlParserException{
		logger.debug("Parsing DROP action");
		ArrayList<AlterTableAction> alterTableActions=new ArrayList<>();
		Token tokenTarget=statementTokenizer.nextToken();
		if(tokenTarget.toString().equalsIgnoreCase("COLUMN")) {
			while(true) {
				Token tokenColumnName=statementTokenizer.nextToken();
				AlterTableAction alterTableAction=new DropColumnAction(tokenColumnName);
				alterTableActions.add(alterTableAction);
				if(!statementTokenizer.hasNext()) {
					break;
				}
				statementTokenizer.consumePrefixIgnoreCaseAndSpace(",");
			}
		}else if(tokenTarget.toString().equalsIgnoreCase("CONSTRAINT")) {
			Token tokenConstraintName=statementTokenizer.nextToken();
			AlterTableAction alterTableAction=new DropConstraintAction(tokenConstraintName);
			alterTableActions.add(alterTableAction);
		}else {
			throw new SqlParserException(String.format("DROP target not supported: %s",tokenTarget),tokenTarget.getLocation());
		}
		return alterTableActions;
	}

	public ArrayList<AlterTableAction> parseAlterColumnAction(StatementTokenizer statementTokenizer) throws SqlParserException{
		logger.debug("Parsing ALTER COLUMN action");
		ArrayList<AlterTableAction> alterTableActions=new ArrayList<>();
		Token tokenColumnName=statementTokenizer.nextToken();
		Token tokenColumnParameter=statementTokenizer.nextTokenUntil(',');
		AlterTableAction alterTableAction=new AlterColumnAction(tokenColumnName,tokenColumnParameter);
		alterTableActions.add(alterTableAction);
		if(statementTokenizer.hasNext()) {
			Token token=statementTokenizer.nextToken();
			throw new SqlParserException(String.format("Unexpected content: %s",token),token.getLocation());
		}
		return alterTableActions;
	}

	private boolean beginsWithSupportedCommand(StatementTokenizer statementTokenizer) {
		return 
				statementTokenizer.startsWithIgnoreCase("ADD") ||
				statementTokenizer.startsWithIgnoreCase("DROP") ||
				statementTokenizer.startsWithIgnoreCase("ALTER COLUMN") ||
				statementTokenizer.startsWithIgnoreCase("MODIFY COLUMN" //MySql, invalid for T-SQL, is interpreted as 'ALTER COLUMN'
						) ;
	}

}
