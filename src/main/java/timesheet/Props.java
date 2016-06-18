package timesheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

public class Props {

	private static final String CONFIG_PROPERTIES = "config.properties";
	private static Properties prop = new Properties();

	public static void setProperty(String propKey, String propVal) {
		File f = new File(CONFIG_PROPERTIES);
		if (!f.exists() && !f.isDirectory()) {
			Path file = Paths.get(CONFIG_PROPERTIES);
			try {
				Files.write(file, new ArrayList<String>(), Charset.forName("UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try (InputStream input = new FileInputStream(CONFIG_PROPERTIES);) {
			prop.load(input);
			// set the properties value
			prop.put(propKey, propVal);
			// save properties to project root folder
			try (OutputStream output = new FileOutputStream(CONFIG_PROPERTIES);) {
				prop.store(output, null);
			}

		} catch (IOException io) {
			io.printStackTrace();
			throw new RuntimeException("value not set for key: " + propKey + " - value: " + propVal);
		}
	}

	public static String getProperty(String propKey) {
		try (InputStream input = new FileInputStream(CONFIG_PROPERTIES);) {

			// load a properties file
			prop.load(input);
			return prop.getProperty(propKey);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("value not found for key: " + propKey);
		}

	}

	public static void deleteProperty(String string) {
		try (InputStream input = new FileInputStream(CONFIG_PROPERTIES);) {
			prop.load(input);
			// set the properties value
			prop.remove(string);
			// save properties to project root folder
			try (OutputStream output = new FileOutputStream(CONFIG_PROPERTIES);) {
				prop.store(output, null);
			}

		} catch (IOException io) {
			io.printStackTrace();
			throw new RuntimeException("value not deleted for key: " + string);
		}

	}
}
