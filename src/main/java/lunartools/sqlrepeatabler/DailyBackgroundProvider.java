package lunartools.sqlrepeatabler;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import javax.imageio.ImageIO;

import lunartools.FileTools;
import lunartools.ImageTools;
import lunartools.sqlrepeatabler.util.SSKPFilterInputStream;

public class DailyBackgroundProvider {
	private static Image imageBackground;

	private static final ArrayList<String> affenvisionen=new ArrayList<>(Arrays.asList(
			"Ar-Es-Tschi pirate",
			"Affenparadies",
			"Soldering ape",
			"Titanic Apeisode One",
			"Pennywise",
			"Batape",
			"Darth Ape",
			"Jason",
			"SQL pirate",
			"Titanic Apeisode Two",
			"Ape Lisa",
			"Albert Apestein",
			"Fortune teller",
			"First ape on the moon",
			"Apeula",
			"2001 An Ape Odyssey",
			"Sphinx",
			"ApeMaid",
			"Ape3PO"
			));
	
	public static Image getImage() {
		if(imageBackground==null) {
			int indexBackground=Calendar.getInstance().get(Calendar.DAY_OF_YEAR) % affenvisionen.size();
			indexBackground=11;
			String resourceBackground="/pixelzeug/"+affenvisionen.get(indexBackground)+".sskp";
			try {
//				imageBackground = ImageTools.createImageFromResource(resourceBackground);
				imageBackground = createImage(resourceBackground);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return imageBackground;
	}

	private static Image createImage(String resourcePath) throws IOException {
		try (InputStream inputStream=new SSKPFilterInputStream(ImageTools.class.getResourceAsStream(resourcePath))
				){
			byte[] imagedata=FileTools.readInputStreamToByteArray(inputStream);
			FileTools.writeByteArrayToFile(new File("c:/temp/image.jpg"), imagedata);
			Image image=Toolkit.getDefaultToolkit().createImage(imagedata);
			return image;
		}
	}
	
}
