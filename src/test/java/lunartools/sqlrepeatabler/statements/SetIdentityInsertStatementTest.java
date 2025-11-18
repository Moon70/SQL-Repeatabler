package lunartools.sqlrepeatabler.statements;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.TestHelper;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.statement.SetIdentityInsertStatementFactory;
import lunartools.sqlrepeatabler.statement.Statement;

class SetIdentityInsertStatementTest {
	private static final String TESTDATAFOLDER="/SetIdentityInsertStatement/";
	private SetIdentityInsertStatementFactory factory=new SetIdentityInsertStatementFactory();

	@Test
	void nonInsertIntoLineIsNotAccepted() throws Exception {
		String filenameTestdata=	TESTDATAFOLDER+"OneNonSetIdentityInsertLine_Testdata.sql";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertFalse(factory.match(sqlScript.peekLineAsString()));
	}

	@Test
	void setIdentityInsert_On() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"SetIdentityInsert_On_Testdata.sql";

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement statement=factory.createStatement(sqlScript);
		SqlBlock sqlCharacterLines=new SqlBlock();
		statement.toSqlCharacters(sqlCharacterLines);
		assertTrue(sqlCharacterLines.size()==0);
	}

	@Test
	void setIdentityInsert_Off() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"SetIdentityInsert_Off_Testdata.sql";

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement statement=factory.createStatement(sqlScript);
		SqlBlock sqlBlock=new SqlBlock();
		statement.toSqlCharacters(sqlBlock);
		assertTrue(sqlBlock.size()==0);
	}

}
