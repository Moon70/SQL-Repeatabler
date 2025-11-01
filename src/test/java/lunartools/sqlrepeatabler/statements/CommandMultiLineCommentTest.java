package lunartools.sqlrepeatabler.statements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.TestHelper;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.parser.SqlString;
import lunartools.sqlrepeatabler.statements.MultiLineCommentStatementFactory;
import lunartools.sqlrepeatabler.statements.Statement;

class CommandMultiLineCommentTest {
	private static final String TESTDATAFOLDER="/CommandMultiLineComment/";
	private MultiLineCommentStatementFactory factory=new MultiLineCommentStatementFactory();

	@Test
	void nonMultiLineCommentIsNotAccepted() throws Exception {
		String filenameTestdata=	TESTDATAFOLDER+"OneNonMultiLineCommentLine_Testdata.txt";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertFalse(factory.match(sqlScript.peekLineAsString()));
	}

	@Test
	void multiLineCommentIsAccepted_String() throws Exception{
		String filenameTestdata=	TESTDATAFOLDER+"TwoMultiLineCommentLines_Testdata.txt";
		String filenameExpecteddata=TESTDATAFOLDER+"TwoMultiLineCommentLines_Expected.txt";
		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLineAsString()));

		Statement sqlSegment=factory.createStatement(sqlScript);
		StringBuilder sb=new StringBuilder();
		sqlSegment.toSql(sb);
		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

	@Test
	void multiLineCommentIsAccepted() throws Exception{
//		String filenameTestdata=	TESTDATAFOLDER+"TwoMultiLineCommentLines_Testdata.txt";
//		String filenameExpecteddata=TESTDATAFOLDER+"TwoMultiLineCommentLines_Expected.txt";
//		String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();
//
//		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
//		assertTrue(factory.match(sqlScript.peekLineAsString()));
//
//		Statement sqlSegment=factory.createStatement(sqlScript);
//		ArrayList<SqlString> sqlCharacters=new ArrayList<>();
//		sqlSegment.toSqlCharacters(sqlCharacters);
//		assertEquals(expected,TestHelper.removeCR(sb).toString());
	}

}
