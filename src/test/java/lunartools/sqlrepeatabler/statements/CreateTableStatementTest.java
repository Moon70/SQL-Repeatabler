package lunartools.sqlrepeatabler.statements;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.TestHelper;
import lunartools.sqlrepeatabler.core.model.CreateTableStatementFactory;
import lunartools.sqlrepeatabler.core.model.SqlBlock;
import lunartools.sqlrepeatabler.core.model.SqlScript;
import lunartools.sqlrepeatabler.core.model.Statement;
import lunartools.sqlrepeatabler.core.processing.StatementTokenizer;

class CreateTableStatementTest {
	private static final String TESTDATAFOLDER="/CreateTableStatement/";
	private CreateTableStatementFactory factory=new CreateTableStatementFactory();

	@Test
	void nonCreateTableIsNotAccepted() throws Exception {
		String filenameTestdata=	TESTDATAFOLDER+"NonCreateTableLine_Testdata.sql";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
        StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
        Statement statement=factory.createStatement(statementTokenizer);
        assertNull(statement);
	}

	@Test
	void createTable_SquareBracketDelimiterIsAccepted() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"CreateOneTable_DelimiterSquareBrackets_Testdata.sql";
		String filenameExpecteddata=TESTDATAFOLDER+"CreateOneTable_DelimiterSquareBrackets_Expected.sql";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
        StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
        Statement statement=factory.createStatement(statementTokenizer);
        assertNotNull(statement);

		SqlBlock sqlBlock=new SqlBlock();
		statement.toSqlCharacters(sqlBlock);
		StringBuilder sb=sqlBlock.toStringBuilder();
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

	@Test
	void createTable_QuoteDelimiterIsAccepted() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"CreateOneTable_DelimiterQuotes_Testdata.sql";
		String filenameExpecteddata=TESTDATAFOLDER+"CreateOneTable_DelimiterQuotes_Expected.sql";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
        StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
        Statement statement=factory.createStatement(statementTokenizer);
        assertNotNull(statement);

		SqlBlock sqlBlock=new SqlBlock();
		statement.toSqlCharacters(sqlBlock);
		StringBuilder sb=sqlBlock.toStringBuilder();
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

	@Test
	void createTable_BacktickDelimiterIsAccepted() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"CreateOneTable_DelimiterBackticks_Testdata.sql";
		String filenameExpecteddata=TESTDATAFOLDER+"CreateOneTable_DelimiterBackticks_Expected.sql";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
        StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
        Statement statement=factory.createStatement(statementTokenizer);
        assertNotNull(statement);

		SqlBlock sqlBlock=new SqlBlock();
		statement.toSqlCharacters(sqlBlock);
		StringBuilder sb=sqlBlock.toStringBuilder();
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

	@Test
	void createTable_NoDelimiterIsAccepted() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"CreateOneTable_NoDelimiter_Testdata.sql";
		String filenameExpecteddata=TESTDATAFOLDER+"CreateOneTable_NoDelimiter_Expected.sql";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
        StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
        Statement statement=factory.createStatement(statementTokenizer);
        assertNotNull(statement);

		SqlBlock sqlBlock=new SqlBlock();
		statement.toSqlCharacters(sqlBlock);
		StringBuilder sb=sqlBlock.toStringBuilder();
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

}
