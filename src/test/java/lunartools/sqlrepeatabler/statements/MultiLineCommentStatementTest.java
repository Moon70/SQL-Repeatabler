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

class MultiLineCommentStatementTest {
	private static final String TESTDATAFOLDER="/MultiLineCommentStatement/";
	private MultiLineCommentStatementFactory factory=new MultiLineCommentStatementFactory();

	@Test
	void nonMultiLineCommentIsNotAccepted() throws Exception {
		String filenameTestdata=	TESTDATAFOLDER+"OneNonMultiLineCommentLine_Testdata.txt";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertFalse(factory.match(sqlScript.peekLineAsString()));
	}

	@Test
	void multiLineCommentIsAccepted() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"TwoMultiLineCommentLines_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"TwoMultiLineCommentLines_Expected.txt";
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
