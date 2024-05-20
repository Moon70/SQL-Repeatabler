package lunartools.sqlrepeatabler;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class TextFileFilter extends FileFilter{
	public static final String FILEEXTENSION=".sql".toLowerCase();

	@Override
	public boolean accept(File file) {
		return file.isDirectory() || file.getName().toLowerCase().endsWith(FILEEXTENSION);
	}

	@Override
	public String getDescription() {
		return "SQL script file";
	}

}