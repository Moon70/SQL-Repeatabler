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

class OneLineCommentStatementTest {
	private static final String TESTDATAFOLDER="/OneLineCommentStatement/";
	private OneLineCommentStatementFactory factory=new OneLineCommentStatementFactory();

	@Test
	void nonOneLineCommentIsNotAccepted() throws Exception {
		String filenameTestdata=	TESTDATAFOLDER+"OneNonOneLineCommentLine_Testdata.txt";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertFalse(factory.match(sqlScript.peekLineAsString()));
	}

	@Test
	void oneLineCommentsAreAccepted() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"TwoOneLineCommentLines_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"TwoOneLineCommentLines_Expected.txt";
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
