package lunartools.sqlrepeatabler.parser;

import java.util.ArrayList;

public class SqlBlock {
	private ArrayList<SqlString> sqlStrings=new ArrayList<>();
	
	public void add(ArrayList<SqlString> sqlStrings) {
		this.sqlStrings.addAll(sqlStrings);
	}
	
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for(SqlString sqlString:sqlStrings) {
			sb.append(sqlString.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	
}
