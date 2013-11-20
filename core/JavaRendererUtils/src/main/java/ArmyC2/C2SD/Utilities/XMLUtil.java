package ArmyC2.C2SD.Utilities;

import java.util.ArrayList;

/**
 * 
 * @author michael.spinelli
 */
public class XMLUtil {

    public static final String SymbolConstantsB = "XML/SymbolConstantsB.xml";
    public static final String SymbolConstantsC = "XML/SymbolConstantsC.xml";
    
    public static final String SinglePointB = "XML/SinglePointB.xml";
    public static final String SinglePointC = "XML/SinglePointC.xml";
    
    public static final String UnitConstantsB = "XML/UnitConstantsB.xml";
    public static final String UnitConstantsC = "XML/UnitConstantsC.xml";
    
    public static final String UnitFontMappingsB = "XML/UnitFontMappingsB.xml";
    public static final String UnitFontMappingsC = "XML/UnitFontMappingsC.xml";
    
    public static final String TacticalGraphics = "XML/TacticalGraphics.xml";
    
        
    
	/**
	 * This method returns the list of objects in a list form.
	 * 
	 * @param xml
	 * @param startTag
	 * @param endTag
	 * @return
	 */
	public static ArrayList<String> getItemList(String xml, String startTag, String endTag) {
		ArrayList<String> list = new ArrayList<String>();

		String value = null;
		try {
			int index = xml.indexOf(startTag);
			int index2 = 0;
			while (xml != null && index > -1 && index2 > -1) {
				index2 = xml.indexOf(endTag, index);
				if (index2 > -1) {
					value = xml.substring(index, index2 + endTag.length());
					list.add(value);
					xml = xml.substring(index2 + endTag.length());
					index = xml.indexOf(startTag);
				}
			}
		} catch (Throwable thrown) {
		}
		return list;
	}

	/**
	 * This method parse the xml tag value.
	 * 
	 * @param xml
	 * @param tag
	 * @param endTag
	 * @return
	 */
	public static String parseTagValue(String xml, String tag, String endTag) {
		String val = "";
		try {
			int index1 = xml.indexOf(tag) + tag.length();
			int index2 = xml.indexOf(endTag, index1);
			if (index1 > -1 && index2 > -1) {
				val = xml.substring(index1, index2);
			}
		} catch (Throwable thrown) {
			val = null;
		}
		return val;
	}

	/**
	 * This method changes the value of the xml.
	 * 
	 * @param xml
	 * @param tag
	 * @param endTag
	 * @param value
	 * @return
	 */
	public static String changeTagValue(String xml, String tag, String endTag, String value) {
		int offset = 0;
		String v = value;
		while (xml.indexOf(tag, offset) > -1) {
			offset = xml.indexOf(tag, offset);
			int endoffset = xml.indexOf(endTag, offset);
			// Replace value
			xml = xml.substring(0, offset + tag.length()) + v + xml.substring(endoffset);
			offset = endoffset;
		}
		return xml;
	}


}
