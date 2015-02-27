package sec.web.renderer.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import sec.web.renderer.model.CommonURL;

import com.google.gson.Gson;

public class IoUtilities {
	private final static Logger LOGGER = Logger.getLogger(IoUtilities.class.getName());	
	private static Properties props;
	private CommonURL baseURL;
	private ArrayList<String> plugins = new ArrayList<String>();	
	private String pluginDirectory;
	private String localPluginPath;
	private final String DEFAULT_DIRECTORY_NAME = "plugins";
	private final String DEFAULT_SERVICE_NAME = "mil-sym-service";
	private boolean enablePlugins = true;

	public IoUtilities() {
		props = ResourceUtils.loadResource("properties/prop.properties", this.getClass().getClassLoader());
		Boolean enabled = Boolean.valueOf((String)props.get("enablePlugins"));
		enablePlugins = (enabled == null) ? true : enabled;	
		loadCurrentWorkingDirectory();
	}
	
	public IoUtilities(Properties props) {				
		try {
			setProps(props);
			getDirContent(pluginDirectory);
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.INFO, e.getMessage());
		} catch (MalformedURLException e) {
			LOGGER.log(Level.INFO, e.getMessage());
		} catch (URISyntaxException e) {
			LOGGER.log(Level.INFO, e.getMessage());
		}
	}

	/**
	 * @param props
	 */
	private void createBaseUrl(Properties props) {
		String prot = props.getProperty("protocol") == null ? "File"  : props.getProperty("protocol");
		String host = props.getProperty("host") == null ? "" : props.getProperty("host");
		String port = props.getProperty("port") == null ? "" : props.getProperty("port");		
		
		String ctx  = (props.getProperty("serviceName") == null || props.getProperty("pluginDir") == null ) 
			        ? "" : (props.getProperty("serviceName") + "/" + props.getProperty("pluginDir"));		
		
		this.pluginDirectory = props.getProperty("pluginDir");
		this.localPluginPath = props.getProperty("pluginDirLocation");

		if (ctx == "") {
			ctx = localPluginPath;
		}
		
		this.baseURL = new CommonURL(prot, host, port, ctx);
	}	

	
	public ArrayList<String> getDirContent(String filePath) throws FileNotFoundException, URISyntaxException, MalformedURLException {		
		File dir;
		URL pluginDir = null;
		
		if (filePath != null) {
			pluginDir = this.getClass().getClassLoader().getResource(filePath);
		}
		
		if (pluginDir == null) {
			dir = new File(localPluginPath);
		} else {			
			dir = new File(pluginDir.toURI());				
		}
		
		System.out.println("Retrieving contents from: \t" + dir.getAbsolutePath());
		
		if (!dir.exists()) {
			System.out.println("location does not exists ... Creating " + dir.getAbsolutePath());
			dir.mkdirs();
		}
		
		String[] files = dir.list();
		for (String s : files) {
			plugins.add(s);
		}
		
		return plugins;
	}
		
	public String getDirContent(String filePath, char delimiter) {
		reloadPlugins();
		File dir = new File(localPluginPath);
		if (!dir.exists()) {
			dir.mkdirs();
			System.out.println("location does not exists ... Creating " + dir.getAbsolutePath());
		}

		String[] files = dir.list();

		return buildDelimitedList(files, delimiter);
	}
	
	private void loadDirContent(String filePath) {
		File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdirs();
			System.out.println("location does not exists ... Creating " + dir.getAbsolutePath());
		}

		String[] files = dir.list();
		
		if (files != null) {

			for (String plugin : files) {
				this.plugins.add(plugin);
			}
		}
	}
	
	private void reloadPlugins() {
		if (this.enablePlugins) {
			this.plugins.clear();
			loadDirContent(baseURL.toString());
		}
	}
			
	public String getContentAsString(char delimiter) throws IOException {
		reloadPlugins();
		StringBuilder s = new StringBuilder();
		for (String str : this.plugins) {
			s.append(str + delimiter);			
		}

		String retVal = s.toString();
		if (retVal.endsWith(",")) {
			retVal = retVal.substring(0, retVal.length() - 1);
		} else {
			throw new IOException("No Plugins were found at:\t" + baseURL.toString());
		}
		
		return  retVal;
	}
		
	public ArrayList<String> getContentAsUrlList() {
		ArrayList<String> pluginUrls = new ArrayList<String>();
		
		for (String s : this.plugins) {
			pluginUrls.add(buildUrlList(s));			
		}
		
		return pluginUrls;
	}
		
	private String buildUrlList(String s) {
		String url = baseURL.toString() + "/" + s;
		
		if (url.contains("../")) {
			url = url.replace("../", "");
		}
		
		System.out.println(url.toString());
		// http://localhost:8080/mil-symbology-renderer/plugins/BridgeRenderer.jar
		
		return url;
	}
		
	private String buildDelimitedList(String[] list, char delimiter) {
		StringBuilder sb = new StringBuilder();
		
		for (String s : list) {
			sb.append(s);
			sb.append(delimiter);
		}		
		String result = sb.toString().substring(sb.toString().lastIndexOf(delimiter) - 1);
		
		return result;
	}
	
		
	public CommonURL getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(CommonURL baseURL) {
		this.baseURL = baseURL;
	}

	public ArrayList<String> getPlugins() {
		reloadPlugins();
		return plugins;
	}

	public void setPlugins(ArrayList<String> plugins) {
		this.plugins = plugins;
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		try {
			IoUtilities.props = props;
			createBaseUrl(props);
			getDirContent(baseURL.getContextPath());
			
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.INFO, e.getMessage());
		} catch (MalformedURLException e) {
			LOGGER.log(Level.INFO, e.getMessage());
		} catch (URISyntaxException e) {
			LOGGER.log(Level.INFO, e.getMessage());
		}
	}
	
	public String loadCurrentWorkingDirectory() {
		String curLoc = ""; 
		try {
			if (this.enablePlugins) {
				curLoc = System.getProperty("user.dir");			
				curLoc = modifyCurrentLocation(curLoc);
				System.out.println(curLoc);
				createDirectories(curLoc);
				
				baseURL = new CommonURL();
				baseURL.setContextPath(curLoc);
			}		

		} catch (Exception e) {
			e.printStackTrace();
		}
		return curLoc;
	}
	
	/**
	 * @param curLoc
	 * 			String representation of current working directory to mold it into application directory
	 * */
	private String modifyCurrentLocation(String curLoc) {		
		props.setProperty("directoryName", DEFAULT_DIRECTORY_NAME);
		String newLoc = (curLoc.endsWith(File.separator)) ? curLoc + DEFAULT_SERVICE_NAME + File.separator + DEFAULT_DIRECTORY_NAME
				: (curLoc + File.separator + DEFAULT_SERVICE_NAME + File.separator + DEFAULT_DIRECTORY_NAME);		
		props.setProperty("directoryPath", newLoc);
		
		return newLoc;
	}
	
	public static String correctPath(String s, String delim) {
		String pattern = Pattern.quote(delim);
		String[] tokens = s.split(pattern);
		StringBuilder sb = new StringBuilder();
		boolean done = false;

		for (String seg : tokens) {
			if (!done) {
				sb.append(seg);
				sb.append(File.separator);			
				if (seg.contains("apache-tomcat-")) {		
					done = true;
				}
			}
		}
		
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	
	
	public static String buildDelimitedList(ArrayList<String> list, char delimiter) {
		StringBuilder sb = new StringBuilder();
		
		for (String s : list) {
			sb.append(s);
			sb.append(delimiter);
		}		
		String result = sb.toString().substring(0, sb.toString().lastIndexOf(delimiter));
		
		return result;
	}
	
	public static String buildUrlDelimitedList(ArrayList<String> list, char delimiter, String baseUrl) {
		StringBuilder sb = new StringBuilder();
		
		for (String s : buildUrl(baseUrl, list)) {
			sb.append(s);
			sb.append(delimiter);
			sb.append(" ");			
		}		
		String result = (sb.toString().length() > 2) ? sb.toString().substring(0, sb.toString().lastIndexOf(delimiter)) : "";
		
		return result;
	}
	
	public static String buildJsonUrlList(ArrayList<String> list, String baseUrl) {
		Gson gson = new Gson();
		String json = gson.toJson(buildUrl(baseUrl, list)); 
		
		return json;
	}
	
		
	private static ArrayList<String> buildUrl(String baseUrl, ArrayList<String> list) {
		ArrayList<String> urls = new ArrayList<String>();
		baseUrl = (baseUrl.endsWith("/") ? baseUrl : baseUrl + "/");
		baseUrl += ("/" + props.getProperty("directoryName") + "/");	
		for (String s : list) {
			urls.add(baseUrl + s);
		}
		return urls;
	}	
	
	
	public void createDirectories(String path) {
		try {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
				System.out.println("creating directory:\t" + dir.getCanonicalPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Properties getProperties() {
		return props;
	}
}