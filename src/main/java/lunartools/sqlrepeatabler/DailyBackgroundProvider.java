package lunartools.sqlrepeatabler;

import java.awt.Image;
import java.io.IOException;
import java.util.Calendar;

import lunartools.ImageTools;

public class DailyBackgroundProvider {
	private static final int NUMBER_OF_BACKGROUNDS=3;
	private static Image imageBackground;

	public static Image getImage() {
		if(imageBackground==null) {
			int indexBackground=Calendar.getInstance().get(Calendar.DAY_OF_YEAR) % NUMBER_OF_BACKGROUNDS;
			String resourceBackground="/Background"+indexBackground+".jpg";
			try {
				imageBackground = ImageTools.createImageFromResource(resourceBackground);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return imageBackground;
	}

}
