package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.SqlParser;
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
		sb.append(String.format("IF OBJECT_ID(N'%s', 'U') IS NULL", tableName.getFullNameWithoutDelimiter())).append(SqlParser.CRLF);
		sb.append(String.format("BEGIN")).append(SqlParser.CRLF);
		sb.append(String.format("\tCREATE TABLE %s (",tableName.getFullName())).append(SqlParser.CRLF);
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

	public boolean isMySql() {
		return mySql;
	}

}
