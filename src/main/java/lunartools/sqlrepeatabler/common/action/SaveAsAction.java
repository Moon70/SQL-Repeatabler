package lunartools.sqlrepeatabler.common.action;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import lunartools.SwingTools;
import lunartools.sqlrepeatabler.common.ui.Dialogs;
import lunartools.sqlrepeatabler.common.ui.SqlFileFilter;
import lunartools.sqlrepeatabler.controller.FileController;
import lunartools.sqlrepeatabler.infrastructure.config.Settings;
import lunartools.sqlrepeatabler.main.SqlRepeatablerModel;

class SaveAsAction extends AbstractAction {
	private SqlRepeatablerModel model;
	private final FileController fileController;
	private final JFrame parentFrame;

	public SaveAsAction(SqlRepeatablerModel model, FileController fileController, JFrame parentFrame) {
		super("Save As");
		this.model = model;
		this.fileController=fileController;
		this.parentFrame=parentFrame;

		putValue(SHORT_DESCRIPTION, "Save converted scripts");
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
		fileChooser.addChoosableFileFilter(new SqlFileFilter());
		File file=null;

		ArrayList<File> inputFiles=model.getSqlInputFiles();
		if(inputFiles.size()>0) {
			file=inputFiles.get(0).getParentFile();
			fileChooser.setCurrentDirectory(file.getAbsoluteFile());
		}

		fileChooser.setDialogTitle("Select file to save");
		fileChooser.setPreferredSize(new Dimension(800,(int)(800/SwingTools.SECTIOAUREA)));
		if(fileChooser.showSaveDialog(parentFrame)==JFileChooser.APPROVE_OPTION) {
			file=fileChooser.getSelectedFile();
			String filename=file.getName();
			if(!filename.toLowerCase().endsWith(SqlFileFilter.FILEEXTENSION)) {
				file=new File(file.getParentFile(),filename+SqlFileFilter.FILEEXTENSION);
			}
			if(file.exists() && Dialogs.userCanceledFileExistsDialogue(String.format("File already exists, OK to overwrite?\n%s", file.getAbsolutePath()),parentFrame)) {
				return;
			}
			Settings.getInstance().setString(Settings.FILE_SAVEFOLDER, file.getParent());
			fileController.saveFile(file);
		}
	}

}
