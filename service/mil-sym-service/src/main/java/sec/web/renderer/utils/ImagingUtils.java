package sec.web.renderer.utils;

import ArmyC2.C2SD.Utilities.MilStdAttributes;
import ArmyC2.C2SD.Utilities.SymbolUtilities;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import sec.web.renderer.SECRenderer;
import sec.web.renderer.utilities.JavaRendererUtilities;
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
                        if(val != null && val.equals("") == false)
                        {
                            params.put(key.toUpperCase(), val);
                        }
			
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

	public static byte[] getMilStd2525PngBytes(String symbolId, Map<String, String> symbolInfoMap) {		

            Boolean icon = false;
            if(symbolInfoMap.containsKey("ICON"))
            {
                if(Boolean.parseBoolean(symbolInfoMap.get("ICON"))==true)
                    icon = true;
            }
            int eWidth = -1;
            int eHeight = -1;
            int ecX = -1;
            int ecY = -1;
            int buffer = -1;
            if(symbolInfoMap.containsKey("EWIDTH"))
            {
                eWidth = Math.round(Float.parseFloat(symbolInfoMap.get("EWIDTH")));
            }
            if(symbolInfoMap.containsKey("EHEIGHT"))
            {
                eHeight = Math.round(Float.parseFloat(symbolInfoMap.get("EHEIGHT")));
            }
            if(symbolInfoMap.containsKey("ECENTERX"))
            {
                ecX = Math.round(Float.parseFloat(symbolInfoMap.get("ECENTERX")));
            }
            if(symbolInfoMap.containsKey("ECENTERY"))
            {
                ecY = Math.round(Float.parseFloat(symbolInfoMap.get("ECENTERY")));
            }
            if(symbolInfoMap.containsKey("BUFFER"))
            {
                buffer = Integer.parseInt(symbolInfoMap.get("BUFFER"));
            }
            
            PNGInfo pngInfo = getMilStd2525Png(symbolId, symbolInfoMap);

            if(icon)
                pngInfo = pngInfo.squareImage();
            else if(eWidth > 0 && eHeight > 0 && ecX >= 0 && ecY >= 0 && buffer > 0)
            {
                pngInfo = pngInfo.fitImage(eWidth, eHeight, ecX, ecY, buffer);
            }
            else
            {
                pngInfo =  processImageModifiers(symbolInfoMap, pngInfo);
            }
            
            Boolean meta = false;
            String tempModifierVal = null;
            if (symbolInfoMap.containsKey("META")) {
                tempModifierVal = symbolInfoMap.get("META");
                if (tempModifierVal != null && tempModifierVal.toLowerCase().equals("true") == true) {
                        meta = true;
                }
            }

            byte[] pngResponse = null;
            if (meta) {
                    pngResponse = (pngInfo == null) ? null : pngInfo.getImageAsByteArrayWithMetaInfo();
            } else {
                    pngResponse = (pngInfo == null) ? null : pngInfo.getImageAsByteArray();
            }
            return pngResponse;
	}
        
        /**
         * 
         * @param symbolId
         * @param symbolInfoMap
         * @return 
         */
        public static PNGInfo getMilStd2525Png(String symbolId, Map<String, String> symbolInfoMap) {		

            Boolean icon = false;
            if(symbolInfoMap.containsKey("ICON"))
            {
                if(Boolean.parseBoolean(symbolInfoMap.get("ICON"))==true)
                    icon = true;
            }
                            
            if(icon)
            {
                //strip unwanted modifiers
                symbolInfoMap = JavaRendererUtilities.parseIconParameters(symbolId, symbolInfoMap);                
                symbolId = JavaRendererUtilities.sanitizeSymbolID(symbolId);
            }

            PNGInfo pngInfo = sr.getSymbolImage(symbolId, symbolInfoMap);	

            return pngInfo;
            

	}
    

	/**
	 * @param symbolInfoMap
	 * @param pngInfo
	 * @return
	 */
	private static PNGInfo processImageModifiers(Map<String, String> symbolInfoMap, PNGInfo pngInfo) {
		String tempModifierVal = null;
		
		                
		if (symbolInfoMap.containsKey("CENTER")) {
			tempModifierVal = symbolInfoMap.get("CENTER");
			if (tempModifierVal != null && tempModifierVal.toLowerCase().equals("true") == true) {
				pngInfo = pngInfo.centerImage();
			}
		}
                else if (symbolInfoMap.containsKey("SQUARE")) {
			tempModifierVal = symbolInfoMap.get("SQUARE");
			if (tempModifierVal != null && tempModifierVal.toLowerCase().equals("true") == true) {
				pngInfo = pngInfo.squareImage();
			}
		}

		return pngInfo;
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

			sr.loadPluginsFromDirectory(f);
			sr.refreshPlugins();
		}
	}
	
	public ArrayList<String> getPreviousPluginContent() {
		return previousPluginContent;
	}
	
	public void setPreviousPluginContent(ArrayList<String> newContent) {
		this.previousPluginContent = newContent;
	}
}
