package lunartools.sqlrepeatabler.statements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.TestHelper;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.util.Tools;

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
		ArrayList<SqlString> sqlCharacterLines=new ArrayList<>();
		statement.toSqlCharacters(sqlCharacterLines);
		StringBuilder sb=Tools.toStringBuilder(sqlCharacterLines);
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
		ArrayList<SqlString> sqlCharacterLines=new ArrayList<>();
		statement.toSqlCharacters(sqlCharacterLines);
		StringBuilder sb=Tools.toStringBuilder(sqlCharacterLines);
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
		ArrayList<SqlString> sqlCharacterLines=new ArrayList<>();
		statement.toSqlCharacters(sqlCharacterLines);
		StringBuilder sb=Tools.toStringBuilder(sqlCharacterLines);
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
		ArrayList<SqlString> sqlCharacterLines=new ArrayList<>();
		statement.toSqlCharacters(sqlCharacterLines);
		StringBuilder sb=Tools.toStringBuilder(sqlCharacterLines);
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

}
