package lunartools.sqlrepeatabler.statements;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.TestHelper;
import lunartools.sqlrepeatabler.core.model.SqlBlock;
import lunartools.sqlrepeatabler.core.model.SqlScript;
import lunartools.sqlrepeatabler.core.model.Statement;
import lunartools.sqlrepeatabler.core.model.UpdateStatementFactory;
import lunartools.sqlrepeatabler.core.processing.StatementTokenizer;

class UpdateStatementTest {
	private static final String TESTDATAFOLDER="/UpdateStatement/";
	private UpdateStatementFactory factory=new UpdateStatementFactory();

	@Test
	void nonUpdateLineIsNotAccepted() throws Exception {
		String filenameTestdata=	TESTDATAFOLDER+"OneNonUpdateLine_Testdata.sql";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuilder(filenameTestdata));
        StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
        Statement statement=factory.createStatement(statementTokenizer);
        assertNull(statement);
	}

	@Test
	void update() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"Update_Testdata.sql";
		String filenameExpecteddata=TESTDATAFOLDER+"Update_Expected.sql";
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

}
