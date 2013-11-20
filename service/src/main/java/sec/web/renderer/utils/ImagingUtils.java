package sec.web.renderer.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import sec.web.renderer.SECRenderer;
import sec.web.renderer.utilities.PNGInfo;

@SuppressWarnings("unused")
public class ImagingUtils {
	private static final Logger LOGGER = Logger.getLogger(ImagingUtils.class.getName());
	private static SECRenderer sr = SECRenderer.getInstance();
	private static IoUtilities io = new IoUtilities();
	private ArrayList<String> previousPluginContent;
	
	
	public ImagingUtils() {
		sr.matchSECWebRendererAppletDefaultRendererSettings();
		this.previousPluginContent = sr.getListOfLoadedPlugins();
	}


	public static HashMap<String, String> getURLParameters(HttpServletRequest request) {
		Enumeration<String> paramNames = request.getParameterNames();
		HashMap<String, String> params = new HashMap<String, String>();	
		
		while (paramNames.hasMoreElements()) {			
			String key = (String) paramNames.nextElement();
			String val = request.getParameter(key);			
			params.put(key.toUpperCase(), val);
			
			// LOGGER.log(Level.INFO, "PROCESSING\nkey: "+key + "\tValue: " + val);
		}

		return params;
	}
	
	public static byte[] getMilStd2525Png(String url, String queryString) {
		String symbolID = url.substring(url.lastIndexOf("/"));				
	
		if (queryString != null || "".equals(queryString)) {
			symbolID += "?" + queryString;			
		}
		
		PNGInfo pngInfo = sr.getMilStdSymbolImageFromURL(symbolID);
		byte[] pngResponse = (pngInfo == null) ? null : pngInfo.getImageAsByteArray();		
		
		return pngResponse;
	}

	public static byte[] getMilStd2525Png(String symbolId, HashMap<String, String> symbolInfoMap) {		
		PNGInfo pngInfo = sr.getSymbolImage(symbolId, symbolInfoMap);		
		
		return processImageModifiers(symbolInfoMap, pngInfo);
	}


	/**
	 * @param symbolInfoMap
	 * @param pngInfo
	 * @return
	 */
	private static byte[] processImageModifiers(HashMap<String, String> symbolInfoMap, PNGInfo pngInfo) {
		boolean meta = false;
		String tempModifierVal = null;
		byte[] pngResponse = null;
		
		if (symbolInfoMap.containsKey("CENTER")) {
			tempModifierVal = symbolInfoMap.get("CENTER");
			if (tempModifierVal != null && tempModifierVal.toLowerCase().equals("true") == true) {
				pngInfo = pngInfo.centerImage();
			}
		}

		if (symbolInfoMap.containsKey("META")) {
			tempModifierVal = symbolInfoMap.get("META");
			if (tempModifierVal != null && tempModifierVal.toLowerCase().equals("true") == true) {
				meta = true;
			}
		}

		if (meta) {
			pngResponse = (pngInfo == null) ? null : pngInfo.getImageAsByteArrayWithMetaInfo();
		} else {
			pngResponse = (pngInfo == null) ? null : pngInfo.getImageAsByteArray();
		}
		return pngResponse;
	}

	public static byte[] getKml(String url, String symbolId, HashMap<String, String> symbolInfoMap) {
		String kmlInfo = sr.getSymbolImageKML(url, symbolId, symbolInfoMap);		
		return kmlInfo.getBytes();
	}
        
        public static String getKmlString(String url, String symbolId, HashMap<String, String> symbolInfoMap) {
		return sr.getSymbolImageKML(url, symbolId, symbolInfoMap);		
	}
	
	public static void reloadPlugins() {
		ImagingUtils iUtils = new ImagingUtils();
		ArrayList<String> latestPluginContent = io.getPlugins();

		if (!iUtils.getPreviousPluginContent().equals(latestPluginContent)) {
			File f = new File(io.loadCurrentWorkingDirectory());
			//System.out.println("\n\nLoading plugins from:\t" + f.getAbsolutePath() + "\n\n");
			sr.loadPluginsFromDirectory(f);
			sr.refreshPlugins();
			//System.out.println("\n\nRefreshing SR plugins");
		}

	}
	
	public ArrayList<String> getPreviousPluginContent() {
		return previousPluginContent;
	}
	
	public void setPreviousPluginContent(ArrayList<String> newContent) {
		this.previousPluginContent = newContent;
	}
}
