package lunartools.sqlrepeatabler.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class SqlCharacterTest {

    @Test
    void getCharWorksAsExpected() throws Exception {
        SqlCharacter character=new SqlCharacter('x');
        assertEquals('x',character.getChar());
    }
    
    @Test
    void isSpaceWorksAsExpected() throws Exception {
        SqlCharacter character=new SqlCharacter('x');
        assertFalse(character.isSpace());
        
        character=new SqlCharacter(' ');
        assertTrue(character.isSpace());
    }
    
    @Test
    void isSemicolonWorksAsExpected() throws Exception {
        SqlCharacter character=new SqlCharacter('x');
        assertFalse(character.isSemicolon());
        
        character=new SqlCharacter(';');
        assertTrue(character.isSemicolon());
    }
    
    @Test
    void isWhiteSpaceWorksAsExpected() throws Exception {
        SqlCharacter character=new SqlCharacter('x');
        assertFalse(character.isWhiteSpace());
        
        character=new SqlCharacter(' ');
        assertTrue(character.isWhiteSpace());
        
        character=new SqlCharacter('\t');
        assertTrue(character.isWhiteSpace());
    }
    
    @Test
    void getCategoryWorksAsExpected() throws Exception {
        SqlCharacter character=new SqlCharacter('x',Category.COMMENT);
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
