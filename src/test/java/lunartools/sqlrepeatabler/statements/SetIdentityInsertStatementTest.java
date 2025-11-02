package lunartools.sqlrepeatabler.statements;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.TestHelper;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.parser.SqlString;

class SetIdentityInsertStatementTest {
	private static final String TESTDATAFOLDER="/SetIdentityInsertStatement/";
	private SetIdentityInsertStatementFactory factory=new SetIdentityInsertStatementFactory();

	@Test
	void nonInsertIntoLineIsNotAccepted() throws Exception {
		String filenameTestdata=	TESTDATAFOLDER+"OneNonSetIdentityInsertLine_Testdata.txt";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertFalse(factory.match(sqlScript.peekLineAsString()));
	}

	@Test
	void setIdentityInsert_On_String() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"SetIdentityInsert_On_Testdata.txt";

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement statement=factory.createStatement(sqlScript);
		StringBuilder sb=new StringBuilder();
		statement.toSql(sb);
		assertTrue(TestHelper.removeCR(sb).length()==0);
	}

	@Test
	void setIdentityInsert_On() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"SetIdentityInsert_On_Testdata.txt";

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement statement=factory.createStatement(sqlScript);
		ArrayList<SqlString> sqlCharacterLines=new ArrayList<>();
		statement.toSqlCharacters(sqlCharacterLines);
		assertTrue(sqlCharacterLines.size()==0);
	}

	@Test
	void setIdentityInsert_Off_String() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"SetIdentityInsert_Off_Testdata.txt";

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement statement=factory.createStatement(sqlScript);
		StringBuilder sb=new StringBuilder();
		statement.toSql(sb);
		assertTrue(TestHelper.removeCR(sb).length()==0);
	}

	@Test
	void setIdentityInsert_Off() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"SetIdentityInsert_Off_Testdata.txt";

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement statement=factory.createStatement(sqlScript);
		ArrayList<SqlString> sqlCharacterLines=new ArrayList<>();
		statement.toSqlCharacters(sqlCharacterLines);
		assertTrue(sqlCharacterLines.size()==0);
	}

}
