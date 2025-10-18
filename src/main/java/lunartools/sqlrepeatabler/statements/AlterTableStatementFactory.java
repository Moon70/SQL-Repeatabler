package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.segments.AddColumnSegment;
import lunartools.sqlrepeatabler.segments.AddForeignKeyConstraintSegment;
import lunartools.sqlrepeatabler.segments.AddUniqueConstraintSegment;
import lunartools.sqlrepeatabler.segments.AlterColumnSegment;
import lunartools.sqlrepeatabler.segments.DropColumnSegment;
import lunartools.sqlrepeatabler.segments.DropConstraintSegment;
import lunartools.sqlrepeatabler.segments.Segment;
import lunartools.sqlrepeatabler.util.SqlParserTools;

public class AlterTableStatementFactory extends StatementFactory{
	private static Logger logger = LoggerFactory.getLogger(AlterTableStatementFactory.class);

	@Override
	public boolean match(String line) {
		return line.trim().toUpperCase().startsWith(AlterTableStatement.COMMAND);
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
		sbStatement.delete(0, AlterTableStatement.COMMAND.length()+1);
		SqlParserTools.stripSpace(sbStatement);

		TableName tableName=TableName.createInstanceByConsuming(sbStatement);
		logger.debug(tableName.toString());

		SqlParserTools.stripSpace(sbStatement);
		if(!beginsWithSupportedCommand(sbStatement)) {
			throw new Exception("unsupported ALTER TABLE action found: "+sbStatement.toString());
		}

		ArrayList<Segment> columnElements=null;
		if(SqlParserTools.consumePrefixIgnoreCaseAndSpace(sbStatement, "ADD")) {
			columnElements=parseAddAction(sbStatement);
		}else if(SqlParserTools.consumePrefixIgnoreCaseAndSpace(sbStatement, "DROP")) {
			columnElements=parseDropAction(sbStatement);
		}else if(SqlParserTools.consumePrefixIgnoreCaseAndSpace(sbStatement, "ALTER COLUMN")) {
			columnElements=parseAlterColumnAction(sbStatement);
		}else if(SqlParserTools.consumePrefixIgnoreCaseAndSpace(sbStatement, "MODIFY COLUMN")) {//invalid
			logger.warn("found MODIFY COLUMN action which is most likely MySql and not supported on T-SQL. Processing as ALTER COLUMN...");
			columnElements=parseAlterColumnAction(sbStatement);
		}

		return new AlterTableStatement(tableName,columnElements);
	}

	public ArrayList<Segment> parseAddAction(StringBuilder sbCommand) throws Exception {
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
		logger.debug("parsing ADD action: "+sbCommand.toString());
		ArrayList<Segment> columnElements=new ArrayList<>();

		while(sbCommand.length()>1 && sbCommand.charAt(0)!=';') {
			if(beginsWithSupportedCommand(sbCommand)) {
				break;
			}else if(SqlParserTools.consumePrefixIgnoreCaseAndSpace(sbCommand, "CONSTRAINT")) {
				String constraintName=consumeTokenIgnoreSpaceAndComma(sbCommand);

				if(SqlParserTools.consumePrefixIgnoreCaseAndSpace(sbCommand, "FOREIGN KEY")) {
					String foreignKey=consumeTokenIgnoreSpaceAndComma(sbCommand);
					if(!SqlParserTools.consumePrefixIgnoreCaseAndSpace(sbCommand, "REFERENCES")) {
						throw new Exception("'REFERENCES' keyword not found: "+sbCommand.substring(0,1+sbCommand.lastIndexOf(";")));
					}
					String referencesTable=consumeTokenIgnoreSpaceAndComma(sbCommand);
					String referencesColumn=consumeTokenIgnoreSpaceAndComma(sbCommand);

					Segment columnElement=new AddForeignKeyConstraintSegment("ADD",constraintName,foreignKey,referencesTable,referencesColumn);
					columnElements.add(columnElement);

				}else if(SqlParserTools.consumePrefixIgnoreCaseAndSpace(sbCommand, "UNIQUE")) {
					String referencesColumn=consumeTokenIgnoreSpaceAndComma(sbCommand);

					Segment columnElement=new AddUniqueConstraintSegment("ADD",constraintName,referencesColumn);
					columnElements.add(columnElement);

				}else {
					throw new Exception("Neither 'FOREIGN KEY' nor 'UNIQUE' keyword not found: "+sbCommand.substring(0,1+sbCommand.lastIndexOf(";")));
				}
			}else {
				if(SqlParserTools.consumePrefixIgnoreCaseAndSpace(sbCommand, "COLUMN")){
					logger.warn("Script is most likely in MySql format. Ignoring COLUMN keyword which is not allowed in T_SQL");
				}
				String columnName=consumeTokenIgnoreSpaceAndComma(sbCommand);
				String columnParameter=consumeAllTokensIgnoreComma(sbCommand);
				Segment columnElement=new AddColumnSegment("ADD",columnName,columnParameter);
				columnElements.add(columnElement);
			}
		}
		return columnElements;
	}

	public ArrayList<Segment> parseDropAction(StringBuilder sbCommand) throws Exception {
		//column targets
		//constraint targets
		logger.debug("parsing DROP action: "+sbCommand.toString());
		ArrayList<Segment> columnElements=new ArrayList<>();
		String target=consumeTokenIgnoreSpaceAndComma(sbCommand);

		if(target.equalsIgnoreCase("COLUMN")) {
			while(true) {
				String columnName=consumeTokenIgnoreSpaceAndComma(sbCommand);
				Segment columnElement=new DropColumnSegment("DROP",columnName);
				columnElements.add(columnElement);

				if(sbCommand.charAt(0)==';') {
					break;
				}

				if(sbCommand.charAt(0)==',') {
					sbCommand.deleteCharAt(0);
				}
			}

		}else if(target.equalsIgnoreCase("CONSTRAINT")) {
			String constraintName=consumeTokenIgnoreSpaceAndComma(sbCommand);
			Segment columnElement=new DropConstraintSegment("DROP",constraintName);
			columnElements.add(columnElement);

		}else {
			throw new Exception("DROP target not supported: >"+target+"<");
		}
		return columnElements;
	}

	public ArrayList<Segment> parseAlterColumnAction(StringBuilder sbCommand) throws Exception {
		logger.debug("parsing ALTER COLUMN action: "+sbCommand.toString());
		ArrayList<Segment> columnElements=new ArrayList<>();
		while(true) {
			String columnName=consumeTokenIgnoreSpaceAndComma(sbCommand);
			String columnParameter=consumeAllTokensIgnoreCommaParenthesis(sbCommand);

			Segment columnElement=new AlterColumnSegment("ALTER COLUMN",columnName,columnParameter);
			columnElements.add(columnElement);

			if(sbCommand.charAt(0)==';') {
				break;
			}

			if(sbCommand.charAt(0)==',') {
				sbCommand.deleteCharAt(0);
			}

			//TODO: should be consumed in higher loop
			SqlParserTools.consumePrefixIgnoreCaseAndSpace(sbCommand, "ALTER COLUMN");
		}
		return columnElements;
	}

	public boolean beginsWithSupportedCommand(StringBuilder sbCommand) {
		return 
				SqlParserTools.startsWithIgnoreCase(sbCommand, "ADD") ||
				SqlParserTools.startsWithIgnoreCase(sbCommand, "DROP") ||
				SqlParserTools.startsWithIgnoreCase(sbCommand, "ALTER COLUMN") ||
				SqlParserTools.startsWithIgnoreCase(sbCommand, "MODIFY COLUMN" //MySql, invalid for T-SQL, is interpreted as 'ALTER COLUMN'
						) ;
	}

}
