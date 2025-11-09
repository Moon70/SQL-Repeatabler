package lunartools.sqlrepeatabler.gui.actions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.SwingTools;
import lunartools.sqlrepeatabler.SqlRepeatablerController;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.SqlRepeatablerSettings;
import lunartools.sqlrepeatabler.TextFileFilter;

class SaveAsAction extends AbstractAction {
	private static Logger logger = LoggerFactory.getLogger(SaveAsAction.class);
	private SqlRepeatablerController controller;

	public SaveAsAction(SqlRepeatablerController controller) {
		this.controller = controller;
		this.putValue(NAME, "Save As");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
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

//		String filepath=SqlRepeatablerSettings.getSettings().getString(SqlRepeatablerSettings.FILE_SAVEFOLDER);		
//		if(filepath!=null && filepath.length()>0) {
//			file=new File(filepath);
//			fileChooser.setCurrentDirectory(file.getAbsoluteFile());
//		}
		ArrayList<File> inputFiles=controller.getModel().getSqlInputFiles();
		if(inputFiles.size()>0) {
			file=inputFiles.get(0).getParentFile();
			fileChooser.setCurrentDirectory(file.getAbsoluteFile());
		}
		
		fileChooser.setDialogTitle("Select file to save");
		fileChooser.setPreferredSize(new Dimension(800,(int)(800/SwingTools.SECTIOAUREA)));
		if(fileChooser.showSaveDialog(controller.getView())==JFileChooser.APPROVE_OPTION) {
			file=fileChooser.getSelectedFile();
			String filename=file.getName();
			if(!filename.toLowerCase().endsWith(TextFileFilter.FILEEXTENSION)) {
				file=new File(file.getParentFile(),filename+TextFileFilter.FILEEXTENSION);
			}
			if(file.exists() && userCanceledFileExistsDialogue(file)) {
				return;
			}
			SqlRepeatablerSettings.getInstance().setString(SqlRepeatablerSettings.FILE_SAVEFOLDER, file.getParent());
			action_SaveProjectFile(file);
		}
	}

	private void action_SaveProjectFile(File file) {
		try {
			try(FileOutputStream fileOutputStream=new FileOutputStream(file)){
				fileOutputStream.write(controller.getModel().getConvertedSqlScriptCharactersAsStringBuffer().toString().getBytes(StandardCharsets.UTF_8));
			}
		} catch (Exception e) {
			logger.error("Error saving SQL file",e);
		}
	}

	private boolean userCanceledFileExistsDialogue(File choosenProjectFile) {
		return JOptionPane.showConfirmDialog(
				controller.getView(),
				"File already exists, OK to overwrite?\n"+choosenProjectFile.getAbsolutePath(),
				SqlRepeatablerModel.PROGRAMNAME,
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE,
				null
				)!=JOptionPane.OK_OPTION;
	}

}
