package sec.geo.kml;

public class KmlOptions {

	
	public enum AltitudeMode {
		ABSOLUTE ("absolute"),
		RELATIVE_TO_GROUND ("relativeToGround"),
		RELATIVE_TO_SEA_FLOOR ("relativeToSeaFloor"),
		CLAMP_TO_GROUND ("clampToGround"),
		CLAMP_TO_SEA_FLOOR("clampToSeaFloor");
		
		private String mode = "absolute";
		
		private AltitudeMode(String mode) {
			this.mode = mode;
		}
		
		public static AltitudeMode fromString(String mode) {			
			if(mode != null) {
				for(AltitudeMode am : AltitudeMode.values()) {
					if(am.getMode().equals(mode)) {
						return am;
					}
				}				
			}
			throw new IllegalArgumentException("No AltitudeMode with mode \""+mode+"\" found");		
		}		
		
		public String toString(){
			return this.mode;
		}

		public String getMode() {
			return mode;
		}
	}
	
	
}
