package lunartools.sqlrepeatabler.segments;

import lunartools.sqlrepeatabler.common.TableName;
import lunartools.sqlrepeatabler.parser.Token;

public abstract class Segment {
	private String action;
	private Token name;

	public Segment(String action,Token name) {
		this.action=action;
		this.name=name;
	}

	public String getColumnNameWithoutDelimiters(){
		String s=name.toString();
		int len=s.length();
		if(
				(s.charAt(0)=='[' && s.charAt(len-1)==']') ||
				(s.charAt(0)=='"' && s.charAt(len-1)=='"')
				) {
			return s.substring(1, len-1);
		}
		return s;
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
