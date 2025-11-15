package lunartools.sqlrepeatabler.parser;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class SqlScript {
	private ArrayList<SqlString> sqlCharacterLinesOriginal=new ArrayList<>();
	private ArrayList<SqlString> sqlStrings;
	private int index;

	public static SqlScript createInstance(StringBuffer stringBuffer) throws Exception{
		try(
				StringReader stringReader=new StringReader(stringBuffer.toString());
				BufferedReader bufferedReader=new BufferedReader(stringReader)){
			return createInstance(bufferedReader);
		}
	}

	public static SqlScript createInstance(StringBuilder stringBuilder) throws Exception{
		try(
				StringReader stringReader=new StringReader(stringBuilder.toString());
				BufferedReader bufferedReader=new BufferedReader(stringReader)){
			return createInstance(bufferedReader);
		}
	}

	public static SqlScript createInstance(BufferedReader bufferedReader) throws Exception{
		return new SqlScript(bufferedReader);
	}

	@SuppressWarnings("unchecked")
	private SqlScript(BufferedReader bufferedReader) throws IOException {
		ArrayList<Integer> scriptAsIntegerArray=new ArrayList<>();
		int iChar;
		while((iChar=bufferedReader.read())!=-1) {
			scriptAsIntegerArray.add(iChar);
		}

		int lineIndex=0;
		int column=0;
		int characterIndex=-1;
		SqlString sqlString=new SqlString();
		for(int i=0;i<scriptAsIntegerArray.size();i++) {
			characterIndex++;
			iChar=scriptAsIntegerArray.get(i);
			char c=(char)iChar;
			if(c==0x0a) {//LF
				lineIndex++;
				column=0;
				sqlCharacterLinesOriginal.add(sqlString);
				sqlString=new SqlString();
			}else if(c==0x0d) {//CR
				lineIndex++;
				column=0;
				sqlCharacterLinesOriginal.add(sqlString);
				sqlString=new SqlString();
				if(i<scriptAsIntegerArray.size()-1 && (scriptAsIntegerArray.get(i+1))==0x0a){
					characterIndex++;
					i++;
				}
			}else {
				SqlCharacter sqlCharacter=new SqlCharacter(c,new CharacterLocation(lineIndex,column,characterIndex));
				sqlString.add(sqlCharacter);
			}
		}
		sqlCharacterLinesOriginal.add(sqlString);
		sqlStrings=(ArrayList<SqlString>)sqlCharacterLinesOriginal.clone();
	}

	/**
	 * @return true when index points to a valid line, false at eof
	 */
	public boolean hasCurrentLine() {
		return index<sqlStrings.size();
	}

	/**
	 * @return Index of current line, or -1 at eof
	 */
	public int getIndex() {
		return index<sqlStrings.size()?index:-1;
	}

	/**
	 * @return Current line
	 */
	public String peekLineAsString() {
		if(index==sqlStrings.size()) {
			return null;
		}
		return sqlStrings.get(index).toString();
	}

	/**
	 * @return Current line
	 */
	public SqlString peekLine() {
		if(index==sqlStrings.size()) {
			return null;
		}
		return sqlStrings.get(index);
	}

	public String readLineAsString() {
		if(index==sqlStrings.size()) {
			return null;
		}
		return sqlStrings.get(index++).toString();
	}

	public SqlString readLine() {
		if(index==sqlStrings.size()) {
			return null;
		}
		return sqlStrings.get(index++);
	}

	public SqlCharacter getFirstCharacterOfCurrentLine() {
		if(index==sqlStrings.size()) {
			return null;
		}
		SqlString sqlScriptLine=sqlStrings.get(index);
		return sqlScriptLine.getFirstCharacter();
	}

	/**
	 * @return line after incrementing the index 
	 */
	public String readNextLineAsString() {
		index++;
		SqlString sqlScriptLine=peekLine();
		if(sqlScriptLine==null) {
			return null;
		}
		return sqlScriptLine.toString();
	}

	private SqlString readLineCharacters() {
		if(index==sqlStrings.size()) {
			return null;
		}
		return sqlStrings.get(index++);
	}

	public StatementTokenizer consumeStatement() throws Exception {
		ArrayList<SqlCharacter> sqlStringStatement=new ArrayList<>();
		while(true) {
			SqlString sqlStringLine=readLineCharacters();
			if(sqlStringLine==null) {
				//				throw new EOFException("Unexpected end of script");
				break;
			}
			if(sqlStringStatement.size()>0 && !sqlStringStatement.get(sqlStringStatement.size()-1).isWhiteSpace()) {
				sqlStringStatement.add(SqlCharacter.SEPARATOR);
			}
			sqlStringStatement.addAll(sqlStringLine.getCharacters());
			if(sqlStringLine.endsWithSemicolonIgnoreWhiteSpace()) {
				break;
			}
		}
		return new StatementTokenizer(stripWhitespace(sqlStringStatement));
	}

	public StatementTokenizer consumeOneLineStatement() throws Exception {
		SqlString sqlString=readLineCharacters();
		if(sqlString==null) {
			throw new EOFException("Unexpected end of script");
		}
		ArrayList<SqlCharacter> charactersOfStatement=new ArrayList<>();
		charactersOfStatement.addAll(sqlString.getCharacters());
		return new StatementTokenizer(stripWhitespace(charactersOfStatement));
	}

	private ArrayList<SqlCharacter> stripWhitespace(ArrayList<SqlCharacter> characterList){
		boolean singleQuoteOpen=false;
		boolean doubleQuoteOpen=false;
		boolean space=false;
		boolean whitespace=true;

		ArrayList<SqlCharacter> strippedCharacterList=new ArrayList<>();
		for(int i=0;i<characterList.size();i++) {
			SqlCharacter sqlCharacter=characterList.get(i);
			if((sqlCharacter.getChar()=='\n') || (sqlCharacter.getChar()=='\r') || (sqlCharacter.getChar()=='\t')) {
				if(whitespace) {
					continue;
				}
				if(!singleQuoteOpen && !doubleQuoteOpen) {
					if(space) {
						continue;
					}else {
						sqlCharacter=SqlCharacter.SEPARATOR;
						space=true;
					}
				}
			}else {
				if(!sqlCharacter.isSpace()) {
					space=false;
					whitespace=false;
				}
			}

			if(sqlCharacter.isSpace()) {
				if(!singleQuoteOpen && !doubleQuoteOpen && space || whitespace) {
					continue;
				}else {
					space=true;
				}
			}
			if(sqlCharacter.getChar()==39){// '
				singleQuoteOpen=!singleQuoteOpen;
			}
			if(sqlCharacter.getChar()=='"'){
				doubleQuoteOpen=!doubleQuoteOpen;
			}
			strippedCharacterList.add(sqlCharacter);
		}
		return strippedCharacterList;
	}

	public String getLineAt(int index) {
		return sqlStrings.get(index).toString();
	}

	public ArrayList<SqlString> getSqlStrings(){
		return sqlStrings;
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for(int line=0;line<sqlStrings.size();line++) {
			sb.append(sqlStrings.get(line).toString());
			sb.append("\r\n");
		}
		return sb.toString();
	}

}
