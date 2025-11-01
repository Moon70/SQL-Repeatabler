package lunartools.sqlrepeatabler.statements;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.TestHelper;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.statements.SetIdentityInsertStatementFactory;
import lunartools.sqlrepeatabler.statements.Statement;

class CommandSetIdentityInsertTest {
	private static final String TESTDATAFOLDER="/CommandSetIdentityInsert/";
	private SetIdentityInsertStatementFactory factory=new SetIdentityInsertStatementFactory();

	@Test
	void nonInsertIntoLineIsNotAccepted() throws Exception {
		String filenameTestdata=	TESTDATAFOLDER+"OneNonSetIdentityInsertLine_Testdata.txt";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertFalse(factory.match(sqlScript.peekLineAsString()));
	}

	@Test
	void setIdentityInsert_On() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"SetIdentityInsert_On_Testdata.txt";

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement sqlSegment=factory.createStatement(sqlScript);
		StringBuilder sb=new StringBuilder();
		sqlSegment.toSql(sb);
		assertTrue(TestHelper.removeCR(sb).length()==0);
	}

	@Test
	void setIdentityInsert_Off() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"SetIdentityInsert_Off_Testdata.txt";

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement sqlSegment=factory.createStatement(sqlScript);
		StringBuilder sb=new StringBuilder();
		sqlSegment.toSql(sb);
		assertTrue(TestHelper.removeCR(sb).length()==0);
	}

}
