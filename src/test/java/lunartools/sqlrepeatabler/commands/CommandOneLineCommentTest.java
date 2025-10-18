package lunartools.sqlrepeatabler.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.TestHelper;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.statements.OneLineCommentStatementFactory;
import lunartools.sqlrepeatabler.statements.Statement;

class CommandOneLineCommentTest {
	private static final String TESTDATAFOLDER="/CommandOneLineComment/";
    private OneLineCommentStatementFactory factory=new OneLineCommentStatementFactory();
    
    @Test
    void nonOneLineCommentIsNotAccepted() throws Exception {
    	String filenameTestdata=	TESTDATAFOLDER+"OneNonOneLineCommentLine_Testdata.txt";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
   		assertFalse(factory.match(sqlScript.peekLine()));
    }

    @Test
    void oneLineCommentsAreAccepted() throws Exception{
    	String filenameTestdata=	TESTDATAFOLDER+"TwoOneLineCommentLines_Testdata.txt";
    	String filenameExpecteddata=TESTDATAFOLDER+"TwoOneLineCommentLines_Expected.txt";
    	String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLine()));

		Statement sqlSegment=factory.createSqlSegment(sqlScript);
		StringBuilder sb=new StringBuilder();
		sqlSegment.toSql(sb);
		assertEquals(expected,TestHelper.removeCR(sb).toString());
    }

}
