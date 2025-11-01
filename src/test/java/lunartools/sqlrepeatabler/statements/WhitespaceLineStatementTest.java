package lunartools.sqlrepeatabler.statements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.TestHelper;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.statements.Statement;
import lunartools.sqlrepeatabler.statements.WhitespaceLineStatementFactory;

class WhitespaceLineStatementTest {
	private static final String TESTDATAFOLDER="/CommandWhitespaceLine/";
	private WhitespaceLineStatementFactory factory=new WhitespaceLineStatementFactory();

	@Test
	void nonWhitespaceLineIsNotAccepted() throws Exception {
		String filenameTestdata=	TESTDATAFOLDER+"OneNonWhitespaceLine_Testdata.txt";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertFalse(factory.match(sqlScript.peekLineAsString()));
	}

	@Test
	void whitespaceLinesAreAccepted_String() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"TwoWhitespaceLines_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"TwoWhitespaceLines_Expected.txt";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement sqlSegment=factory.createStatement(sqlScript);
		StringBuilder sb=new StringBuilder();
		sqlSegment.toSql(sb);
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

	@Test
	void whitespaceLinesAreAccepted() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"TwoWhitespaceLines_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"TwoWhitespaceLines_Expected.txt";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement sqlSegment=factory.createStatement(sqlScript);
		StringBuilder sb=new StringBuilder();
		sqlSegment.toSql(sb);
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

}
