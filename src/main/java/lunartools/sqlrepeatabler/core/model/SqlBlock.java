package lunartools.sqlrepeatabler.core.model;

import java.util.ArrayList;

public class SqlBlock {
	private ArrayList<SqlString> sqlStrings=new ArrayList<>();

	public void add(SqlString sqlString) {
		this.sqlStrings.add(sqlString);
	}

	public void add(ArrayList<SqlString> sqlStrings) {
		this.sqlStrings.addAll(sqlStrings);
	}

	public void add(SqlBlock sqlBlock) {
		this.sqlStrings.addAll(sqlBlock.getSqlStrings());
	}

	public StringBuilder toStringBuilder() {
		StringBuilder sb=new StringBuilder();
		for(SqlString sqlString:sqlStrings) {
			sb.append(sqlString.toString());
			sb.append("\n");
		}
		return sb;
	}

	public String toString() {
		return toStringBuilder().toString();
	}

	public ArrayList<SqlString> getSqlStrings(){
		return sqlStrings;
	}

	public SqlString getLastLine() {
		return sqlStrings.get(sqlStrings.size()-1);
	}

	public int size() {
		return sqlStrings.size();
	}

	public void setBackgroundColor(String backgroundColor) {
		for (SqlString sqlString:sqlStrings) {
			sqlString.setBackgroundColor(backgroundColor);
		}
	}
}
