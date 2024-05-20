package lunartools.sqlrepeatabler.commands;

import java.io.BufferedReader;

import lunartools.sqlrepeatabler.services.StringWriterLn;

public abstract class Command {
	
	public abstract boolean acceptLine(String line,BufferedReader bufferesReader, StringWriterLn writer) throws Exception;
	
}