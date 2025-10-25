package lunartools.sqlrepeatabler.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SqlScriptTest {

	@Test
	void basicReadOperationsWorkAsExpected() throws Exception {
		StringBuilder sb=new StringBuilder();
		sb.append("line 1").append(SqlParser.CRLF);
		sb.append("line 2").append(SqlParser.CRLF);
		SqlScript sqlScript=SqlScript.createInstance(sb);

		assertEquals(0,sqlScript.getIndex());
		assertTrue(sqlScript.hasCurrentLine());
		assertEquals("line 1",sqlScript.peekLineAsString());
		assertEquals(0,sqlScript.getIndex());
		assertEquals("line 1",sqlScript.peekLineAsString());

		//    	assertEquals(1,sqlScript.getIndex());
		//    	assertTrue(sqlScript.hasCurrentLine());
		//    	assertEquals("line 2",sqlScript.peekLine());
		//
		//    	assertEquals(-1,sqlScript.getIndex());
		//    	assertFalse(sqlScript.hasCurrentLine());
		//    	assertNull(sqlScript.peekLine());
	}

}
