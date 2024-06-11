package lunartools.sqlrepeatabler;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.filter.ThresholdFilter;
import lunartools.Settings;
import lunartools.sqlrepeatabler.gui.SqlRepeatablerView;
import lunartools.sqlrepeatabler.worker.ConvertSqlFileWorker;

public class SqlRepeatablerController implements Observer{
	private static Logger logger = LoggerFactory.getLogger(SqlRepeatablerController.class);
	private SqlRepeatablerModel model;
	private SqlRepeatablerView view;

	public SqlRepeatablerController() {
		Settings settings=SqlRepeatablerSettings.getSettings();
		model=new SqlRepeatablerModel();
		configureLogger();
		model.addObserver(this);
		Rectangle frameBounds=fixScreenBounds(settings.getRectangle(SqlRepeatablerSettings.VIEW_BOUNDS, SqlRepeatablerModel.getDefaultFrameBounds()),SqlRepeatablerModel.getDefaultFrameSize());
		model.setFrameBounds(frameBounds);
		view=new SqlRepeatablerView(model,this);
		view.addObserver(this);
	}

	private void configureLogger() {
		ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		TextareaAppender textareaAppender = new TextareaAppender(model);
        ThresholdFilter filter = new ThresholdFilter();
        filter.setLevel(Level.INFO.levelStr);
        filter.setContext(rootLogger.getLoggerContext());
        filter.start();
		textareaAppender.addFilter(filter);
		rootLogger.addAppender(textareaAppender);
		textareaAppender.start();
	}

	private Rectangle fixScreenBounds(Rectangle screenBounds, Dimension defaultFrameSize) {
		int centerX=screenBounds.x+screenBounds.width>>1;
		int centerY=screenBounds.y+screenBounds.height>>1;
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();  
		GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();
		int numberOfGraphicsDevices=graphicsDevices.length;
		for(int i=0;i<numberOfGraphicsDevices;i++){
			GraphicsDevice graphicsDevice=graphicsDevices[i];
			Rectangle graphicsDeviceBounds = graphicsDevice.getDefaultConfiguration().getBounds();
			if(
					centerX>=graphicsDeviceBounds.x &&
					centerY>=graphicsDeviceBounds.y &&
					centerX<=graphicsDeviceBounds.x+graphicsDeviceBounds.width &&
					centerY<=graphicsDeviceBounds.y+graphicsDeviceBounds.height
					) {
				return screenBounds;
			}
		}
		GraphicsDevice graphicsDevice=graphicsDevices[0];
		Rectangle graphicsDeviceBounds = graphicsDevice.getDefaultConfiguration().getBounds();
		int marginX=(graphicsDeviceBounds.width-defaultFrameSize.width)>>1;
					int marginY=(graphicsDeviceBounds.height-defaultFrameSize.height)>>1;
					return new Rectangle(graphicsDeviceBounds.x+marginX,graphicsDeviceBounds.y+marginY,defaultFrameSize.width,defaultFrameSize.height);
	}

	public void openGUI() {
		view.setVisible(true);
		logger.info(SqlRepeatablerModel.PROGRAMNAME+" "+SqlRepeatablerModel.determineProgramVersion());
//		logger.debug("test debug message");
//		logger.info("test info message");
//		logger.warn("test warn message");
//		logger.error("test error message");
//		for(int i=0;i<50;i++) {
//	        logger.info("log test "+i);
//		}
	}

	@Override
	public void update(Observable observable, Object object) {
		if(logger.isTraceEnabled()) {
			logger.trace("update: "+observable+", "+object);
		}
		if(object==SimpleEvents.EXIT) {
			exit();
		}else if(object==SimpleEvents.MODEL_SQLINPUTFILESCHANGED) {
			ConvertSqlFileWorker worker=new ConvertSqlFileWorker(model);
			worker.execute();
		}
	}

	private void exit() {
		Settings settings=SqlRepeatablerSettings.getSettings();
		settings.setRectangle(SqlRepeatablerSettings.VIEW_BOUNDS, view.getBounds());
		try {
			settings.saveSettings();
		} catch (IOException e) {
			logger.error("error while saving settings",e);
		}
		view.setVisible(false);
		view.dispose();
	}

	public void action_FileOpen() {
		final JFileChooser fileChooser= new JFileChooser() {
			public void updateUI() {
				putClientProperty("FileChooser.useShellFolder", Boolean.FALSE);
				super.updateUI();
			}
		};
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new TextFileFilter());
		File file=null;
		String loadFolder=SqlRepeatablerSettings.getSettings().getString(SqlRepeatablerSettings.FILE_LOADFOLDER);
		if(loadFolder!=null) {
			file=new File(loadFolder);
			if(file.exists()) {
				fileChooser.setCurrentDirectory(file);
			}
		}
		if(fileChooser.showOpenDialog(view)==JFileChooser.APPROVE_OPTION) {
			file=fileChooser.getSelectedFile();
			SqlRepeatablerSettings.getSettings().setString(SqlRepeatablerSettings.FILE_LOADFOLDER, file.getParent());
			model.addSqlInputFile(file);
		}
	}

	public void action_FileSaveAs() {
		final JFileChooser fileChooser= new JFileChooser() {
			public void updateUI() {
				putClientProperty("FileChooser.useShellFolder", Boolean.FALSE);
				super.updateUI();
			}
		};
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new TextFileFilter());
		//		File file=model.getFile();
		File file=null;
		if(file!=null) {
			fileChooser.setCurrentDirectory(file.getParentFile());
			fileChooser.setSelectedFile(file);
		}else {
			String filepath=SqlRepeatablerSettings.getSettings().getString(SqlRepeatablerSettings.FILE_SAVEFOLDER);
			if(filepath!=null && filepath.length()>0) {
				file=new File(filepath);
				fileChooser.setCurrentDirectory(file.getAbsoluteFile());
			}
		}
		fileChooser.setDialogTitle("Select file to save");
		fileChooser.setPreferredSize(new Dimension(800,(int)(800/SqlRepeatablerModel.SECTIOAUREA)));
		if(fileChooser.showSaveDialog(view)==JFileChooser.APPROVE_OPTION) {
			file=fileChooser.getSelectedFile();
			String filename=file.getName();
			if(!filename.toLowerCase().endsWith(TextFileFilter.FILEEXTENSION)) {
				file=new File(file.getParentFile(),filename+TextFileFilter.FILEEXTENSION);
			}
			if(file.exists() && userCanceledFileExistsDialogue(file)) {
				return;
			}
			//			model.setFile(file);
			SqlRepeatablerSettings.getSettings().setString(SqlRepeatablerSettings.FILE_SAVEFOLDER, file.getParent());
			action_SaveProjectFile(file);
		}
	}

	private void action_SaveProjectFile(File file) {
		try {
			try(FileOutputStream fileOutputStream=new FileOutputStream(file)){
				fileOutputStream.write(model.getConvertedSqlScript().toString().getBytes(StandardCharsets.UTF_8));
			}
		} catch (Exception e) {
			logger.error("Error saving SQL file",e);
		}
	}

	public void action_Reset() {
		model.reset();
	}

	private boolean userCanceledFileExistsDialogue(File choosenProjectFile) {
		return JOptionPane.showConfirmDialog(
				view,
				"File already exists, OK to overwrite?\n"+choosenProjectFile.getAbsolutePath(),
				SqlRepeatablerModel.PROGRAMNAME,
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE,
				null
				)!=JOptionPane.OK_OPTION;
	}

}
