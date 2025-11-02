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

class InsertIntoStatementTest {
	private static final String TESTDATAFOLDER="/InsertIntoStatement/";
	private InsertIntoStatementFactory factory=new InsertIntoStatementFactory();

	@Test
	void nonInsertIntoLineIsNotAccepted() throws Exception {
		String filenameTestdata=	TESTDATAFOLDER+"OneNonInsertIntoLine_Testdata.txt";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertFalse(factory.match(sqlScript.peekLineAsString()));
	}

	@Test
	void insert_OneColumn_String() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"InsertInto_OneRow_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"InsertInto_OneRow_Expected.txt";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement statement=factory.createStatement(sqlScript);
		StringBuilder sb=new StringBuilder();
		statement.toSql(sb);
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

	@Test
	void insert_OneColumn() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"InsertInto_OneRow_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"InsertInto_OneRow_Expected.txt";
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
	void insert_ThreeColumns_String() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"InsertInto_ThreeRows_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"InsertInto_ThreeRows_Expected.txt";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement statement=factory.createStatement(sqlScript);
		StringBuilder sb=new StringBuilder();
		statement.toSql(sb);
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

	@Test
	void insert_ThreeColumns() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"InsertInto_ThreeRows_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"InsertInto_ThreeRows_Expected.txt";
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
	void insert_OneColumn_ColumnWithFunction_String() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"InsertInto_OneRow_ColumnWithFunction_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"InsertInto_OneRow_ColumnWithFunction_Expected.txt";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement statement=factory.createStatement(sqlScript);
		StringBuilder sb=new StringBuilder();
		statement.toSql(sb);
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

	@Test
	void insert_OneColumn_ColumnWithFunction() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"InsertInto_OneRow_ColumnWithFunction_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"InsertInto_OneRow_ColumnWithFunction_Expected.txt";
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
