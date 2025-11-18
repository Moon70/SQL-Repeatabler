package lunartools.sqlrepeatabler.statements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.TestHelper;
import lunartools.sqlrepeatabler.parser.SqlBlock;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.statement.OneLineCommentStatementFactory;
import lunartools.sqlrepeatabler.statement.Statement;

class OneLineCommentStatementTest {
	private static final String TESTDATAFOLDER="/OneLineCommentStatement/";
	private OneLineCommentStatementFactory factory=new OneLineCommentStatementFactory();

	@Test
	void nonOneLineCommentIsNotAccepted() throws Exception {
		String filenameTestdata=	TESTDATAFOLDER+"OneNonOneLineCommentLine_Testdata.sql";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertFalse(factory.match(sqlScript.peekLineAsString()));
	}

	@Test
	void oneLineCommentsAreAccepted() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"TwoOneLineCommentLines_Testdata.sql";
		String filenameExpecteddata=TESTDATAFOLDER+"TwoOneLineCommentLines_Expected.sql";
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
