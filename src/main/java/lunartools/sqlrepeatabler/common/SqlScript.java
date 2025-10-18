package lunartools.sqlrepeatabler.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import lunartools.sqlrepeatabler.util.SqlParserTools;

public class SqlScript {
	private ArrayList<String> lines=new ArrayList<>();
	private int index;

	public static SqlScript createInstance(StringBuffer stringBuffer) throws Exception{
		try(
				StringReader stringReader=new StringReader(stringBuffer.toString());
				BufferedReader bufferedReader=new BufferedReader(stringReader)){
			return createInstance(bufferedReader);
		}
	}

	public static SqlScript createInstance(BufferedReader bufferedReader) throws Exception{
		return new SqlScript(bufferedReader);
	}

	private SqlScript(BufferedReader bufferedReader) throws IOException {
		String line;
		while((line=bufferedReader.readLine())!=null) {
			lines.add(line);
		}
	}

	public boolean hasCurrentLines() {
		return index<lines.size();
	}

	public int getIndex() {
		return index;
	}

	public int getLineNumber() {
		return index+1;
	}

	public String peekLine() {
		if(index==lines.size()) {
			return null;
		}
		return lines.get(index);
	}

	public String readLine() {
		String line=peekLine();
		index++;
		return line;
	}

	public String readNextLine() {
		index++;
		String line=peekLine();
		return line;
	}

	public String getLineAt(int index) {
		return lines.get(index);
	}

	public StringBuilder consumeUntilSemicolon() throws Exception {
		StringBuilder sb=new StringBuilder();
		while(true) {
			String line=readLine();
			if(line==null) {
				throw new Exception("Unexpected end of script");
			}
			if(sb.length()>0 && sb.charAt(sb.length()-1)!=' ') {
				sb.append(' ');
			}
			sb.append(line);
			if(line.trim().endsWith(";")) {
				break;
			}
		}
		return sb;
	}

	public StringBuilder consumeStatement() throws Exception{
		StringBuilder sbStatement=consumeUntilSemicolon();
		return SqlParserTools.stripWhitespace(sbStatement);
	}

}
