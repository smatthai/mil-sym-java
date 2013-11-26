package sec.web.renderer.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DirectoryReaderUtils {
	private final static Logger LOGGER = Logger.getLogger(DirectoryReaderUtils.class.getName());	

	public DirectoryReaderUtils() {
		
	}	
	
	public static String getDirectoryContent(File dir) throws IOException {		
		StringBuilder sb = new StringBuilder();
		System.out.println(dir.getAbsolutePath());
		
		if (!dir.exists()) {
			dir.mkdirs();
			System.out.println("location does not exists ... Creating " + dir.getAbsolutePath());
		}		
		
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File f : files) {
				sb.append(f.getName());
				sb.append(",");
			}
		}
		
		if (sb.toString().length() <= 0) {
			throw new IOException("NO plugins were found at:\t" + dir.getAbsolutePath());
		}
		
		return sb.substring(0, sb.toString().lastIndexOf(','));
	}
	
	
	public static Properties loadProperties(String fileName, ClassLoader loader) {
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
	
	/**
	 * @param dir
	 */
	public static void emptyDirectory(File dir) {
		if (dir.exists() && dir.isDirectory()) {
			File[] tmpFiles = dir.listFiles();

			if (tmpFiles.length > 0) {
				for (File f : tmpFiles) {
					f.delete();
				}
			}
		}
	}
	
	/**
	 * @param dir
	 * @throws IOException 
	 */
	public static boolean isEmpty(File dir) throws IOException {
		boolean isEmpty = false;
		
		if (!dir.exists()) {
			LOGGER.log(Level.SEVERE, "Directory does NOT exist at " + dir.getAbsolutePath());
			throw new IOException("Directory does NOT exist at " + dir.getAbsolutePath());
		} else if ( !dir.isDirectory() ) {
			LOGGER.log(Level.SEVERE, dir.getAbsolutePath() + " is not a directory");
			throw new IOException(dir.getAbsolutePath() + " is not a directory");
		}

		isEmpty = (dir.listFiles().length <= 0) ? isEmpty = true : isEmpty;
		
		return isEmpty;
	}
}
