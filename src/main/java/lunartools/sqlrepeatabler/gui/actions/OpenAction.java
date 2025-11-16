package lunartools.sqlrepeatabler.gui.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import lunartools.AbstractSettings;
import lunartools.sqlrepeatabler.SqlRepeatablerController;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.TextFileFilter;
import lunartools.sqlrepeatabler.settings.Settings;

class OpenAction extends AbstractAction {
	private SqlRepeatablerController controller;

	public OpenAction(SqlRepeatablerController controller) {
		this.controller = controller;
		this.putValue(NAME, "Open");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SqlRepeatablerModel model=controller.getModel();

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
		AbstractSettings settings=Settings.getInstance();
		String loadFolder=settings.getString(Settings.FILE_LOADFOLDER);
		if(loadFolder!=null) {
			file=new File(loadFolder);
			if(file.exists()) {
				fileChooser.setCurrentDirectory(file);
			}
		}
		if(fileChooser.showOpenDialog(controller.getView())==JFileChooser.APPROVE_OPTION) {
			file=fileChooser.getSelectedFile();
			settings.setString(Settings.FILE_LOADFOLDER, file.getParent());
			model.addSqlInputFile(file);
		}
	}

}
