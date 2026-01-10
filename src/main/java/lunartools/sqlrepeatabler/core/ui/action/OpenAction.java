package lunartools.sqlrepeatabler.core.ui.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import lunartools.AbstractSettings;
import lunartools.sqlrepeatabler.common.util.SqlFileFilter;
import lunartools.sqlrepeatabler.core.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.infrastructure.config.Settings;

class OpenAction extends AbstractAction {
	private SqlRepeatablerModel model;
	private final JFrame parentFrame;

	public OpenAction(SqlRepeatablerModel model, JFrame parentFrame) {
		super("Open");
		this.model = model;
		this.parentFrame=parentFrame;

		putValue(SHORT_DESCRIPTION, "Open a SQL script");
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
		AbstractSettings settings=Settings.getInstance();
		String loadFolder=settings.getString(Settings.FILE_LOADFOLDER);
		if(loadFolder!=null) {
			file=new File(loadFolder);
			if(file.exists()) {
				fileChooser.setCurrentDirectory(file);
			}
		}
		if(fileChooser.showOpenDialog(parentFrame)==JFileChooser.APPROVE_OPTION) {
			file=fileChooser.getSelectedFile();
			settings.setString(Settings.FILE_LOADFOLDER, file.getParent());
			model.addSqlInputFile(file);
		}
	}

}
