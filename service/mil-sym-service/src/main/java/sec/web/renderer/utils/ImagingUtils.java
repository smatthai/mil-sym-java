package sec.web.renderer.utils;

import ArmyC2.C2SD.Utilities.MilStdAttributes;
import ArmyC2.C2SD.Utilities.ModifiersTG;
import ArmyC2.C2SD.Utilities.SymbolUtilities;
import java.awt.Color;
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

	public static byte[] getMilStd2525Png(String symbolId, HashMap<String, String> symbolInfoMap) {		

            Boolean icon = false;
            if(symbolInfoMap.containsKey("ICON"))
            {
                if(Boolean.parseBoolean(symbolInfoMap.get("ICON"))==true)
                    icon = true;
            }
                
                if(icon)
                {
                    HashMap<String, String> iconInfo = new HashMap<String, String>();
                    //if icon == true, make sure keepUnitRatio defaults to false.
                    iconInfo.put(MilStdAttributes.KeepUnitRatio,"false");
                    
                    if(SymbolUtilities.isWarfighting(symbolId))
                    {
                        Color fillColor = SymbolUtilities.getFillColorOfAffiliation(symbolId);
                        iconInfo.put(MilStdAttributes.FillColor,SymbolUtilities.colorToHexString(fillColor, Boolean.TRUE));
                    }
                    if(symbolInfoMap.containsKey(MilStdAttributes.FillColor))
                    {
                        iconInfo.put(MilStdAttributes.FillColor,symbolInfoMap.get(MilStdAttributes.FillColor));
                    }
                    if(symbolId.substring(0, 1).equals("G"))
                    {
                        Color fillColor = SymbolUtilities.getLineColorOfAffiliation(symbolId);
                        iconInfo.put(MilStdAttributes.LineColor,SymbolUtilities.colorToHexString(fillColor, Boolean.TRUE));
                    }
                    if(symbolInfoMap.containsKey(MilStdAttributes.LineColor))
                    {
                        iconInfo.put(MilStdAttributes.LineColor,symbolInfoMap.get(MilStdAttributes.LineColor));
                    }
                    if(symbolInfoMap.containsKey(MilStdAttributes.SymbologyStandard))
                    {
                        iconInfo.put(MilStdAttributes.SymbologyStandard,symbolInfoMap.get(MilStdAttributes.SymbologyStandard));
                    }
                    if(symbolInfoMap.containsKey(MilStdAttributes.KeepUnitRatio))
                    {
                        iconInfo.put(MilStdAttributes.KeepUnitRatio,symbolInfoMap.get(MilStdAttributes.KeepUnitRatio));
                    }
                    if(symbolInfoMap.containsKey(MilStdAttributes.Alpha))
                    {
                        iconInfo.put(MilStdAttributes.Alpha,symbolInfoMap.get(MilStdAttributes.Alpha));
                    }
                    if(symbolInfoMap.containsKey(MilStdAttributes.Renderer))
                    {
                        iconInfo.put(MilStdAttributes.Renderer,symbolInfoMap.get(MilStdAttributes.Renderer));
                    }
                    if(symbolInfoMap.containsKey(MilStdAttributes.PixelSize))
                    {
                        iconInfo.put(MilStdAttributes.PixelSize,symbolInfoMap.get(MilStdAttributes.PixelSize));
                    }
                    if(symbolInfoMap.containsKey(MilStdAttributes.OutlineColor))
                    {
                        iconInfo.put(MilStdAttributes.OutlineColor,symbolInfoMap.get(MilStdAttributes.OutlineColor));
                    }
                    iconInfo.put(MilStdAttributes.OutlineSymbol,"false");
                    symbolId = sanitizeSymbolID(symbolId);
                    
                    symbolInfoMap = iconInfo;
                }

                
                
                PNGInfo pngInfo = sr.getSymbolImage(symbolId, symbolInfoMap);	
                
                if(icon)
                    pngInfo = pngInfo.squareImage();
                
		return processImageModifiers(symbolInfoMap, pngInfo);
	}

        /**
         * we only have font lookups for F,H,N,U.  But the shapes match one of these
         * four for the remaining affiliations.  So we convert the string to a base
         * affiliation before we do the lookup.
         * @param {String} symbolID
         * @returns {String}
         */        
        private static String sanitizeSymbolID(String symbolID)
        {
            String code = symbolID;
            char affiliation = symbolID.charAt(1);

            if(SymbolUtilities.isWeather(symbolID)==false)
            {
                if(affiliation == 'F' ||//friendly
                        affiliation == 'H' ||//hostile
                        affiliation == 'U' ||//unknown
                        affiliation == 'N' )//neutral
                {
                    //code = code;
                }
                else if(affiliation == 'S')//suspect
                    code = code.charAt(0) + "H" + code.substring(2, 15);
                else if(affiliation == 'L')//exercise neutral
                    code = code.charAt(0) + "N" + code.substring(2, 15);
                else if(affiliation == 'A' ||//assumed friend
                        affiliation == 'D' ||//exercise friend
                        affiliation == 'M' ||//exercise assumed friend
                        affiliation == 'K' ||//faker
                        affiliation == 'J')//joker
                    code = code.charAt(0) + "F" + code.substring(2, 15);
                else if(affiliation == 'P' ||//pending
                        affiliation == 'G' ||//exercise pending
                        affiliation == 'O' ||//? brought it over from mitch's code
                        affiliation == 'W')//exercise unknown
                    code = code.charAt(0) + "U" + code.substring(2, 15);
                else
                    code = code.charAt(0) + "U" + code.substring(2, 15);

                code = code.substring(0,10) + "-----";
            }

            return code;
        };

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
                else if (symbolInfoMap.containsKey("SQUARE")) {
			tempModifierVal = symbolInfoMap.get("SQUARE");
			if (tempModifierVal != null && tempModifierVal.toLowerCase().equals("true") == true) {
				pngInfo = pngInfo.squareImage();
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
