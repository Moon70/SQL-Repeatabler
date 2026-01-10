package lunartools.sqlrepeatabler.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.core.model.Category;
import lunartools.sqlrepeatabler.core.model.SqlCharacter;
import lunartools.sqlrepeatabler.core.model.SqlString;
import lunartools.sqlrepeatabler.core.model.Token;

class SqlStringTest {

    @Test
    void createSqlStringFromStringWorksAsExpected() {
        SqlString sqlString=SqlString.createSqlStringFromString("abc", Category.COMMAND);
        
        ArrayList<SqlCharacter> characters=sqlString.getCharacters();
        assertEquals(3,characters.size());
        
        assertEquals("abc",sqlString.toString());
    }

    @Test
    void createSqlStringFromStringWithTokenWorksAsExpected() {
        Token token=new Token("X",Category.COMMAND);
        SqlString sqlString=SqlString.createSqlStringFromString("a%sb", Category.COMMAND,token);
        ArrayList<SqlCharacter> characters=sqlString.getCharacters();
        assertEquals(3,characters.size());
        
        assertEquals("aXb",sqlString.toString());
    }

    @Test
    void createSqlStringFromStringAndMissingTokenThrowsExpectedException() {
        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class,()->SqlString.createSqlStringFromString("a%sb", Category.COMMAND));
        assertEquals("String fragment count (2) does not match token count (0)",exception.getMessage());
    }

    @Test
    void createSqlStringFromStringAndTooMuchTokenThrowsExpectedException() {
        Token token=new Token("X",Category.COMMAND);
        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class,()->SqlString.createSqlStringFromString("ab", Category.COMMAND,token));
        assertEquals("String fragment count (1) does not match token count (1)",exception.getMessage());
    }

    @Test
    void endsWithSemicolonIgnoreWhiteSpaceWorksAsExpected() {
        SqlString sqlString=SqlString.createSqlStringFromString("abc;", Category.COMMAND);
        assertTrue(sqlString.endsWithSemicolonIgnoreWhiteSpace());

        sqlString=SqlString.createSqlStringFromString("abc; ", Category.COMMAND);
        assertTrue(sqlString.endsWithSemicolonIgnoreWhiteSpace());

        sqlString=SqlString.createSqlStringFromString("abc", Category.COMMAND);
        assertFalse(sqlString.endsWithSemicolonIgnoreWhiteSpace());
    }

    @Test
    void startsWithIgnoreCaseWorksAsExpected() {
        SqlString sqlString=SqlString.createSqlStringFromString("abcdef;", Category.COMMAND);
        assertTrue(sqlString.startsWithIgnoreCase("abc"));
        assertTrue(sqlString.startsWithIgnoreCase("AbC"));
    }

    
}
