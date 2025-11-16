package lunartools.sqlrepeatabler.gui;

import java.awt.BorderLayout;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import lunartools.ImageTools;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.settings.Settings;

public class IOPanel extends JPanel{
	private SqlEditorPane inputPane;
	private SqlEditorPane outputPane;
	private final int sqlFileIndex;
	private JSplitPane jSplitPaneHorizontal;
	private JScrollPane scrollPaneLeft;
	private JScrollPane scrollPaneRight;
	
	public IOPanel(SqlRepeatablerModel model, int sqlFileIndex) {
		this.sqlFileIndex=sqlFileIndex;

		setLayout(new BorderLayout());

		inputPane=new SqlEditorPane(model,sqlFileIndex,false);
		outputPane=new SqlEditorPane(model,sqlFileIndex,true);

		jSplitPaneHorizontal = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
		jSplitPaneHorizontal.setResizeWeight(0.5);
		jSplitPaneHorizontal.setOneTouchExpandable(true);
		jSplitPaneHorizontal.setDividerSize(8);

		scrollPaneLeft=new JScrollPane(inputPane);
		scrollPaneLeft.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneLeft.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jSplitPaneHorizontal.setLeftComponent(scrollPaneLeft);

		scrollPaneRight=new JScrollPane(outputPane);
		scrollPaneRight.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneRight.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jSplitPaneHorizontal.setRightComponent(scrollPaneRight);
		
		jSplitPaneHorizontal.setResizeWeight(1.0);

		add(jSplitPaneHorizontal,BorderLayout.CENTER);

		SwingUtilities.invokeLater(() -> {
			applyDividerLocation();
			jSplitPaneHorizontal.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, evt -> {
			    int newLocation=(int)evt.getNewValue();
			    Settings.getInstance().setDividerlocationScript(newLocation);
			});
		});
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

	public SqlEditorPane getInputPane() {
		return inputPane;
	}

	public SqlEditorPane getOutputPane() {
		return outputPane;
	}

	public void applyDividerLocation() {
		int min=scrollPaneLeft.getMinimumSize().width;
		int max=jSplitPaneHorizontal.getWidth()-scrollPaneRight.getMinimumSize().width;
		int location=Settings.getInstance().getDividerlocationScript();
		if(location!=0 && location>=min && location<=max) {
			jSplitPaneHorizontal.setDividerLocation(location);
		}
	}

}
