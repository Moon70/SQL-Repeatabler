package lunartools.sqlrepeatabler;

import java.io.IOException;
import java.io.InputStream;

import lunartools.FileTools;

public class TestHelper {

	public static StringBuilder removeCR(StringBuilder sb) {
		int i;
		while((i=sb.indexOf("\r"))!=-1) {
			sb.deleteCharAt(i);
		}
		return sb;
	}

	public static StringBuilder getResourceAsStringBuilder(String resource) throws IOException {
		try(InputStream inputStream=TestHelper.class.getResourceAsStream(resource)){
			return FileTools.readInputStreamToStringBuilder(inputStream,"UTF-8");
		}
	}

	public static StringBuilder getCrStrippedResourceAsStringBuffer(String resource) throws IOException {
		return removeCR(getResourceAsStringBuilder(resource));
	}

}
