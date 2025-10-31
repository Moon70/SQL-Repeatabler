package lunartools.sqlrepeatabler.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class SqlCharacterTest {

    @Test
    void getCharWorksAsExpected() throws Exception {
        SqlCharacter character=new SqlCharacter('x',-1,-1,-1);
        assertEquals('x',character.getChar());
    }
    
    @Test
    void getRowWorksAsExpected() throws Exception {
        SqlCharacter character=new SqlCharacter('x',1,2,3);
        assertEquals(1,character.getRow());
    }
    
    @Test
    void getColumnWorksAsExpected() throws Exception {
        SqlCharacter character=new SqlCharacter('x',1,2,3);
        assertEquals(2,character.getColumn());
    }
    
    @Test
    void getIndexWorksAsExpected() throws Exception {
        SqlCharacter character=new SqlCharacter('x',1,2,3);
        assertEquals(3,character.getIndex());
    }
    
    @Test
    void isSpaceWorksAsExpected() throws Exception {
        SqlCharacter character=new SqlCharacter('x',1,2,3);
        assertFalse(character.isSpace());
        
        character=new SqlCharacter(' ',1,2,3);
        assertTrue(character.isSpace());
    }
    
    @Test
    void isSemicolonWorksAsExpected() throws Exception {
        SqlCharacter character=new SqlCharacter('x',1,2,3);
        assertFalse(character.isSemicolon());
        
        character=new SqlCharacter(';',1,2,3);
        assertTrue(character.isSemicolon());
    }
    
    @Test
    void isWhiteSpaceWorksAsExpected() throws Exception {
        SqlCharacter character=new SqlCharacter('x',1,2,3);
        assertFalse(character.isWhiteSpace());
        
        character=new SqlCharacter(' ',1,2,3);
        assertTrue(character.isWhiteSpace());
        
        character=new SqlCharacter('\t',1,2,3);
        assertTrue(character.isWhiteSpace());
    }
    
    @Test
    void getCategoryWorksAsExpected() throws Exception {
        SqlCharacter character=new SqlCharacter('x',1,2,3,Category.COMMENT);
        assertEquals(Category.COMMENT, character.getCategory());
    }
    
	@Test
	void insertingTokenMiddleWorksAsExpected() throws Exception {
	    Token token=new Token("T",Category.STATEMENT);
	    SqlString sqlString=SqlString.createSqlStringFromString("a%sb", Category.COMMAND, token);
	    
	    Token tokenResult=new Token(sqlString.getCharacters());
	    ArrayList<SqlCharacter> characters=tokenResult.getCharacters();
	    assertEquals(3,characters.size());
	    
	    for(int i=0;i<characters.size();i++) {
	        char c=characters.get(i).getChar();
	        if(i==0) {
	            assertEquals('a',c);
	        }else if(i==1){
                assertEquals('T',c);
	        }else if(i==2){
                assertEquals('b',c);
	        }
	    }
	}

    @Test
    void insertingTokenLeftWorksAsExpected() throws Exception {
        Token token=new Token("T",Category.STATEMENT);
        SqlString sqlString=SqlString.createSqlStringFromString("%sab", Category.COMMAND, token);
        
        Token tokenResult=new Token(sqlString.getCharacters());
        ArrayList<SqlCharacter> characters=tokenResult.getCharacters();
        assertEquals(3,characters.size());
        
        for(int i=0;i<characters.size();i++) {
            char c=characters.get(i).getChar();
            if(i==0) {
                assertEquals('T',c);
            }else if(i==1){
                assertEquals('a',c);
            }else if(i==2){
                assertEquals('b',c);
            }
        }
    }

    @Test
    void basicReadOperationsWorkAsExpected3() throws Exception {
        Token token=new Token("T",Category.STATEMENT);
        SqlString sqlString=SqlString.createSqlStringFromString("ab%s", Category.COMMAND, token);
        
        Token tokenResult=new Token(sqlString.getCharacters());
        ArrayList<SqlCharacter> characters=tokenResult.getCharacters();
        assertEquals(3,characters.size());
        
        for(int i=0;i<characters.size();i++) {
            char c=characters.get(i).getChar();
            if(i==0) {
                assertEquals('a',c);
            }else if(i==1){
                assertEquals('b',c);
            }else if(i==2){
                assertEquals('T',c);
            }
        }
    }

}
