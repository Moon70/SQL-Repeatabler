package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlCharacter;
import lunartools.sqlrepeatabler.parser.SqlParser;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.segments.TableSegment;

public class CreateTableStatement implements Statement{
	private static Logger logger = LoggerFactory.getLogger(CreateTableStatement.class);
	public static final String COMMAND="CREATE TABLE";
	private TableName tableName;
	private ArrayList<TableSegment> tableElements;
	private boolean mySql;

	public CreateTableStatement(TableName tableName,ArrayList<TableSegment> tableElements) {
		this.tableName=tableName;
		this.tableElements=tableElements;
		this.mySql=tableName.isMySql();
	}

	@Override
	public void toSql(StringBuilder sb) throws Exception {
		sb.append(String.format("IF OBJECT_ID(N'%s', 'U') IS NULL", tableName.getFullNameWithoutDelimiterAsString())).append(SqlParser.CRLF);
		sb.append(String.format("BEGIN")).append(SqlParser.CRLF);
		sb.append(String.format("\tCREATE TABLE %s (",tableName.getFullNameAsString())).append(SqlParser.CRLF);
		if(tableName.isMySql()) {
			logger.warn("Script is most likely in MySql format. Converting backtick delimiter to square brackets");
		}

		for(int i=0;i<tableElements.size();i++) {
			sb.append("\t\t");
			tableElements.get(i).toSql(sb,mySql);
			if(i<tableElements.size()-1) {
				sb.append(',');
			}
			sb.append(SqlParser.CRLF);
		}

		sb.append("\t);"+SqlParser.CRLF);
		sb.append("END;").append(SqlParser.CRLF);
	}

	@Override
	public void toSqlCharacters(ArrayList<SqlString> sqlCharacterLines) throws Exception {
		sqlCharacterLines.add(SqlString.createSqlStringFromString("IF OBJECT_ID(N'%s', 'U') IS NULL", Category.INSERTED, tableName.getFullNameWithoutDelimiter()));
		sqlCharacterLines.add(SqlString.createSqlStringFromString("BEGIN", Category.INSERTED));
		sqlCharacterLines.add(SqlString.createSqlStringFromString("\tCREATE TABLE %s (", Category.INSERTED,tableName.getFullName()));

		SqlString sqlString;
		for(int i=0;i<tableElements.size();i++) {
			sqlString=SqlString.createSqlStringFromString("\t\t%s", Category.INSERTED,tableElements.get(i).getToken());
			if(i<tableElements.size()-1) {
				sqlString.append(new SqlCharacter(',',Category.INSERTED));
			}
			sqlCharacterLines.add(sqlString);
		}

		sqlCharacterLines.add(SqlString.createSqlStringFromString("\t);", Category.INSERTED));
		sqlCharacterLines.add(SqlString.createSqlStringFromString("END;", Category.INSERTED));
	}

	public boolean isMySql() {
		return mySql;
	}

}
