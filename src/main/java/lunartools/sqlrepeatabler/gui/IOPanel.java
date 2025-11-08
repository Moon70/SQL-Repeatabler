package lunartools.sqlrepeatabler.gui;

import java.awt.BorderLayout;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;

import lunartools.ImageTools;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;

public class IOPanel extends JPanel{
	private SqlScriptEditorPane inputPane;
	private SqlScriptEditorPane outputPane;
	private final int sqlFileIndex;

	public IOPanel(SqlRepeatablerModel model, int sqlFileIndex) {
		this.sqlFileIndex=sqlFileIndex;

		setLayout(new BorderLayout());

		inputPane=new SqlScriptEditorPane(model,sqlFileIndex,false);
		outputPane=new SqlScriptEditorPane(model,sqlFileIndex,true);

		JSplitPane jSplitPaneHorizontal = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
		jSplitPaneHorizontal.setResizeWeight(0.5);
		jSplitPaneHorizontal.setOneTouchExpandable(true);

		JScrollPane scrollPaneLeft=new JScrollPane(inputPane);
		scrollPaneLeft.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneLeft.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jSplitPaneHorizontal.setLeftComponent(scrollPaneLeft);

		JScrollPane scrollPaneRight=new JScrollPane(outputPane);
		scrollPaneRight.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneRight.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jSplitPaneHorizontal.setRightComponent(scrollPaneRight);
		add(jSplitPaneHorizontal,BorderLayout.CENTER);

	}

	public void installPopup(Action copyAll) {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem jMenuItem=new JMenuItem(copyAll);
		jMenuItem.setIcon(ImageTools.createImageIcon("/icons/content_copy_16.png"));
		popupMenu.add(jMenuItem);

		outputPane.setComponentPopupMenu(popupMenu);
	}

	public int getSqlFileIndex() {
		return sqlFileIndex;
	}

	public SqlScriptEditorPane getInputPane() {
		return inputPane;
	}

	public SqlScriptEditorPane getOutputPane() {
		return outputPane;
	}
}
