package lunartools.sqlrepeatabler;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import lunartools.FileTools;
import lunartools.ImageTools;
import lunartools.sqlrepeatabler.util.SSKPFilterInputStream;

public class DailyBackgroundProvider {
	private static Image imageBackground;

	private static final ArrayList<String> affenvisionen=new ArrayList<>(Arrays.asList(
			"Ar-Es-Tschi pirate",
			"Affenparadies",
			"Soldering Ape",
			"Titanic Apeisode One",
			"Apewise",
			"Batape",
			"Darth Ape",
			"Jason",
			"SQL Pirate",
			"Titanic Apeisode Two",
			"Ape Lisa",
			"Albert Apestein",
			"Fortune Teller",
			"First Ape on the Moon",
			"Count Apeula",
			"2001 An Ape Odyssey",
			"Sphinx",
			"Apemaid",
			"Ape3PO",
			"Wyatt Ape"			
			));

	public static Image getImage() {
		if(imageBackground==null) {
			int indexBackground=Calendar.getInstance().get(Calendar.DAY_OF_YEAR) % affenvisionen.size();
			String resourceBackground="/pixelzeug/"+affenvisionen.get(indexBackground)+".sskp";
			try {
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
			Image image=Toolkit.getDefaultToolkit().createImage(imagedata);
			return image;
		}
	}

}
