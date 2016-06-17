package timesheet;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Props {

	private static Properties prop = new Properties();

	public static void setProperty(String propKey, String propVal) {
		try (InputStream input = new FileInputStream("config.properties");) {
			prop.load(input);
			// set the properties value
			prop.put(propKey, propVal);
			// save properties to project root folder
			try (OutputStream output = new FileOutputStream("config.properties");) {
				prop.store(output, null);
			}

		} catch (IOException io) {
			io.printStackTrace();
			throw new RuntimeException("value not set for key: " + propKey + " - value: " + propVal);
		}
	}

	public static String getProperty(String propKey) {
		try (InputStream input = new FileInputStream("config.properties");) {

			// load a properties file
			prop.load(input);
			return prop.getProperty(propKey);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("value not found for key: " + propKey);
		}

	}
}
