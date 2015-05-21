package sec.web.renderer.services.imaging;

import sec.web.renderer.SECRenderer;
import sec.web.renderer.utilities.PNGInfo;

public class SECRendererImpl {
	protected static SECRenderer sr;
	
	public SECRendererImpl() {
		sr = SECRenderer.getInstance();
		//sr.matchSECWebRendererAppletDefaultRendererSettings();
	}
	
	
	public static byte[] getMilStd2525Png(String symbolId, String queryString) {		
		String path = symbolId;
	
		if (queryString != null || "".equals(queryString)) {
			path += "?" + queryString;			
		}
		
		PNGInfo pngInfo = sr.getMilStdSymbolImageFromURL(path);
		byte[] pngResponse = (pngInfo == null) ? null : pngInfo.getImageAsByteArray();		
		
		return pngResponse;
	}

}
