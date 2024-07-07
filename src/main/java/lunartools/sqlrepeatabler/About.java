package lunartools.sqlrepeatabler;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import lunartools.FileTools;
import lunartools.ImageTools;

public class About {

	public static void showAboutDialog(JFrame jframe) {
		try {
			InputStream inputStream = About.class.getResourceAsStream("/About_"+SqlRepeatablerModel.PROGRAMNAME+".html");
			StringBuffer html=FileTools.getStringBufferFromInputStream(inputStream,StandardCharsets.UTF_8.name());
			JEditorPane editorPane = new JEditorPane("text/html", html.toString());

			editorPane.addHyperlinkListener(new HyperlinkListener(){
				@Override
				public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent){
					if (hyperlinkEvent.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
						try {
							Desktop.getDesktop().browse(hyperlinkEvent.getURL().toURI());
						} catch (IOException | URISyntaxException e) {
							e.printStackTrace();
						}
					}
				}
			});
			editorPane.setEditable(false);
			editorPane.setBackground(jframe.getBackground());

			JOptionPane.showMessageDialog(jframe, editorPane, "About "+SqlRepeatablerModel.PROGRAMNAME,JOptionPane.INFORMATION_MESSAGE,ImageTools.createImageIcon("/icons/ProgramIcon90x90.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
