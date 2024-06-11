package lunartools.sqlrepeatabler.commands;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lunartools.sqlrepeatabler.services.StringWriterLn;

class CommandEmptyLineTest {
    private StringWriterLn writer;
    private Command command;

    @BeforeEach
    void init() {
        writer=new StringWriterLn();
        command=new CommandEmptyLine();
    }
    
    @Test
    void emptyLineIsAccepted() throws Exception{
        BufferedReader reader=null;
        String line="";
        assertTrue(command.acceptLine(line, reader, writer));
        assertEquals(line+System.lineSeparator(), writer.toString());
    }

    @Test
    void whitespaceLineIsAccepted() throws Exception {
        BufferedReader reader=null;
        String line="    \t\r\n";
        assertTrue(command.acceptLine(line, reader, writer));
        assertEquals(""+System.lineSeparator(), writer.toString());
    }

    @Test
    void nonemptyLineIsNotAccepted() throws Exception {
        BufferedReader reader=null;
        String line="ALTER SCHWEDE";
        assertFalse(command.acceptLine(line, reader, writer));
        assertEquals("", writer.toString());
    }

}
