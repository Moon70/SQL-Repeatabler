package lunartools.sqlrepeatabler.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lunartools.FileTools;
import lunartools.sqlrepeatabler.services.StringWriterLn;

class CommandAlterTableTest {
    private StringWriterLn writer;
    private Command command;
    
    @BeforeEach
    void init() {
        writer=new StringWriterLn();
        command=new CommandAlterTable();
    }

    @Test
    void alterTableIsAccepted() throws Exception {
        InputStream inputStream=this.getClass().getResourceAsStream("/AlterTable/AlterTable_AddFieldModifyColumn_Testdata.txt");
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        String line=reader.readLine();
        assertTrue(command.acceptLine(line, reader, writer));
        
        InputStream inputStreamExpected=this.getClass().getResourceAsStream("/AlterTable/AlterTable_AddFieldModifyColumn_Expected.txt");
        assertEquals(FileTools.getStringBufferFromInputStream(inputStreamExpected,"UTF-8").toString(),writer.toString());
    }

    @Test
    void alterTableNoSquareBracketsIsAccepted() throws Exception {
        InputStream inputStream=this.getClass().getResourceAsStream("/AlterTable/AlterTable_NoSquareBrackets_Testdata.txt");
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        String line=reader.readLine();
        assertTrue(command.acceptLine(line, reader, writer));
        
        InputStream inputStreamExpected=this.getClass().getResourceAsStream("/AlterTable/AlterTable_NoSquareBrackets_Expected.txt");
        assertEquals(FileTools.getStringBufferFromInputStream(inputStreamExpected,"UTF-8").toString(),writer.toString());
    }

    @Test
    void alterTableAndAddConstraintInOneLineIsAccepted() throws Exception {
        InputStream inputStream=this.getClass().getResourceAsStream("/AlterTable/AlterTable_AddConstraintOneLine_Testdata.txt");
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        String line=reader.readLine();
        assertTrue(command.acceptLine(line, reader, writer));
        
        InputStream inputStreamExpected=this.getClass().getResourceAsStream("/AlterTable/AlterTable_AddConstraintOneLine_Expected.txt");
        assertEquals(FileTools.getStringBufferFromInputStream(inputStreamExpected,"UTF-8").toString(),writer.toString());
    }

    @Test
    void alterTableAndAddFieldInOneLineIsAccepted() throws Exception {
        InputStream inputStream=this.getClass().getResourceAsStream("/AlterTable/AlterTable_AddFieldOneLine_Testdata.txt");
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        String line=reader.readLine();
        assertTrue(command.acceptLine(line, reader, writer));
        
        InputStream inputStreamExpected=this.getClass().getResourceAsStream("/AlterTable/AlterTable_AddFieldOneLine_Expected.txt");
        assertEquals(FileTools.getStringBufferFromInputStream(inputStreamExpected,"UTF-8").toString(),writer.toString());
    }
    
}
