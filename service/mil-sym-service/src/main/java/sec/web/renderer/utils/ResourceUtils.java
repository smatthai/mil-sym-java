package sec.web.renderer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ResourceUtils {
	public static final Logger LOGGER = Logger.getLogger(ResourceUtils.class.getName());
	
	public static Properties loadResource(String fileName) {
		return loadResource(fileName, null);
	}
	
	public static Properties loadResource(String fileName, ClassLoader loader) {
		Properties props = new Properties();
		if (fileName == null || fileName.equals("")) {
			throw new IllegalArgumentException("null reference: " + fileName);
		}
		if (fileName.startsWith("/")) {
			fileName = fileName.substring(1);
		}

		InputStream inStream = null;
		try {
			if (loader == null) {
				loader = ClassLoader.getSystemClassLoader();
			}

			inStream = loader.getResourceAsStream(fileName);
			props.load(inStream);

		} catch (IOException ioe) {
			LOGGER.log(Level.WARNING, ioe.getMessage());
			ioe.printStackTrace();
		}
		return props;
	}
	
	public static boolean isPluginsEnabled() {
		Properties props = loadResource("properties/prop.properties", null);
		Boolean enabled = Boolean.valueOf((String)props.get("enablePlugins"));
		boolean enablePlugins = (enabled == null) ? true : enabled;
		
		return enablePlugins;
	}

}
