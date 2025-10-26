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
import lunartools.sqlrepeatabler.segments.AddColumnSegment;
import lunartools.sqlrepeatabler.segments.AddForeignKeyConstraintSegment;
import lunartools.sqlrepeatabler.segments.AddUniqueConstraintSegment;
import lunartools.sqlrepeatabler.segments.AlterColumnSegment;
import lunartools.sqlrepeatabler.segments.DropColumnSegment;
import lunartools.sqlrepeatabler.segments.DropConstraintSegment;
import lunartools.sqlrepeatabler.segments.Segment;

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
		if(logger.isTraceEnabled()) {
			logger.trace("parsing statement");
		}

		StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
		logger.info("statement: "+statementTokenizer.toString());

		Token token=statementTokenizer.nextToken();//skip 'ALTER' token	
		token.categorize(Category.STATEMENT);
		token=statementTokenizer.nextToken();//skip 'TABLE' token
		token.categorize(Category.STATEMENT);

		TableName tableName=TableName.createInstanceByConsuming(statementTokenizer);
		logger.debug(tableName.toString());

		statementTokenizer.stripWhiteSpaceLeft();

		ArrayList<Segment> columnElements=null;
		if(statementTokenizer.consumeCommandIgnoreCaseAndSpace("ADD")) {
			columnElements=parseAddAction(statementTokenizer);
		}else if(statementTokenizer.consumeCommandIgnoreCaseAndSpace("DROP")) {
			columnElements=parseDropAction(statementTokenizer);
		}else if(statementTokenizer.consumeCommandIgnoreCaseAndSpace("ALTER COLUMN")) {
			columnElements=parseAlterColumnAction(statementTokenizer);
		}else if(statementTokenizer.consumeCommandIgnoreCaseAndSpace("MODIFY COLUMN")) {//invalid
			logger.warn("found MODIFY COLUMN action which is most likely MySql and not supported on T-SQL. Processing as ALTER COLUMN...");
			columnElements=parseAlterColumnAction(statementTokenizer);
		}else {
			//throw new Exception("unsupported ALTER TABLE action found");
            throw new SqlParserException("unsupported ALTER TABLE action found",statementTokenizer.getFirstCharacter());
		}

		return new AlterTableStatement(tableName,columnElements);
	}

	public ArrayList<Segment> parseAddAction(StatementTokenizer statementTokenizer) throws Exception {
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
		logger.debug("parsing ADD action: ");
		ArrayList<Segment> columnElements=new ArrayList<>();

		while(statementTokenizer.hasNext()) {
			if(beginsWithSupportedCommand(statementTokenizer)) {
				break;
			}else if(statementTokenizer.consumeCommandIgnoreCaseAndSpace("CONSTRAINT")) {
				Token tokenConstraintName=statementTokenizer.nextToken();

				if(statementTokenizer.consumeCommandIgnoreCaseAndSpace("FOREIGN KEY")) {
					Token tokenForeignKey=statementTokenizer.nextToken();

					if(!statementTokenizer.consumeCommandIgnoreCaseAndSpace("REFERENCES")) {
						throw new Exception("'REFERENCES' keyword not found: ");
					}
					Token tokenReferencesTable=statementTokenizer.nextToken();
					Token tokenReferencesColumn=statementTokenizer.nextToken();

					Segment columnElement=new AddForeignKeyConstraintSegment("ADD",tokenConstraintName,tokenForeignKey,tokenReferencesTable,tokenReferencesColumn);
					columnElements.add(columnElement);

				}else if(statementTokenizer.consumeCommandIgnoreCaseAndSpace("UNIQUE")) {
					Token tokenReferencesColumn=statementTokenizer.nextToken();

					Segment columnElement=new AddUniqueConstraintSegment("ADD",tokenConstraintName,tokenReferencesColumn);
					columnElements.add(columnElement);
				}else {
					throw new Exception("Neither 'FOREIGN KEY' nor 'UNIQUE' keyword not found");
				}
			}else {
				if(statementTokenizer.consumeCommandIgnoreCaseAndSpace("COLUMN")){
					logger.warn("Script is most likely in MySql format. Ignoring COLUMN keyword which is not allowed in T_SQL");
				}
				Token tokenColumName=statementTokenizer.nextToken();
				tokenColumName.categorize(Category.COLUMN);
				Token tokenColumParameter=statementTokenizer.nextTokenUntil(',');
				tokenColumParameter.categorize(Category.COLUMNPARAMETER);
				
				Segment columnElement=new AddColumnSegment("ADD",tokenColumName,tokenColumParameter);
				columnElements.add(columnElement);
			}
		}
		return columnElements;
	}

	public ArrayList<Segment> parseDropAction(StatementTokenizer statementTokenizer) throws Exception {
		//column targets
		//constraint targets
		logger.debug("parsing DROP action");
		ArrayList<Segment> columnElements=new ArrayList<>();
		Token tokenTarget=statementTokenizer.nextToken();

		if(tokenTarget.toString().equalsIgnoreCase("COLUMN")) {
			while(true) {
				Token tokenColumnName=statementTokenizer.nextToken();

				Segment columnElement=new DropColumnSegment("DROP",tokenColumnName);
				columnElements.add(columnElement);

				if(!statementTokenizer.hasNext()) {
					break;
				}

				statementTokenizer.consumePrefixIgnoreCaseAndSpace(",");
			}

		}else if(tokenTarget.toString().equalsIgnoreCase("CONSTRAINT")) {
			Token tokenConstraintName=statementTokenizer.nextToken();

			Segment columnElement=new DropConstraintSegment("DROP",tokenConstraintName);
			columnElements.add(columnElement);

		}else {
			throw new Exception("DROP target not supported: >"+tokenTarget+"<");
		}
		return columnElements;
	}

	public ArrayList<Segment> parseAlterColumnAction(StatementTokenizer statementTokenizer) throws Exception {
		logger.debug("parsing ALTER COLUMN action");
		ArrayList<Segment> columnElements=new ArrayList<>();
		while(true) {
			Token tokenColumnName=statementTokenizer.nextToken();
			Token tokenColumnParameter=statementTokenizer.nextTokenUntil(',');

			Segment columnElement=new AlterColumnSegment("ALTER COLUMN",tokenColumnName,tokenColumnParameter);
			columnElements.add(columnElement);

			if(!statementTokenizer.hasNext()) {
				break;
			}

			statementTokenizer.consumePrefixIgnoreCaseAndSpace(",");

			//TODO: should be consumed in higher loop
			statementTokenizer.consumePrefixIgnoreCaseAndSpace("ALTER COLUMN");
		}
		return columnElements;
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
