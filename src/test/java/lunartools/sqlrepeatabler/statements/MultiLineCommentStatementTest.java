package lunartools.sqlrepeatabler.statements;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.TestHelper;
import lunartools.sqlrepeatabler.core.model.MultiLineCommentStatementFactory;
import lunartools.sqlrepeatabler.core.model.SqlBlock;
import lunartools.sqlrepeatabler.core.model.SqlScript;
import lunartools.sqlrepeatabler.core.model.Statement;
import lunartools.sqlrepeatabler.core.processing.StatementTokenizer;

class MultiLineCommentStatementTest {
	private static final String TESTDATAFOLDER="/MultiLineCommentStatement/";
	private MultiLineCommentStatementFactory factory=new MultiLineCommentStatementFactory();

	@Test
	void nonMultiLineCommentIsNotAccepted() throws Exception {
		String filenameTestdata=	TESTDATAFOLDER+"OneNonMultiLineCommentLine_Testdata.sql";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
        StatementTokenizer statementTokenizer=sqlScript.consumeStatement();
        Statement statement=factory.createStatement(statementTokenizer);
        assertNull(statement);
	}

	@Test
	void multiLineCommentIsAccepted() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"TwoMultiLineCommentLines_Testdata.sql";
		String filenameExpecteddata=TESTDATAFOLDER+"TwoMultiLineCommentLines_Expected.sql";
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
