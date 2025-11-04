package lunartools.sqlrepeatabler.statements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.TestHelper;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlScript;

class CreateTableStatementTest {
	private static final String TESTDATAFOLDER="/CreateTableStatement/";
	private CreateTableStatementFactory factory=new CreateTableStatementFactory();

	@Test
	void nonCreateTableIsNotAccepted() throws Exception {
		String filenameTestdata=	TESTDATAFOLDER+"NonCreateTableLine_Testdata.txt";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertFalse(factory.match(sqlScript.peekLineAsString()));
	}

	@Test
	void createTable_SquareBracketDelimiterIsAccepted() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"CreateOneTable_DelimiterSquareBrackets_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"CreateOneTable_DelimiterSquareBrackets_Expected.txt";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement statement=factory.createStatement(sqlScript);
		SqlBlock sqlBlock=new SqlBlock();
		statement.toSqlCharacters(sqlBlock);
		StringBuilder sb=sqlBlock.toStringBuilder();
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

	@Test
	void createTable_QuoteDelimiterIsAccepted() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"CreateOneTable_DelimiterQuotes_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"CreateOneTable_DelimiterQuotes_Expected.txt";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement statement=factory.createStatement(sqlScript);
		SqlBlock sqlBlock=new SqlBlock();
		statement.toSqlCharacters(sqlBlock);
		StringBuilder sb=sqlBlock.toStringBuilder();
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

	@Test
	void createTable_BacktickDelimiterIsAccepted() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"CreateOneTable_DelimiterBackticks_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"CreateOneTable_DelimiterBackticks_Expected.txt";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement statement=factory.createStatement(sqlScript);
		SqlBlock sqlBlock=new SqlBlock();
		statement.toSqlCharacters(sqlBlock);
		StringBuilder sb=sqlBlock.toStringBuilder();
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

	@Test
	void createTable_NoDelimiterIsAccepted() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"CreateOneTable_NoDelimiter_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"CreateOneTable_NoDelimiter_Expected.txt";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement statement=factory.createStatement(sqlScript);
		SqlBlock sqlBlock=new SqlBlock();
		statement.toSqlCharacters(sqlBlock);
		StringBuilder sb=sqlBlock.toStringBuilder();
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

}
