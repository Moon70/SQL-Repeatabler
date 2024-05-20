package lunartools.sqlrepeatabler.services;

import java.io.StringWriter;

public class StringWriterLn extends StringWriter{

	public void writeln(String str) {
		super.write(str);
		super.write(System.lineSeparator());
	}
	
}
