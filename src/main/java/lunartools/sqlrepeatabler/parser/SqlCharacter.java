package lunartools.sqlrepeatabler.parser;

import java.util.ArrayList;

public class SqlCharacter {
	public static final SqlCharacter SEPARATOR=new SqlCharacter(' ',-1,-1,-1);
	private char c;
	private int row;
	private int column;
	private int indexInFile;
	private Category category=Category.UNCATEGORIZED;
	
	/**
	 * @deprecated
	 */
	@Deprecated
	public static ArrayList<SqlCharacter> createCharactersFromString(String string, Category category, Token... tokens){
        String[] stringFragments=string.split("%s",-1);
        if(stringFragments.length!=tokens.length+1) {
            throw new IllegalArgumentException("String fragment count ("+stringFragments.length+") does not match token count ("+tokens.length+")");
        }
        
        ArrayList<SqlCharacter> characters=new ArrayList<>();
        for(int i=0;i<stringFragments.length;i++) {
            String s=stringFragments[i];
            for(int k=0;k<s.length();k++) {
                SqlCharacter sqlCharacter=new SqlCharacter(s.charAt(k),-1,-1,-1);
                sqlCharacter.setCategory(category);
                characters.add(sqlCharacter);
            }
            if(i<stringFragments.length-1) {
                characters.addAll(tokens[i].getCharacters());
            }
            
        }
	    return  characters;
	}
	
	public SqlCharacter(char c, int row, int column, int indexInFile) {
		this.c=c;
		this.row=row;
		this.column=column;
		this.indexInFile=indexInFile;
	}
    
    public SqlCharacter(char c, int row, int column, int indexInFile,Category category) {
        this.c=c;
        this.row=row;
        this.column=column;
        this.indexInFile=indexInFile;
        this.category=category;
    }
    
    public SqlCharacter(char c,Category category) {
        this(c,-1,-1,-1);
        this.category=category;
    }
	
	public char getChar() {
		return c;
	}

	public int getRow() {
		return row;
	}
	
	public int getColumn() {
	    return column;
	}

	public int getIndex() {
		return indexInFile;
	}

	public boolean isSpace() {
		return c==' ';
	}

    public boolean isWhiteSpace() {
        return c==' ' || c=='\t';
    }

	public boolean isSemicolon() {
		return c==';';
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
}
