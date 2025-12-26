package lunartools.sqlrepeatabler.main;

import javax.swing.JPopupMenu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.model.SimpleEvents;
import lunartools.sqlrepeatabler.common.ui.ContextMenuView;
import lunartools.sqlrepeatabler.parser.HtmlRenderer;
import lunartools.sqlrepeatabler.parser.SqlScript;

public class IOPanelController {
	private static Logger logger = LoggerFactory.getLogger(IOPanelController.class);
	private final SqlRepeatablerModel model;
	private IOPanel ioPanel;

	public IOPanelController(SqlRepeatablerModel model,IOPanel ioPanel,ContextMenuView contextMenuView) {
		this.model=model;
		this.ioPanel=ioPanel;

		JPopupMenu popupMenuOutputPane = new JPopupMenu();
		popupMenuOutputPane.add(contextMenuView.createCloseScriptJMenuItem(ioPanel));
		popupMenuOutputPane.add(contextMenuView.createCopyToClipboardJMenuItem(ioPanel));
		ioPanel.getOutputPane().setComponentPopupMenu(popupMenuOutputPane);

		JPopupMenu popupMenuInputPane = new JPopupMenu();
		popupMenuInputPane.add(contextMenuView.createCloseScriptJMenuItem(ioPanel));
		ioPanel.getInputPane().setComponentPopupMenu(popupMenuInputPane);


		model.addChangeListener(this::updateModelChanges);
	}

	public void updateModelChanges(Object object) {
		if(logger.isTraceEnabled()) {
			logger.trace("update: "+object);
		}
		if(object==SimpleEvents.MODEL_INPUTSQLSCRIPTCHANGED) {
			int sqlFileIndex=ioPanel.getSqlFileIndex();
			SqlScript sqlScript=model.getSqlScript(sqlFileIndex);
			if(sqlScript!=null) {
				HtmlRenderer htmlRenderer=new HtmlRenderer();
				ioPanel.getInputPane().setText(htmlRenderer.render(sqlScript.getSqlStrings()));
				ioPanel.repaint();
			}
		}else if(object==SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED) {
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
		}else if(object==SimpleEvents.MODEL_SETTING_BACKGROUNDCOLOR_CHANGED) {
			int sqlFileIndex=ioPanel.getSqlFileIndex();
			SqlScript sqlScript=model.getSqlScript(sqlFileIndex);
			if(sqlScript!=null) {
				HtmlRenderer htmlRenderer=new HtmlRenderer();
				ioPanel.getInputPane().setText(htmlRenderer.render(sqlScript.getSqlStrings()));
				ioPanel.repaint();
				ioPanel.getOutputPane().setText(htmlRenderer.render(model.getSingleConvertedSqlScriptBlock(sqlFileIndex)));
				ioPanel.repaint();
			}
		}
	}

	public void setInputPaneText() {
		int sqlFileIndex=ioPanel.getSqlFileIndex();
		SqlScript sqlScript=model.getSqlScript(sqlFileIndex);
		if(sqlScript!=null) {
			HtmlRenderer htmlRenderer=new HtmlRenderer();
			ioPanel.getInputPane().setText(htmlRenderer.render(sqlScript.getSqlStrings()));
			ioPanel.repaint();
		}
	}
}
