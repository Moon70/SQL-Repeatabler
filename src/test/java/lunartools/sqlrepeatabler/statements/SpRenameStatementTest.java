package lunartools.sqlrepeatabler.statements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.TestHelper;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.statements.SpRenameStatementFactory;
import lunartools.sqlrepeatabler.statements.Statement;

class SpRenameStatementTest {
	private static final String TESTDATAFOLDER="/CommandSpRename/";
	private SpRenameStatementFactory factory=new SpRenameStatementFactory();

	@Test
	void nonSpRenameLineIsNotAccepted() throws Exception {
		String filenameTestdata=	TESTDATAFOLDER+"OneNonSpRenameLine_Testdata.txt";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertFalse(factory.match(sqlScript.peekLineAsString()));
	}

	@Test
	void spRename_RenameColumn_String() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"RenameColumn_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"RenameColumn_Expected.txt";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement statement=factory.createStatement(sqlScript);
		StringBuilder sb=new StringBuilder();
		statement.toSql(sb);
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

	@Test
	void spRename_RenameColumn() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"RenameColumn_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"RenameColumn_Expected.txt";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement statement=factory.createStatement(sqlScript);
		StringBuilder sb=new StringBuilder();
		statement.toSql(sb);
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

}
