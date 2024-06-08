package lunartools.sqlrepeatabler.gui;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import lunartools.sqlrepeatabler.SqlRepeatablerModel;

public class MainPanel extends JPanel {
	private IOPanel ioPanel;
	private LogPanel logPanel;

	public MainPanel(SqlRepeatablerModel model) {
		JSplitPane jSplitPaneVertical=new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		ioPanel=new IOPanel(model);
		jSplitPaneVertical.setTopComponent(ioPanel);

		logPanel=new LogPanel(model);
		jSplitPaneVertical.setBottomComponent(logPanel);

		add(jSplitPaneVertical);
	}

}
