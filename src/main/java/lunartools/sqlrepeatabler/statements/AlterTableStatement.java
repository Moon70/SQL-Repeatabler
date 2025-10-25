package lunartools.sqlrepeatabler.statements;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.SqlParser;
import lunartools.sqlrepeatabler.segments.AlterColumnSegment;
import lunartools.sqlrepeatabler.segments.Segment;

public class AlterTableStatement implements Statement{
	private static Logger logger = LoggerFactory.getLogger(AlterTableStatement.class);
	public static final String COMMAND="ALTER TABLE";
	private TableName tableName;
	private ArrayList<Segment> columnElements;
	private boolean mySql;

	public AlterTableStatement(TableName tableName,ArrayList<Segment> columnElements) {
		this.tableName=tableName;
		this.columnElements=columnElements;
		this.mySql=tableName.isMySql();
	}

	@Override
	public void toSql(StringBuilder sb) throws Exception {
		boolean hasAlterColumnAction=false;
		StringBuilder sbTemp=new StringBuilder();
		for(int i=0;i<columnElements.size();i++) {
			Segment columnelement=columnElements.get(i);
			if(columnelement instanceof AlterColumnSegment) {
				hasAlterColumnAction=true;
				if(i>0) {
					sbTemp.append(',').append(SqlParser.CRLF);
				}
				columnelement.toSql(sbTemp,tableName, mySql);
			}
		}
		if(hasAlterColumnAction) {
			sb.append(String.format("ALTER TABLE %s",tableName.getFullName())).append(SqlParser.CRLF);
			sb.append(sbTemp).append(';').append(SqlParser.CRLF);
		}

		for(int i=0;i<columnElements.size();i++) {
			Segment columnelement=columnElements.get(i);
			if(!(columnelement instanceof AlterColumnSegment)) {
				columnelement.toSql(sb,tableName, mySql);
				if(i<columnElements.size()-1) {
					sb.append(SqlParser.CRLF);
				}
			}
		}
	}

	public boolean isMySql() {
		return mySql;
	}

}
