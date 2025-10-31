package lunartools.sqlrepeatabler.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class SqlCharacterTest {

	@Test
	void insertingTokenMiddleWorksAsExpected() throws Exception {
	    Token token=new Token("T",Category.STATEMENT);
	    ArrayList<SqlCharacter> test=SqlCharacter.createCharactersFromString("a%sb", Category.COMMAND, token);
	    
	    Token tokenResult=new Token(test);
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
        ArrayList<SqlCharacter> test=SqlCharacter.createCharactersFromString("%sab", Category.COMMAND, token);
        
        Token tokenResult=new Token(test);
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
        ArrayList<SqlCharacter> test=SqlCharacter.createCharactersFromString("ab%s", Category.COMMAND, token);
        
        Token tokenResult=new Token(test);
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
