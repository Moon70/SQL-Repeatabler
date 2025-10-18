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
			"Soldering ape",
			"Titanic Apeisode One",
			"Pennywise",
			"Batape",
			"Darth Ape",
			"Jason",
			"SQL pirate",
			"Titanic Apeisode Two",
			"Ape Lisa"
			));
	
	
	public static Image getImage() {
		if(imageBackground==null) {
			int indexBackground=Calendar.getInstance().get(Calendar.DAY_OF_YEAR) % affenvisionen.size();
indexBackground=7;
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
		try (InputStream inputStream = ImageTools.class.getResourceAsStream(resourcePath);
				InputStream inputStream2=new SSKPFilterInputStream(inputStream)
				){
			if(inputStream2==null) {
				return null;
			}
			byte[] imagedata=FileTools.readInputStreamToByteArray(inputStream2);
			Image image=Toolkit.getDefaultToolkit().createImage(imagedata);
			return image;
		}
	}
	
}
