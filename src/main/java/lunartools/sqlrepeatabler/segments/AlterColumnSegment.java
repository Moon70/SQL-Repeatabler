package lunartools.sqlrepeatabler.segments;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Token;

public class AlterColumnSegment extends Segment{
	private String action;
	private Token name;
	private Token parameters;

	public AlterColumnSegment(String action,Token name,Token parameters) {
		super(action,name);
		this.action=action;
		this.name=name;
		this.parameters=parameters;
	}

	public void toSql(StringBuilder sb,TableName tableName,boolean mySql) throws Exception {
		sb.append(String.format("\t%s %s %s",action,name,parameters));
	}

	public String toString() {
		return String.format("AlterColumnSegment: %s %s %s",action,name,parameters);
	}
}
