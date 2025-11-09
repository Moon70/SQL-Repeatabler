package lunartools.sqlrepeatabler.gui;

import javax.swing.Action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.SimpleEvents;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.gui.actions.CopyToClipboardAction;
import lunartools.sqlrepeatabler.parser.HtmlRenderer;
import lunartools.sqlrepeatabler.parser.SqlScript;

public class IOPanelController {
	private static Logger logger = LoggerFactory.getLogger(IOPanelController.class);
	private final SqlRepeatablerModel model;
	private IOPanel ioPanel;

	public final Action copyToClipboardAction;

	public IOPanelController(SqlRepeatablerModel model,IOPanel ioPanel) {
		this.model=model;
		this.ioPanel=ioPanel;

		copyToClipboardAction=new CopyToClipboardAction(model,ioPanel);

		ioPanel.installPopup(copyToClipboardAction);

		model.addChangeListener(this::updateModelChanges);

	}

	public void updateModelChanges(Object object) {
		if(logger.isTraceEnabled()) {
			logger.trace("update: "+object);
		}
		if(object==SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED) {
			if(model.hasSqlConvertedScripts()) {
				int sqlFileIndex=ioPanel.getSqlFileIndex();
				SqlScript sqlScript=model.getSqlScript(sqlFileIndex);
				if(sqlScript!=null) {
					HtmlRenderer htmlRenderer=new HtmlRenderer();
					ioPanel.getInputPane().setText(htmlRenderer.render(sqlScript.getSqlStrings()));
					htmlRenderer=new HtmlRenderer();
					ioPanel.getOutputPane().setText(htmlRenderer.render(model.getSingleConvertedSqlScriptBlock(sqlFileIndex)));
					ioPanel.repaint();
				}
			}
		}
	}
}
