package lunartools.sqlrepeatabler.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.swing.JTextArea;

import lunartools.ImageTools;

public class JBackgroundTextArea extends JTextArea {
	private Image imageNormal;
	private Image imageBright;

	public JBackgroundTextArea(int rows, int columns, String backgroundResourceNormal, String backgroundResourceBright) {
		super(rows,columns);
		try {
			imageNormal=ImageTools.createImageFromResource(backgroundResourceNormal);
			imageBright=ImageTools.createImageFromResource(backgroundResourceBright);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(this.getText().length()>0) {
			if(imageBright!=null) {
				int offsetY=(this.getHeight()-imageBright.getHeight(null))>>1;
				g.drawImage(imageBright, 0, offsetY, this);
			}
		}else {
			if(imageNormal!=null) {
				int offsetY=(this.getHeight()-imageBright.getHeight(null))>>1;
				g.drawImage(imageNormal, 0, offsetY, this);
			}
		}
		super.paintComponent(g);
	}

}
