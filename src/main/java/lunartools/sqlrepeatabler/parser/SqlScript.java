package lunartools.sqlrepeatabler.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

public class SqlScript {
	private ArrayList<String> lines=new ArrayList<>();
	private HashMap<Integer,ArrayList<SqlCharacter>> sqlCharacters=new HashMap<>();
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

	private SqlScript(BufferedReader bufferedReader) throws IOException {
		String line;
		int lineIndex=0;
		int characterIndex=0;
		while((line=bufferedReader.readLine())!=null) {
			lines.add(line);
			ArrayList<SqlCharacter> sqlCharacterInLine=new ArrayList<>();
			for(int i=0;i<line.length();i++) {
				SqlCharacter sqlCharacter=new SqlCharacter(line.charAt(i),lineIndex,i,characterIndex+i);
				sqlCharacterInLine.add(sqlCharacter);
			}
			sqlCharacters.put(lineIndex, sqlCharacterInLine);
			lineIndex++;
			characterIndex+=line.length();
		}
	}

	/**
	 * @return true when index points to a valid line, false at eof
	 */
	public boolean hasCurrentLine() {
		return index<lines.size();
	}
	
	/**
	 * @return Index of current line, or -1 at eof
	 */
	public int getIndex() {
		return index<lines.size()?index:-1;
	}

	/**
	 * @return Index plus 1 of current line, or -1 at eof
	 */
	//TODO: remove after improving error location handling
	public int getLineNumber() {
		return index<lines.size()?index+1:-1;
	}

	/**
	 * @return Current line
	 */
	public String peekLine() {
		if(index==lines.size()) {
			return null;
		}
		return lines.get(index);
	}
	
	public String readLine() {
		if(index==lines.size()) {
			return null;
		}
		return lines.get(index++);
	}
	
	/**
	 * @return line after incrementing the index 
	 */
	public String readNextLine() {
		index++;
		String line=peekLine();
		return line;
	}

	private ArrayList<SqlCharacter> readLineCharacters() {
		if(index==lines.size()) {
			return null;
		}
		return sqlCharacters.get(index++);
	}

	public StatementTokenizer consumeStatement() throws Exception {
		SqlCharacter sqlCharacterInsertedSpace=new SqlCharacter(' ',-1,-1,-1);
		ArrayList<SqlCharacter> charactersOfStatement=new ArrayList<>();
		while(true) {
			ArrayList<SqlCharacter> charactersOfLine=readLineCharacters();
			if(charactersOfLine==null) {
				throw new Exception("Unexpected end of script");
			}
			if(charactersOfStatement.size()>0 && charactersOfStatement.get(charactersOfStatement.size()-1).isSpace()) {
				charactersOfStatement.add(sqlCharacterInsertedSpace);
			}
			charactersOfStatement.addAll(charactersOfLine);
			if(endsWithSemicolon(charactersOfLine)) {
				break;
			}
		}
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
	
	private boolean endsWithSemicolon(ArrayList<SqlCharacter> characters) {
		for(int i=characters.size()-1;i>=0;i--) {
			if(characters.get(i).isSemicolon()) {
				return true;
			}
		}
		return false;
	}

	public String getLineAt(int index) {
		return lines.get(index);
	}

}
