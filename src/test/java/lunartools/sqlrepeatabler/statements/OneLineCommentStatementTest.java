package lunartools.sqlrepeatabler.statements;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.TestHelper;
import lunartools.sqlrepeatabler.core.model.OneLineCommentStatementFactory;
import lunartools.sqlrepeatabler.core.model.SqlBlock;
import lunartools.sqlrepeatabler.core.model.SqlScript;
import lunartools.sqlrepeatabler.core.model.Statement;
import lunartools.sqlrepeatabler.core.processing.StatementTokenizer;

class OneLineCommentStatementTest {
	private static final String TESTDATAFOLDER="/OneLineCommentStatement/";
	private OneLineCommentStatementFactory factory=new OneLineCommentStatementFactory();

	@Test
	void nonOneLineCommentIsNotAccepted() throws Exception {
		String filenameTestdata=	TESTDATAFOLDER+"OneNonOneLineCommentLine_Testdata.sql";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
        StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
        Statement statement=factory.createStatement(statementTokenizer);
        assertNull(statement);
	}

	@Test
	void oneLineCommentsAreAccepted() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"TwoOneLineCommentLines_Testdata.sql";
		String filenameExpecteddata=TESTDATAFOLDER+"TwoOneLineCommentLines_Expected.sql";
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
