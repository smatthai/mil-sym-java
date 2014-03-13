/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.web.renderer.utils;

import ArmyC2.C2SD.Utilities.ErrorLogger;

import java.util.Map;

import sec.web.renderer.SECRenderer;

/**
 *
 * @author michael.spinelli
 */
public class MultiPointUtils {
    
	public static String RenderSymbol(String symbolID, Map<String, String> params) {
		String returnVal = "";
		String id = "ID";
		String name = "NAME";
		String description = "DESCRIPTION";
		// String symbolID
		String controlPoints = "0,0";
		String altitudeMode = "clampToGround";
		double scale = 50000;
		String bbox = "";
		String modifiers = "";
		int format = 0;
		int symStd = 0;
		boolean debug = false;
		try {
			if (params.containsKey("ID"))
				id = params.get("ID");
			if (params.containsKey("NAME"))
				name = params.get("NAME");
			if (params.containsKey("DESCRIPTION"))
				description = params.get("DESCRIPTION");
			if (params.containsKey("CONTROLPOINTS"))
				controlPoints = params.get("CONTROLPOINTS");
			if (params.containsKey("ALTITUDEMODE"))
				altitudeMode = params.get("ALTITUDEMODE");
			if (params.containsKey("SCALE"))
				scale = Double.parseDouble(params.get("SCALE"));
			if (params.containsKey("BBOX"))
				bbox = params.get("BBOX");
			if (params.containsKey("MODIFIERS"))
				modifiers = params.get("MODIFIERS");
			if (params.containsKey("FORMAT"))
				format = Integer.parseInt(params.get("FORMAT"));
			if (params.containsKey("SYMSTD"))
				symStd = Integer.parseInt(params.get("SYMSTD"));

			if (debug) {
				System.out.println("SymbolID: " + symbolID);
				System.out.println("Param values:");
				System.out.println(ErrorLogger.PrintStringMap(params));
			}

			returnVal = SECRenderer.getInstance().RenderMultiPointSymbol(id, name, description, symbolID, controlPoints,
					altitudeMode, scale, bbox, modifiers, format, symStd);
		} catch (Exception exc) {
			System.err.println(exc.getMessage());
			exc.printStackTrace();
		}
		if (debug) {
			// System.out.println("MultiPointUtils.RenderSymbol() return: ");
			// System.out.println(returnVal);
		}
		return returnVal;
	}

	@SuppressWarnings("unused")
	public static String RenderSymbol2D(String symbolID, Map<String, String> params) {
		String returnVal = "";
		String id = "ID";
		String name = "NAME";
		String description = "DESCRIPTION";
		// String symbolID
		String controlPoints = "0,0";
		String altitudeMode = "clampToGround";
		double scale = 50000;
		String bbox = "";
		String modifiers = "";
		int pixelWidth = 1280;
		int pixelHeight = 1024;
		int format = 0;
		int symStd = 0;
		boolean debug = false;
		try {
			if (params.containsKey("ID"))
				id = params.get("ID");
			if (params.containsKey("NAME"))
				name = params.get("NAME");
			if (params.containsKey("DESCRIPTION"))
				description = params.get("DESCRIPTION");
			if (params.containsKey("CONTROLPOINTS"))
				controlPoints = params.get("CONTROLPOINTS");
			if (params.containsKey("PIXELWIDTH"))
				scale = Integer.parseInt(params.get("PIXELWIDTH"));
			if (params.containsKey("PIXELHEIGHT"))
				scale = Integer.parseInt(params.get("PIXELHEIGHT"));
			if (params.containsKey("BBOX"))
				bbox = params.get("BBOX");
			if (params.containsKey("MODIFIERS"))
				modifiers = params.get("MODIFIERS");
			if (params.containsKey("FORMAT"))
				format = Integer.parseInt(params.get("FORMAT"));
			if (params.containsKey("SYMSTD"))
				symStd = Integer.parseInt(params.get("SYMSTD"));

			if (debug) {
				System.out.println("SymbolID: " + symbolID);
				System.out.println("Param values:");
				System.out.println(ErrorLogger.PrintStringMap(params));
			}

			returnVal = SECRenderer.getInstance().RenderMultiPointSymbol2D(id, name, description, symbolID, controlPoints,
					pixelWidth, pixelHeight, bbox, modifiers, format, symStd);
		} catch (Exception exc) {
			System.err.println(exc.getMessage());
			exc.printStackTrace();
		}
		if (debug) {
			// System.out.println("MultiPointUtils.RenderSymbol() return: ");
			// System.out.println(returnVal);
		}
		return returnVal;
	}
}
