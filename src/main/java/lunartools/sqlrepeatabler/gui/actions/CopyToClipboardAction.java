package lunartools.sqlrepeatabler.gui.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.gui.IOPanel;
import lunartools.sqlrepeatabler.parser.SqlBlock;

public class CopyToClipboardAction extends AbstractAction {
	private static Logger logger = LoggerFactory.getLogger(CopyToClipboardAction.class);
	private SqlRepeatablerModel model;
	private IOPanel ioPanel;

	public CopyToClipboardAction(SqlRepeatablerModel model, IOPanel ioPanel) {
		this.model = model;
		this.ioPanel = ioPanel;
		this.putValue(NAME, "Copy to clipboard");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int index=ioPanel.getSqlFileIndex();
		SqlBlock sqlBlock=model.getSingleConvertedSqlScriptBlock(index);
		StringSelection stringSelection = new StringSelection(sqlBlock.toString());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
		logger.info("copied script to clipboard");
	}

}
