package sec.geo;

public class GeoArc extends GeoPath {
	public GeoArc(GeoPoint pivot, double widthMeters, double heightMeters, double leftAzimuth, double rightAzimuth,
			double maxDistanceMeters, double flatnessDistanceMeters, int limit) {
		super(maxDistanceMeters, flatnessDistanceMeters, limit);
		
		moveTo(pivot);
		arcTo(pivot, widthMeters, heightMeters, leftAzimuth, rightAzimuth);
		closePath();
	}
}
