package lunartools.sqlrepeatabler.gui;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.ImageTools;
import lunartools.sqlrepeatabler.SimpleEvents;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.gui.actions.CloseScriptAction;
import lunartools.sqlrepeatabler.gui.actions.CopyToClipboardAction;
import lunartools.sqlrepeatabler.parser.HtmlRenderer;
import lunartools.sqlrepeatabler.parser.SqlScript;

public class IOPanelController {
	private static Logger logger = LoggerFactory.getLogger(IOPanelController.class);
	private final SqlRepeatablerModel model;
	private IOPanel ioPanel;

	public IOPanelController(SqlRepeatablerModel model,IOPanel ioPanel) {
		this.model=model;
		this.ioPanel=ioPanel;

		JPopupMenu popupMenuOutputPane = new JPopupMenu();
		popupMenuOutputPane.add(createCloseScriptJMenuItem(ioPanel));
		popupMenuOutputPane.add(createCopyToClipboardJMenuItem(ioPanel));
		ioPanel.getOutputPane().setComponentPopupMenu(popupMenuOutputPane);

		JPopupMenu popupMenuInputPane = new JPopupMenu();
		popupMenuInputPane.add(createCloseScriptJMenuItem(ioPanel));
		ioPanel.getInputPane().setComponentPopupMenu(popupMenuInputPane);


		model.addChangeListener(this::updateModelChanges);
	}

	private JMenuItem createCloseScriptJMenuItem(IOPanel ioPanel) {
		JMenuItem jMenuItemCloseScript=new JMenuItem(new CloseScriptAction(model, ioPanel));
		jMenuItemCloseScript.setIcon(ImageTools.createImageIcon("/icons/tab_close_16.png"));
		return jMenuItemCloseScript;
	}

	private JMenuItem createCopyToClipboardJMenuItem(IOPanel ioPanel) {
		JMenuItem jMenuItemCopyToClipboard=new JMenuItem(new CopyToClipboardAction(model,ioPanel));
		jMenuItemCopyToClipboard.setIcon(ImageTools.createImageIcon("/icons/content_copy_16.png"));
		return jMenuItemCopyToClipboard;
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
