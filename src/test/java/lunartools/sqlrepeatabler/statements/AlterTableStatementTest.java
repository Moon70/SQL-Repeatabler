package lunartools.sqlrepeatabler.statements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.TestHelper;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.statement.AlterTableStatementFactory;
import lunartools.sqlrepeatabler.statement.Statement;

class AlterTableStatementTest {
	private static final String TESTDATAFOLDER="/AlterTableStatement/";
	private AlterTableStatementFactory factory=new AlterTableStatementFactory();

	@Test
	void nonAlterTableIsNotAccepted() throws Exception {
		String filenameTestdata=	TESTDATAFOLDER+"NonAlterTableLine_Testdata.sql";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertFalse(factory.match(sqlScript.peekLineAsString()));
	}

	@Test
	void alterTable_Column_AddOne() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"Column_AddOne_Testdata.sql";
		String filenameExpecteddata=TESTDATAFOLDER+"Column_AddOne_Expected.sql";
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
	void alterTable_Column_AddOne_OneLine() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"Column_AddOne_OneLine_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"Column_AddOne_OneLine_Expected.txt";
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
	void alterTable_Column_AddThree() throws Exception{
		String filenameExpecteddata=TESTDATAFOLDER+"Column_AddThree_Expected.sql";
		String filenameTestdata=	TESTDATAFOLDER+"Column_AddThree_Testdata.sql";
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
	void alterTable_Constraint_FK_AddOne() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"Constraint_FK_AddOne_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"Constraint_FK_AddOne_Expected.txt";
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
	void alterTable_Constraint_Unique_AddOne() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"Constraint_Unique_AddOne_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"Constraint_Unique_AddOne_Expected.txt";
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
	void alterTable_Constraint_FK_AddTwo() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"Constraint_FK_AddTwo_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"Constraint_FK_AddTwo_Expected.txt";
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
	void alterTable_DropOneColumnWorksAsExpected() throws Exception{
		String filenameExpecteddata=TESTDATAFOLDER+"Column_DropOne_Expected.sql";
		String filenameTestdata=	TESTDATAFOLDER+"Column_DropOne_Testdata.sql";
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
	void alterTable_DropThreeColumnWorksAsExpectedg() throws Exception{
		String filenameExpecteddata=TESTDATAFOLDER+"Column_DropThree_Expected.sql";
		String filenameTestdata=	TESTDATAFOLDER+"Column_DropThree_Testdata.sql";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement statement=factory.createStatement(sqlScript);
		SqlBlock sqlBlocks=new SqlBlock();
		statement.toSqlCharacters(sqlBlocks);
		StringBuilder sb=sqlBlocks.toStringBuilder();
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

	@Test
	void alterTable_DropOneConstraintWorksAsExpected() throws Exception{
		String filenameExpecteddata=TESTDATAFOLDER+"Constraint_DropOne_Expected.txt";
		String filenameTestdata=	TESTDATAFOLDER+"Constraint_DropOne_Testdata.txt";
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
	void alterTable_AlterColumn_OneColumn() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"AlterTable_AlterColumn_OneColumn_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"AlterTable_AlterColumn_OneColumn_Expected.txt";
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
    void alterTable_unsupported_modify() throws Exception{
        String filenameTestdata=    TESTDATAFOLDER+"AlterTable_ModifyColumn_Testdata.txt";
        String filenameExpecteddata=TESTDATAFOLDER+"AlterTable_ModifyColumn_Expected.txt";
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
    void alterTable_missingSemicolon() throws Exception{
        String filenameTestdata=    TESTDATAFOLDER+"AlterTable_AlterColumn_MissingSemicolon_Testdata.txt";
        String filenameExpecteddata=TESTDATAFOLDER+"AlterTable_AlterColumn_MissingSemicolon_Expected.txt";
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
    void alterTable_invalidAddCommandIsIgnoredWhenAddingTwoColumns() throws Exception {
        String filenameTestdata=    TESTDATAFOLDER+"Column_InvalidAddTwoColumnsThrowsAnException_Testdata.txt";
        String filenameExpecteddata=TESTDATAFOLDER+"Column_InvalidAddTwoColumnsThrowsAnException_Expected.txt";
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
