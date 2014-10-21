package sec.web.renderer.model;

public enum RenderingDataEnums {
	IMAGE("image"), KML("kml"), MP3D("mp3d"), MP2D("mp2d"), SPBI("spbi"), SVG("svg"), SVGZ("svgz");

	private String type;
	
	private RenderingDataEnums(String type) {
		this.type = type;
	}
	
	public String getRequestType() {
		return this.type;
	}
	
	
	/**
	 * @param text
	 * @throws This method is the same as the valueOf(String) method but gaurds against case sensitivity that could result in an
	 *     Exception in thread main java.lang.IllegalArgumentexcetiopn: No enum const class
	 * 			if text does not match any enum type in this class 
	 * */
	public static RenderingDataEnums fromString(String text) {
		RenderingDataEnums rde = null;
		if (text != null && text.length() > 2) {
			for (RenderingDataEnums enums : RenderingDataEnums.values()) {				
				if (text.equalsIgnoreCase(enums.type)) {
					rde = enums;					
				}
			}
		}
		return rde;
	}
}
