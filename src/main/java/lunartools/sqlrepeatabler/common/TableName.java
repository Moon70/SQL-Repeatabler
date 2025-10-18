package lunartools.sqlrepeatabler.common;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableName {
	private static Logger logger = LoggerFactory.getLogger(TableName.class);
	private String databaseName;
	private String schemaName;
	//	private String schemaName="[dbo]";
	private String tableName;
	private boolean mySql;

	private TableName(ArrayList<StringBuilder> segments, boolean mySql) {
		if(segments.get(0)!=null) {
			this.databaseName=segments.get(0).toString();
		}
		if(segments.get(1)!=null) {
			this.schemaName=segments.get(1).toString();
		}
		this.tableName=segments.get(2).toString();
		this.mySql=mySql;
	}

	public static TableName createInstanceByConsuming(StringBuilder sbCommand) throws Exception{
		boolean mySql=false;
		ArrayList<StringBuilder> segments=new ArrayList<>();

		for(int i=0;i<3;i++) {//database name, schema name, table name
			stripSpace(sbCommand);
			if(sbCommand.charAt(0)=='[') {
				StringBuilder segment=createBracketSegmentByConsuming(sbCommand);
				segments.add(segment);
			}else if(sbCommand.charAt(0)=='"') {
				StringBuilder segment=createQuoteSegmentByConsuming(sbCommand);
				segments.add(segment);
			}else if(sbCommand.charAt(0)=='`') {
				mySql=true;
				StringBuilder segment=createBacktickSegmentByConsuming(sbCommand);
				segments.add(segment);
			}else {
				//				throw new Exception("Error scanning table name: >"+sbCommand.toString()+"<");
				StringBuilder segment=createSpaceSegmentByConsuming(sbCommand);
				segments.add(segment);
			}
			stripSpace(sbCommand);
			if(sbCommand.charAt(0)!='.') {
				break;
			}
			sbCommand.deleteCharAt(0);
		}
		while(segments.size()<3) {
			segments.add(0,null);
		}
		return new TableName(segments,mySql);
	}

	private static void stripSpace(StringBuilder sbCommand) {
		while(sbCommand.charAt(0)==' ') {
			sbCommand.deleteCharAt(0);
		}
	}

	private static StringBuilder createBracketSegmentByConsuming(StringBuilder sbCommand) {
		StringBuilder sbSegment=new StringBuilder();
		sbSegment.append(sbCommand.charAt(0));
		sbCommand.deleteCharAt(0);
		while(true) {
			if(sbCommand.charAt(0)==']') {
				sbSegment.append(sbCommand.charAt(0));
				sbCommand.deleteCharAt(0);
				if(sbCommand.charAt(1)==']') {
					continue;
				}else {
					break;
				}
			}
			sbSegment.append(sbCommand.charAt(0));
			sbCommand.deleteCharAt(0);
		}
		return sbSegment;
	}

	private static StringBuilder createQuoteSegmentByConsuming(StringBuilder sbCommand) {
		StringBuilder sbSegment=new StringBuilder();
		sbSegment.append(sbCommand.charAt(0));
		sbCommand.deleteCharAt(0);
		while(true) {
			if(sbCommand.charAt(0)=='"') {
				sbSegment.append(sbCommand.charAt(0));
				sbCommand.deleteCharAt(0);
				if(sbCommand.charAt(1)=='"') {
					continue;
				}else {
					break;
				}
			}
			sbSegment.append(sbCommand.charAt(0));
			sbCommand.deleteCharAt(0);
		}
		return sbSegment;
	}

	private static StringBuilder createBacktickSegmentByConsuming(StringBuilder sbCommand) {
		StringBuilder sbSegment=new StringBuilder();
		sbSegment.append('[');
		sbCommand.deleteCharAt(0);
		while(true) {
			if(sbCommand.charAt(0)=='`') {
				if(sbCommand.charAt(1)=='`') {
					sbSegment.append("``");
					sbCommand.delete(0,2);
					continue;
				}else {
					sbSegment.append(']');
					sbCommand.deleteCharAt(0);
					break;
				}
			}
			sbSegment.append(sbCommand.charAt(0));
			sbCommand.deleteCharAt(0);
		}
		return sbSegment;
	}

	private static StringBuilder createSpaceSegmentByConsuming(StringBuilder sbCommand) {
		StringBuilder sbSegment=new StringBuilder();
		sbSegment.append(sbCommand.charAt(0));
		sbCommand.deleteCharAt(0);
		while(true) {
			if(sbCommand.charAt(0)==' ') {
				sbCommand.deleteCharAt(0);
				if(sbCommand.charAt(1)==' ') {
					continue;
				}else {
					break;
				}
			}
			sbSegment.append(sbCommand.charAt(0));
			sbCommand.deleteCharAt(0);
		}
		return sbSegment;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public String getTableName() {
		return tableName;
	}

	public String getDatabaseNameWithoutDelimiter() {
		return databaseName.substring(1, databaseName.length()-1);
	}

	public String getSchemaNameWithoutDelimiter() {
		return schemaName.substring(1, schemaName.length()-1);
	}

	public String getTableNameWithoutDelimiter() {
		return tableName.substring(1, tableName.length()-1);
	}

	public String getFullName() {
		StringBuffer sb=new StringBuffer();
		if(databaseName!=null) {
			sb.append(databaseName);
			sb.append('.');
		}
		if(schemaName!=null) {
			sb.append(schemaName);
			sb.append('.');
		}
		sb.append(tableName);
		return sb.toString();
	}

	public String getFullSchemaAndName() {
		StringBuffer sb=new StringBuffer();
		if(schemaName!=null) {
			sb.append(schemaName);
			sb.append('.');
		}
		sb.append(tableName);
		return sb.toString();
	}

	public String getFullNameWithoutDelimiter() {
		StringBuffer sb=new StringBuffer();
		if(databaseName!=null) {
			sb.append(getDatabaseNameWithoutDelimiter());
			sb.append('.');
		}
		if(schemaName!=null) {
			sb.append(getSchemaNameWithoutDelimiter());
			sb.append('.');
		}
		sb.append(getTableNameWithoutDelimiter());
		return sb.toString();
	}

	public boolean isMySql() {
		return mySql;
	}

	@Override
	public String toString() {
		return String.format("Table: DatabaseName=%s, SchemaName=%s, TableName=%s",databaseName,schemaName,tableName);
	}
}
