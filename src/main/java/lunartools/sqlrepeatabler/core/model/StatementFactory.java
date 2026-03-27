package lunartools.sqlrepeatabler.core.model;

import java.io.EOFException;

import lunartools.sqlrepeatabler.core.processing.StatementTokenizer;

public abstract class StatementFactory {

	public abstract Statement createStatement(StatementTokenizer statementTokenizer) throws SqlParserException,EOFException;

}
