package lunartools.sqlrepeatabler.common.view;

import javax.swing.JMenuItem;

import lunartools.sqlrepeatabler.common.ui.IconProvider;
import lunartools.sqlrepeatabler.common.ui.Icons;
import lunartools.sqlrepeatabler.core.ui.action.ActionFactory;
import lunartools.sqlrepeatabler.core.view.IOPanel;

public class ContextMenuView {
	private final ActionFactory actionFactory;

	public ContextMenuView(ActionFactory actionFactory) {
		this.actionFactory=actionFactory;
	}

	public JMenuItem createCloseScriptJMenuItem(IOPanel ioPanel) {
		JMenuItem jMenuItemCloseScript=new JMenuItem(actionFactory.createCloseScriptAction(ioPanel.getSqlFileIndex(), ioPanel));
		jMenuItemCloseScript.setIcon(IconProvider.getFlatSvgIcon(Icons.CLOSE,jMenuItemCloseScript));
		return jMenuItemCloseScript;
	}

	public JMenuItem createCopyToClipboardJMenuItem(IOPanel ioPanel) {
		JMenuItem jMenuItemCopyToClipboard=new JMenuItem(actionFactory.createCopyToClipboardAction(ioPanel.getSqlFileIndex(), ioPanel));
		jMenuItemCopyToClipboard.setIcon(IconProvider.getFlatSvgIcon(Icons.COPY,jMenuItemCopyToClipboard));
		return jMenuItemCopyToClipboard;
	}

}
