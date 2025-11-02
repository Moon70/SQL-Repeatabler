package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Category;
import lunartools.sqlrepeatabler.parser.SqlCharacter;
import lunartools.sqlrepeatabler.parser.SqlParser;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.segments.AlterColumnSegment;
import lunartools.sqlrepeatabler.segments.Segment;

public class AlterTableStatement implements Statement{
	private static Logger logger = LoggerFactory.getLogger(AlterTableStatement.class);
	public static final String COMMAND="ALTER TABLE";
	private TableName tableName;
	private ArrayList<Segment> segments;
	private boolean mySql;

	public AlterTableStatement(TableName tableName,ArrayList<Segment> segments) {
		this.tableName=tableName;
		this.segments=segments;
		this.mySql=tableName.isMySql();
	}

	@Override
	public void toSql(StringBuilder sb) throws Exception {
		boolean hasAlterColumnAction=false;
		StringBuilder sbTemp=new StringBuilder();
		for(int i=0;i<segments.size();i++) {
			Segment columnelement=segments.get(i);
			if(columnelement instanceof AlterColumnSegment) {
				hasAlterColumnAction=true;
				if(i>0) {
					sbTemp.append(',').append(SqlParser.CRLF);
				}
				columnelement.toSql(sbTemp,tableName, mySql);
			}
		}
		if(hasAlterColumnAction) {
			sb.append(String.format("ALTER TABLE %s",tableName.getFullNameAsString())).append(SqlParser.CRLF);
			sb.append(sbTemp).append(';').append(SqlParser.CRLF);
		}

		for(int i=0;i<segments.size();i++) {
			Segment columnelement=segments.get(i);
			if(!(columnelement instanceof AlterColumnSegment)) {
				columnelement.toSql(sb,tableName, mySql);
				if(i<segments.size()-1) {
					sb.append(SqlParser.CRLF);
				}
			}
		}
	}

	@Override
	public void toSqlCharacters(ArrayList<SqlString> sqlCharacterLines) throws Exception {
		boolean hasAlterColumnAction=false;

		ArrayList<SqlString> sqlCharacterLinesTemp=new ArrayList<>();
		for(int i=0;i<segments.size();i++) {
			Segment segment=segments.get(i);
			if(segment instanceof AlterColumnSegment) {
				hasAlterColumnAction=true;
				if(i>0) {
					sqlCharacterLinesTemp.get(sqlCharacterLinesTemp.size()-1).append(new SqlCharacter(',',Category.UNCATEGORIZED));
				}
				segment.toSqlCharacters(sqlCharacterLinesTemp, tableName, hasAlterColumnAction);
			}
		}
		if(hasAlterColumnAction) {
			sqlCharacterLines.add(SqlString.createSqlStringFromString("ALTER TABLE %s", Category.INSERTED,tableName.getFullName()));
			sqlCharacterLinesTemp.get(sqlCharacterLinesTemp.size()-1).append(new SqlCharacter(';',Category.UNCATEGORIZED));
			sqlCharacterLines.addAll(sqlCharacterLinesTemp);
		}

		for(int i=0;i<segments.size();i++) {
			Segment columnelement=segments.get(i);
			if(!(columnelement instanceof AlterColumnSegment)) {
				columnelement.toSqlCharacters(sqlCharacterLines, tableName, hasAlterColumnAction);
				if(i<segments.size()-1) {
					sqlCharacterLines.add(SqlString.EMPTY_LINE);
				}
			}
		}
	}

	public boolean isMySql() {
		return mySql;
	}

}
