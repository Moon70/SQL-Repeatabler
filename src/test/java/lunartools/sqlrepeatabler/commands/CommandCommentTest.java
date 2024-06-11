package lunartools.sqlrepeatabler.commands;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.services.StringWriterLn;

class CommandCommentTest {
    private StringWriterLn writer;
    private Command command;
    
    @BeforeEach
    void init() {
        writer=new StringWriterLn();
        command=new CommandComment();
    }

    @Test
    void commentLineIsAccepted() throws Exception {
        BufferedReader reader=null;
        String line="-- foo";
        assertTrue(command.acceptLine(line, reader, writer));
        assertEquals(line+System.lineSeparator(), writer.toString());
    }

    @Test
    void noncommentLineIsNotAccepted() throws Exception {
        BufferedReader reader=null;
        String line="ALTER SCHWEDE";
        assertFalse(command.acceptLine(line, reader, writer));
        assertEquals("", writer.toString());
    }

}
