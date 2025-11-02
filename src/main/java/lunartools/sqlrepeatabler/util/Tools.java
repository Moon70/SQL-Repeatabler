package lunartools.sqlrepeatabler.util;

import java.util.ArrayList;

import lunartools.sqlrepeatabler.parser.SqlString;

public class Tools {

	public static StringBuilder toStringBuilder(ArrayList<SqlString> sqlCharacterLines) {
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<sqlCharacterLines.size();i++) {
			SqlString sqlString=sqlCharacterLines.get(i);
			sb.append(sqlString.toString());
			sb.append("\n");
		}
		return sb;
	}
	
}
