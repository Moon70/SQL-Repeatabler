package lunartools.sqlrepeatabler.common.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import lunartools.sqlrepeatabler.gui.IOPanel;
import lunartools.sqlrepeatabler.main.SqlRepeatablerModel;

public class CloseScriptAction extends AbstractAction {
	private SqlRepeatablerModel model;
	private IOPanel ioPanel;

	public CloseScriptAction(SqlRepeatablerModel model, IOPanel ioPanel) {
		this.model = model;
		this.ioPanel = ioPanel;
		this.putValue(NAME, "Close");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int index=ioPanel.getSqlFileIndex();
		File file=model.getSqlInputFile(index);
		if(userCanceledCloseScriptDialogue(file)) {
			return;
		}
		model.closeScript(index);
	}

	private boolean userCanceledCloseScriptDialogue(File choosenProjectFile) {
		return JOptionPane.showConfirmDialog(
				ioPanel,
				"OK to close file?\n"+choosenProjectFile.getAbsolutePath(),
				SqlRepeatablerModel.PROGRAMNAME,
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE,
				null
				)!=JOptionPane.OK_OPTION;
	}

}
