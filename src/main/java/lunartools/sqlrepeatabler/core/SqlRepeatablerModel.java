package lunartools.sqlrepeatabler.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.ChangeListenerSupport;
import lunartools.SwingTools;
import lunartools.sqlrepeatabler.common.model.SimpleEvents;
import lunartools.sqlrepeatabler.core.model.SqlBlock;
import lunartools.sqlrepeatabler.core.model.SqlScript;
import lunartools.sqlrepeatabler.infrastructure.config.ProcessingOrder;
import lunartools.sqlrepeatabler.infrastructure.config.Settings;

public class SqlRepeatablerModel implements ChangeListenerSupport{
	private static Logger logger = LoggerFactory.getLogger(SqlRepeatablerModel.class);
	public static final String PROGRAMNAME = "Sql-Repeatabler";
	private static String versionProgram=SwingTools.determineProgramVersion();
	private final List<ChangeListener> listeners = new CopyOnWriteArrayList<>();

	private ArrayList<File> sqlInputFilesOrderAsAdded=new ArrayList<>();
	private ArrayList<File> sqlInputFiles=new ArrayList<>();
	private ArrayList<SqlScript> sqlScripts=new ArrayList<>();
	private ArrayList<SqlBlock> sqlConvertedScriptBlocks=new ArrayList<>();

	public static String getProgramVersion() {
		return versionProgram;
	}

	@Override
	public List<ChangeListener> getListeners() {
		return listeners;
	}

	public ArrayList<File> getSqlInputFiles() {
		return sqlInputFiles;
	}

	public void addSqlInputFile(File file) {
		if(!this.sqlInputFilesOrderAsAdded.contains(file)) {
			this.sqlInputFilesOrderAsAdded.add(file);
			this.sqlInputFiles.add(file);
			sortInputFiles();
			notifyListeners(SimpleEvents.MODEL_SQLINPUTFILESCHANGED);
		}
	}

	public void addSqlInputFiles(ArrayList<File> files) {
		boolean changed=false;
		for(int i=0;i<files.size();i++) {
			File file=files.get(i);
			if(!this.sqlInputFilesOrderAsAdded.contains(file)) {
				this.sqlInputFilesOrderAsAdded.add(file);
				this.sqlInputFiles.add(file);
				changed=true;
			}
		}
		if(changed) {
			sortInputFiles();
			notifyListeners(SimpleEvents.MODEL_SQLINPUTFILESCHANGED);
		}
	}

	private Comparator<File> getSortFilesByCreationDateComparator() {
		Comparator<File> comparator=new Comparator<File>() {

			@Override
			public int compare(File file1, File file2) {
				Path p1 = file1.toPath();
				Path p2 = file2.toPath();
				try {
					FileTime t1 = Files.readAttributes(p1, BasicFileAttributes.class).creationTime();
					FileTime t2 = Files.readAttributes(p2, BasicFileAttributes.class).creationTime();
					return t1.compareTo(t2);			
				} catch (IOException e) {
					throw new RuntimeException("Exception while sorting SQL files",e);
				}
			}
		};
		return comparator;
	}

	public void clearConvertedSqlScriptBlocks() {
		sqlConvertedScriptBlocks=new ArrayList<>();
		notifyListeners(SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED);
	}

	public boolean hasSqlInputFiles() {
		return sqlInputFilesOrderAsAdded.size()>0;
	}

	public boolean hasSqlConvertedScripts() {
		return sqlConvertedScriptBlocks.size()>0;
	}

	public SqlBlock getSingleConvertedSqlScriptBlock(int index) {
		if(index>=sqlConvertedScriptBlocks.size()) {
			return new SqlBlock();
		}
		return sqlConvertedScriptBlocks.get(index);
	}

	public SqlScript getSqlScript(int index) {
		if(index>=sqlScripts.size()) {
			return null;
		}
		return sqlScripts.get(index);
	}

	public void setSqlScripts(ArrayList<SqlScript> sqlScripts) {
		this.sqlScripts=sqlScripts;
		notifyListeners(SimpleEvents.MODEL_INPUTSQLSCRIPTCHANGED);
	}

	public void setConvertedSqlScriptBlocks(ArrayList<SqlBlock> sqlBlocks) {
		this.sqlConvertedScriptBlocks=sqlBlocks;
		notifyListeners(SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED);
	}

	public StringBuffer getConvertedSqlScriptCharactersAsStringBuffer() {
		StringBuffer sbConvertedScripts=new StringBuffer();
		sbConvertedScripts.append("-- "+SqlRepeatablerModel.PROGRAMNAME+" "+SqlRepeatablerModel.getProgramVersion()+System.lineSeparator());
		sbConvertedScripts.append(String.format("-- created: %s, %s\n",
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
				System.getProperty("user.name")));
		for(int i=0;i<sqlConvertedScriptBlocks.size();i++) {
			SqlBlock sqlBlock=sqlConvertedScriptBlocks.get(i);
			sbConvertedScripts.append(System.lineSeparator());
			sbConvertedScripts.append("-- Original: "+sqlInputFiles.get(i).getName());
			sbConvertedScripts.append(System.lineSeparator());
			sbConvertedScripts.append(sqlBlock.toString());
		}
		return sbConvertedScripts;
	}

	public void reload() {
		for(int i=sqlInputFilesOrderAsAdded.size()-1;i>=0;i--) {
			File file=sqlInputFilesOrderAsAdded.get(i);
			if(!file.exists()) {
				logger.warn("Removed file from list, because it was deleted or renamed: "+file.getAbsolutePath());
				sqlInputFilesOrderAsAdded.remove(i);
				sqlInputFiles.remove(file);
			}
		}
		notifyListeners(SimpleEvents.MODEL_SQLINPUTFILESCHANGED);
	}

	public void reset() {
		sqlInputFilesOrderAsAdded=new ArrayList<>();
		sqlInputFiles=new ArrayList<>();
		sqlConvertedScriptBlocks=new ArrayList<>();
		notifyListeners(SimpleEvents.MODEL_RESET);
		notifyListeners(SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED);
	}

	public void clearInputPanel() {
		notifyListeners(SimpleEvents.MODEL_CLEARINPUTPANEL);
	}

	public void setProcessingOrder(ProcessingOrder processingOrder) {
		Settings settings=Settings.getInstance();
		if(settings.getProcessingOrder()!=processingOrder) {
			logger.info(String.format("Processing order changed: %s", processingOrder.getLabel()));
			settings.setProcessingOrder(processingOrder);
			sortInputFiles();
			notifyListeners(SimpleEvents.MODEL_SQLINPUTFILESCHANGED);
		}
	}

	public void enableBackgroundColor(boolean isBackgroundColorEnabled) {
		Settings settings=Settings.getInstance();
		if(settings.isBackgroundColorEnabled()!=isBackgroundColorEnabled) {
			logger.info(String.format("Background color %s",isBackgroundColorEnabled?"enabled":"disabled"));
			Settings.getInstance().enableBackgroundColor(isBackgroundColorEnabled);
			notifyListeners(SimpleEvents.MODEL_SETTING_BACKGROUNDCOLOR_CHANGED);
		}
	}

	private void sortInputFiles() {
		switch(Settings.getInstance().getProcessingOrder()) {
		case ASADDED:
			sqlInputFiles.clear();
			sqlInputFiles.addAll(sqlInputFilesOrderAsAdded);
			break;
		case CREATIONDATE:
			sqlInputFiles.sort(getSortFilesByCreationDateComparator());
			break;
		case ALPHABETICALLY:
			sqlInputFiles.sort(Comparator.comparing(File::getName));
			break;
		}
	}

	public void closeScript(int index) {
		if(index>=0 && index<sqlInputFilesOrderAsAdded.size()) {
			sqlInputFilesOrderAsAdded.remove(index);
			if(sqlInputFilesOrderAsAdded.size()==0) {
				reset();
				return;
			}
			sqlInputFiles.remove(index);
			notifyListeners(SimpleEvents.MODEL_SQLINPUTFILESCHANGED);
		}
	}

	public File getSqlInputFile(int index) {
		if(index<0 && index>=sqlInputFiles.size()) {
			throw new IllegalArgumentException(String.format("Illegal index: %d. Valid index: 0 to %d", index,sqlInputFiles.size()-1));
		}
		return sqlInputFiles.get(index);
	}

}
