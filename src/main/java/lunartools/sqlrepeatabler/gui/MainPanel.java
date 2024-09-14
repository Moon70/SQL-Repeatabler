package lunartools.sqlrepeatabler.gui;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import lunartools.sqlrepeatabler.SqlRepeatablerModel;

public class MainPanel extends JPanel {
	private IOPanel ioPanel;

	public MainPanel(SqlRepeatablerModel model,JTextArea logTextArea) {
		JSplitPane jSplitPaneVertical=new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		ioPanel=new IOPanel(model);
		jSplitPaneVertical.setTopComponent(ioPanel);

		LogPanelTextarea logPanel=new LogPanelTextarea(model,logTextArea);
		jSplitPaneVertical.setBottomComponent(logPanel);

//		LogPanelEditorpane logPanel=new LogPanelEditorpane(model);
//		jSplitPaneVertical.setBottomComponent(logPanel);

		add(jSplitPaneVertical);
	}

}
