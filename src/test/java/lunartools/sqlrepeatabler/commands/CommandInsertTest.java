package lunartools.sqlrepeatabler.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.TestHelper;
import lunartools.sqlrepeatabler.parser.SqlScript;
import lunartools.sqlrepeatabler.statements.InsertIntoStatementFactory;
import lunartools.sqlrepeatabler.statements.Statement;

class CommandInsertTest {
	private static final String TESTDATAFOLDER="/CommandInsertInto/";
    private InsertIntoStatementFactory factory=new InsertIntoStatementFactory();
    
    @Test
    void nonInsertIntoLineIsNotAccepted() throws Exception {
    	String filenameTestdata=	TESTDATAFOLDER+"OneNonInsertIntoLine_Testdata.txt";
		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
   		assertFalse(factory.match(sqlScript.peekLine()));
    }

    @Test
    void insert_OneColumn() throws Exception{
    	String filenameTestdata=	TESTDATAFOLDER+"InsertInto_OneRow_Testdata.txt";
    	String filenameExpecteddata=TESTDATAFOLDER+"InsertInto_OneRow_Expected.txt";
    	String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLine()));

		Statement sqlSegment=factory.createSqlSegment(sqlScript);
		StringBuilder sb=new StringBuilder();
		sqlSegment.toSql(sb);
		assertEquals(expected,TestHelper.removeCR(sb).toString());
    }

    @Test
    void insert_ThreeColumns() throws Exception{
    	String filenameTestdata=	TESTDATAFOLDER+"InsertInto_ThreeRows_Testdata.txt";
    	String filenameExpecteddata=TESTDATAFOLDER+"InsertInto_ThreeRows_Expected.txt";
    	String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLine()));

		Statement sqlSegment=factory.createSqlSegment(sqlScript);
		StringBuilder sb=new StringBuilder();
		sqlSegment.toSql(sb);
		assertEquals(expected,TestHelper.removeCR(sb).toString());
    }

    @Test
    void insert_OneColumn_ColumnWithFunction() throws Exception{
    	String filenameTestdata=	TESTDATAFOLDER+"InsertInto_OneRow_ColumnWithFunction_Testdata.txt";
    	String filenameExpecteddata=TESTDATAFOLDER+"InsertInto_OneRow_ColumnWithFunction_Expected.txt";
    	String expected=TestHelper.getCrStrippedResourceAsStringBuffer(filenameExpecteddata).toString();

		SqlScript sqlScript=SqlScript.createInstance(TestHelper.getResourceAsStringBuffer(filenameTestdata));
		assertTrue(factory.match(sqlScript.peekLine()));

		Statement sqlSegment=factory.createSqlSegment(sqlScript);
		StringBuilder sb=new StringBuilder();
		sqlSegment.toSql(sb);
		assertEquals(expected,TestHelper.removeCR(sb).toString());
    }
    
}
