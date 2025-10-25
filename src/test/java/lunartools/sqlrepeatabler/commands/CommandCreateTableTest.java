package lunartools.sqlrepeatabler.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.TestHelper;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.statements.CreateTableStatementFactory;
import lunartools.sqlrepeatabler.statements.Statement;

class CommandCreateTableTest {
	private static final String TESTDATAFOLDER="/CommandCreateTable/";
	private CreateTableStatementFactory factory=new CreateTableStatementFactory();

	@Test
	void nonCreateTableIsNotAccepted() throws Exception {
		String filenameTestdata=	TESTDATAFOLDER+"NonCreateTableLine_Testdata.txt";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertFalse(factory.match(sqlScript.peekLine()));
	}

	@Test
	void createTable_SquareBracketDelimiterIsAccepted() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"CreateOneTable_DelimiterSquareBrackets_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"CreateOneTable_DelimiterSquareBrackets_Expected.txt";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLine()));

		Statement sqlSegment=factory.createStatement(sqlScript);
		StringBuilder sb=new StringBuilder();
		sqlSegment.toSql(sb);
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

	@Test
	void createTable_QuoteDelimiterIsAccepted() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"CreateOneTable_DelimiterQuotes_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"CreateOneTable_DelimiterQuotes_Expected.txt";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLine()));

		Statement sqlSegment=factory.createStatement(sqlScript);
		StringBuilder sb=new StringBuilder();
		sqlSegment.toSql(sb);
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

	@Test
	void createTable_BacktickDelimiterIsAccepted() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"CreateOneTable_DelimiterBackticks_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"CreateOneTable_DelimiterBackticks_Expected.txt";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLine()));

		Statement sqlSegment=factory.createStatement(sqlScript);
		StringBuilder sb=new StringBuilder();
		sqlSegment.toSql(sb);
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

	@Test
	void createTable_NoDelimiterIsAccepted() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"CreateOneTable_NoDelimiter_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"CreateOneTable_NoDelimiter_Expected.txt";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLine()));

		Statement sqlSegment=factory.createStatement(sqlScript);
		StringBuilder sb=new StringBuilder();
		sqlSegment.toSql(sb);
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

}
