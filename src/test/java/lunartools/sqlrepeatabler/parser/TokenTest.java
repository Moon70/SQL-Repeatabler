package lunartools.sqlrepeatabler.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class TokenTest {

	@Test
	void sqlStringConstructorIgnoresWhitespaceCorrectly() {
		SqlString sqlString=SqlString.createSqlStringFromString(" ABC ",Category.STATEMENT);
		Token token=new Token(sqlString);
		assertEquals("ABC",token.toString());
	}

	@Test
	void charactersConstructorIgnoresWhitespaceCorrectly() {
		ArrayList<SqlCharacter> characters=new ArrayList<>();
		characters.add(new SqlCharacter(' ', Category.STATEMENT));
		characters.add(new SqlCharacter('A', Category.STATEMENT));
		characters.add(new SqlCharacter('B', Category.STATEMENT));
		characters.add(new SqlCharacter('C', Category.STATEMENT));
		characters.add(new SqlCharacter(' ', Category.STATEMENT));
		Token token=new Token(characters);
		assertEquals("ABC",token.toString());
	}

	@Test
	void stringConstructorIgnoresWhitespaceCorrectly() {
		Token token=new Token(" ABC ",Category.STATEMENT);
		assertEquals("ABC",token.toString());
	}

	@Test
	void splitWorksCorrectly() {
		Token token=new Token("ABC%XYZ",Category.STATEMENT);
		Token[] tokenArray=token.split('%');
		assertEquals(2,tokenArray.length);
		assertEquals("ABC",tokenArray[0].toString());
		assertEquals("XYZ",tokenArray[1].toString());
	}
	
	@Test
	void removeEnclosingWorksCorrectly() {
		Token token=new Token("#ABC#",Category.STATEMENT);
		token.removeEnclosing('#');
		assertEquals("ABC",token.toString());
	}
	
	@Test
	void removeEnclosing2WorksCorrectly() {
		Token token=new Token("(ABC)",Category.STATEMENT);
		token.removeEnclosing('(',')');
		assertEquals("ABC",token.toString());
	}
	
	@Test
	void cloneWorksCorrectly() throws CloneNotSupportedException {
		Token token=new Token("ABC",Category.STATEMENT);
		Token tokenClone=token.clone();
		assertEquals("ABC",tokenClone.toString());
		token.getCharacters().get(1).setChar('X');
		assertEquals("AXC",token.toString());
		assertEquals("AXC",tokenClone.toString());
		
	}
	
	@Test
	void cloneWithoutDelimiterWithBracketsWorksCorrectly() throws CloneNotSupportedException {
		Token token=new Token("[ABC]",Category.STATEMENT);
		assertEquals("[ABC]",token.toString());
		Token tokenClone=token.cloneWithoutDelimiters();
		assertEquals("[ABC]",token.toString());
		assertEquals("ABC",tokenClone.toString());
	}
	
	@Test
	void cloneWithoutDelimiterWithQuotesWorksCorrectly() throws CloneNotSupportedException {
		Token token=new Token("\"ABC\"",Category.STATEMENT);
		assertEquals("\"ABC\"",token.toString());
		Token tokenClone=token.cloneWithoutDelimiters();
		assertEquals("\"ABC\"",token.toString());
		assertEquals("ABC",tokenClone.toString());
	}
	
	@Test
	void appendTokenWorksCorrectly() throws CloneNotSupportedException {
		Token token=new Token("ABC",Category.STATEMENT);
		Token tokenXyz=new Token("XYZ",Category.STATEMENT);
		token.append(tokenXyz);
		assertEquals("ABCXYZ",token.toString());
	}
	
	@Test
	void fixMySqlDelimiterWorksCorrectly() throws CloneNotSupportedException {
		Token token=new Token("`ABC`",Category.STATEMENT);
		token.fixMySql();
		assertEquals("[ABC]",token.toString());
		
		token=new Token("`ABC`.`XYZ`.`123`",Category.STATEMENT);
		token.fixMySql();
		assertEquals("[ABC].[XYZ].[123]",token.toString());
	}
	
	@Test
	void toUpperCaseWorksCorrectly() throws CloneNotSupportedException {
		Token token=new Token("foo",Category.STATEMENT);
		Token tokenUpperCase=token.toUpperCase();
		assertEquals("foo",token.toString());
		assertEquals("FOO",tokenUpperCase.toString());
	}
	
}
