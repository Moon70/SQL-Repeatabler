package lunartools.sqlrepeatabler.segments;

import lunartools.sqlrepeatabler.common.TableName;

public class AlterColumnSegment extends Segment{
	private String action;
	private String name;
	private String parameters;

	public AlterColumnSegment(String action,String name,String parameters) {
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
