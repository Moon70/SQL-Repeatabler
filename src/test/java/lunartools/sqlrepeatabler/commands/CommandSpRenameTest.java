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

class CommandSpRenameTest {
    private StringWriterLn writer;
    private Command command;
    
    @BeforeEach
    void init() {
        writer=new StringWriterLn();
        command=new CommandSpRename();
    }

    @Test
    void spRenameForColumnIsAccepted() throws Exception {
        InputStream inputStream=this.getClass().getResourceAsStream("/SpRename/RenameColumn.txt");
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        String line=reader.readLine();
        assertTrue(command.acceptLine(line, reader, writer));
        
        InputStream inputStreamExpected=this.getClass().getResourceAsStream("/SpRename/RenameColumn_Expected.txt");
        assertEquals(FileTools.readInputStreamToStringBuffer(inputStreamExpected,"UTF-8").toString(),writer.toString());
    }

}
