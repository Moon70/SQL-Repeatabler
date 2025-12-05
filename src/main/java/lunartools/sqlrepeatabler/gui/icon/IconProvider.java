package lunartools.sqlrepeatabler.gui.icon;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

import com.formdev.flatlaf.extras.FlatSVGIcon;

public class IconProvider {

	public static FlatSVGIcon getFlatSvgIcon(Icons icon, JComponent component) {
		int size=getDefaultSize(component);
		FlatSVGIcon flatSVGIcon = new FlatSVGIcon(icon.getPath(), size, size,FlatSVGIcon.class.getClassLoader());
		return flatSVGIcon;
	}

	private static int getDefaultSize(JComponent component) {
		if(component instanceof JMenuItem) {
			return 16;
		}else if(component instanceof JToolBar || component instanceof JButton) {
			return 24;
		}else {
			return 16;
		}
	}
}
