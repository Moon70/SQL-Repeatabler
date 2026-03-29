package lunartools.sqlrepeatabler.statements;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.TestHelper;
import lunartools.sqlrepeatabler.core.model.InsertStatementFactory;
import lunartools.sqlrepeatabler.core.model.SqlBlock;
import lunartools.sqlrepeatabler.core.model.SqlScript;
import lunartools.sqlrepeatabler.core.model.Statement;
import lunartools.sqlrepeatabler.core.model.WhitespaceLineStatement;
import lunartools.sqlrepeatabler.core.processing.StatementTokenizer;

class InsertStatementTest {
	private static final String TESTDATAFOLDER="/InsertStatement/";
	private InsertStatementFactory factory=new InsertStatementFactory();

	@Test
	void nonInsertLineIsNotAccepted() throws Exception {
		String filenameTestdata=	TESTDATAFOLDER+"OneNonInsertLine_Testdata.sql";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuilder(filenameTestdata));
        StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
        Statement statement=factory.createStatement(statementTokenizer);
		assertNull(statement);
	}

	@Test
	void insert_OneColumn() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"Insert_OneRow_Testdata.sql";
		String filenameExpecteddata=TESTDATAFOLDER+"Insert_OneRow_Expected.sql";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuilder(filenameTestdata));
        StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
        Statement statement=factory.createStatement(statementTokenizer);
        assertNotNull(statement);

		SqlBlock sqlBlock=new SqlBlock();
		statement.toSqlCharacters(sqlBlock);
		StringBuilder sb=sqlBlock.toStringBuilder();
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

	@Test
	void insert_ThreeColumns() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"Insert_ThreeRows_Testdata.sql";
		String filenameExpecteddata=TESTDATAFOLDER+"Insert_ThreeRows_Expected.sql";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuilder(filenameTestdata));
        StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
        Statement statement=factory.createStatement(statementTokenizer);
        assertNotNull(statement);

		SqlBlock sqlBlock=new SqlBlock();
		statement.toSqlCharacters(sqlBlock);
		StringBuilder sb=sqlBlock.toStringBuilder();
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

	@Test
	void insert_OneColumn_ColumnWithFunction() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"Insert_OneRow_ColumnWithFunction_Testdata.sql";
		String filenameExpecteddata=TESTDATAFOLDER+"Insert_OneRow_ColumnWithFunction_Expected.sql";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuilder(filenameTestdata));
        StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
        Statement statement=factory.createStatement(statementTokenizer);
        assertNotNull(statement);

		SqlBlock sqlBlock=new SqlBlock();
		statement.toSqlCharacters(sqlBlock);
		StringBuilder sb=sqlBlock.toStringBuilder();
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

    @Test
    void insert_ThreeColumnsWithoutSemicolon() throws Exception{
        String filenameTestdata=    TESTDATAFOLDER+"Insert_ThreeRowsWithoutSemicolon_Testdata.sql";
        String filenameExpecteddata=TESTDATAFOLDER+"Insert_ThreeRowsWithoutSemicolon_Expected.sql";
        String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

        SqlBlock sqlBlock=new SqlBlock();

        SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuilder(filenameTestdata));
        StatementTokenizer statementTokenizer=sqlScript.consumeStatement();

        Statement statement=factory.createStatement(statementTokenizer);
        assertNotNull(statement);
        statement.toSqlCharacters(sqlBlock);
        WhitespaceLineStatement.WHITESPACE_LINE.toSqlCharacters(sqlBlock);
        
        statement=factory.createStatement(statementTokenizer);
        assertNotNull(statement);
        statement.toSqlCharacters(sqlBlock);
        WhitespaceLineStatement.WHITESPACE_LINE.toSqlCharacters(sqlBlock);

        statement=factory.createStatement(statementTokenizer);
        assertNotNull(statement);
        statement.toSqlCharacters(sqlBlock);
        
        StringBuilder sb=sqlBlock.toStringBuilder();
        assertEquals(expected,TestHelper.removeCR(sb).toString());
    }

}
