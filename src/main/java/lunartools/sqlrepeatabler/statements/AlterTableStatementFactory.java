package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlParserException;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.parser.StatementTokenizer;
import lunartools.sqlrepeatabler.parser.Token;
import lunartools.sqlrepeatabler.segments.AddColumnAction;
import lunartools.sqlrepeatabler.segments.AddForeignKeyConstraintAction;
import lunartools.sqlrepeatabler.segments.AddUniqueConstraintAction;
import lunartools.sqlrepeatabler.segments.AlterColumnAction;
import lunartools.sqlrepeatabler.segments.DropColumnAction;
import lunartools.sqlrepeatabler.segments.DropConstraintAction;
import lunartools.sqlrepeatabler.segments.AlterTableAction;

public class AlterTableStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(AlterTableStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().toUpperCase().startsWith(AlterTableStatement.COMMAND);
	}

	@Override
	public Statement createStatement(SqlScript sqlScript) throws Exception{
		if(!match(sqlScript.peekLineAsString())) {
			throw new Exception("Illegal factory call");
		}

		StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
		logger.info("Statement: "+statementTokenizer.toString());
		statementTokenizer.setBackgroundColor(null);

		Token tokenStatement=statementTokenizer.nextToken(AlterTableStatement.COMMAND);
		tokenStatement.setCategory(Category.STATEMENT);
		tokenStatement=tokenStatement.toUpperCase();

		TableName tableName=TableName.createInstanceByConsuming(statementTokenizer);
		logger.debug("Table: "+tableName.toString());

		statementTokenizer.stripWhiteSpaceLeft();

		ArrayList<AlterTableAction> columnElements=null;
		if(statementTokenizer.consumeCommandIgnoreCaseAndSpace("ADD")) {
			columnElements=parseAddAction(statementTokenizer);
		}else if(statementTokenizer.consumeCommandIgnoreCaseAndSpace("DROP")) {
			columnElements=parseDropAction(statementTokenizer);
		}else if(statementTokenizer.consumeCommandIgnoreCaseAndSpace("ALTER COLUMN")) {
			columnElements=parseAlterColumnAction(statementTokenizer);
		}else if(statementTokenizer.consumeCommandIgnoreCaseAndSpace("MODIFY COLUMN")) {//invalid
			logger.warn("Found MODIFY COLUMN action which is most likely MySql flavour and not supported in T-SQL. Processing as ALTER COLUMN.");
			columnElements=parseAlterColumnAction(statementTokenizer);
		}else {
			throw new SqlParserException("Unsupported ALTER TABLE action found",statementTokenizer.getFirstCharacter());
		}

		return new AlterTableStatement(tokenStatement,tableName,columnElements);
	}

	public ArrayList<AlterTableAction> parseAddAction(StatementTokenizer statementTokenizer) throws Exception {
		//column definitions
		//constraint definitions
		/*
		 * alter table [T_FOO]
		 * add [COLUMNNAME] tinyint;
		 * 
		 * alter table [T_FOO]
		 * add constraint [T_FOO_COLUMNNAME_ID_FK]
		 * foreign key ([COLUMNNAME_ID])
		 * references [T_FOO] ([ID]);
		 */
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
						throw new SqlParserException("'REFERENCES' keyword not found.",statementTokenizer.getFirstCharacter());
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
					throw new SqlParserException("Neither 'FOREIGN KEY' nor 'UNIQUE' keyword not found",statementTokenizer.getFirstCharacter());
				}
			}else {
				if(statementTokenizer.startsWithIgnoreCase("COLUMN")){
					logger.warn("Ignoring COLUMN keyword which is not allowed in T-SQL. Script probably uses MySql flavour. "+statementTokenizer.getFirstCharacter().getLocationString());
					Token token=statementTokenizer.nextToken();
					token.setCategory(Category.IGNORED);
				}
				Token tokenColumName=statementTokenizer.nextToken().setCategory(Category.COLUMN);
				Token tokenColumParameter=statementTokenizer.nextTokenUntil(',').setCategory(Category.COLUMNPARAMETER);

				AlterTableAction alterTableAction=new AddColumnAction(tokenColumName,tokenColumParameter);
				alterTableActions.add(alterTableAction);
				if(statementTokenizer.startsWithIgnoreCase("ADD")) {
					logger.warn("Ignoring illegal ADD command. In T-SQL, when adding multiple columns in a single ALTER TABLE...ADD statement, only use one ADD keyword!"+statementTokenizer.getFirstCharacter().getLocationString());
					Token tokenIllegal=statementTokenizer.nextToken();
					tokenIllegal.setCategory(Category.IGNORED);
				}
			}
		}
		return alterTableActions;
	}

	public ArrayList<AlterTableAction> parseDropAction(StatementTokenizer statementTokenizer) throws Exception {
		//column targets
		//constraint targets
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
			throw new SqlParserException(String.format("DROP target not supported: %s",tokenTarget.toString()),tokenTarget.getFirstCharacter());
		}
		return alterTableActions;
	}

	public ArrayList<AlterTableAction> parseAlterColumnAction(StatementTokenizer statementTokenizer) throws Exception {
		logger.debug("Parsing ALTER COLUMN action");
		ArrayList<AlterTableAction> alterTableActions=new ArrayList<>();
		Token tokenColumnName=statementTokenizer.nextToken();
		Token tokenColumnParameter=statementTokenizer.nextTokenUntil(',');
		AlterTableAction alterTableAction=new AlterColumnAction(tokenColumnName,tokenColumnParameter);
		alterTableActions.add(alterTableAction);
		if(statementTokenizer.hasNext()) {
			Token token=statementTokenizer.nextToken();
			throw new SqlParserException(String.format("Unexpected content: %s",token.toString()),token.getFirstCharacter());
		}
		return alterTableActions;
	}

	public boolean beginsWithSupportedCommand(StatementTokenizer statementTokenizer) {
		return 
				statementTokenizer.startsWithIgnoreCase("ADD") ||
				statementTokenizer.startsWithIgnoreCase("DROP") ||
				statementTokenizer.startsWithIgnoreCase("ALTER COLUMN") ||
				statementTokenizer.startsWithIgnoreCase("MODIFY COLUMN" //MySql, invalid for T-SQL, is interpreted as 'ALTER COLUMN'
						) ;
	}

}
