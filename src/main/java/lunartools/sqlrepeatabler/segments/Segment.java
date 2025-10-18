package lunartools.sqlrepeatabler.segments;

import lunartools.sqlrepeatabler.common.TableName;

public abstract class Segment {
	private String action;
	private String name;
	
	public Segment(String action,String name) {
		this.action=action;
		this.name=name;
	}

	public String getColumnNameWithoutDelimiters(){
		int len=name.length();
		if(
				(name.charAt(0)=='[' && name.charAt(len-1)==']') ||
				(name.charAt(0)=='"' && name.charAt(len-1)=='"')
				) {
			return name.substring(1, len-1);
		}
		return name;
	}

	public String stripDelimiters(String s) {
		int len=s.length();
		if(
				(s.charAt(0)=='[' && s.charAt(len-1)==']') ||
				(s.charAt(0)=='"' && s.charAt(len-1)=='"')
				) {
			return s.substring(1, len-1);
		}
		return s;
	}
	
	public String getAction() {
		return action;
	}
	
	public abstract void toSql(StringBuilder sb,TableName tableName,boolean mySql) throws Exception;

}
