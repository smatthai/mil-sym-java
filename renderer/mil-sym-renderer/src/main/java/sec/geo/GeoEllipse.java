package sec.geo;

public class GeoEllipse extends GeoPath {
	public GeoEllipse(GeoPoint pivot, double widthMeters, double heightMeters, double maxDistanceMeters,
			double flatnessDistanceMeters, int limit) {
		super(maxDistanceMeters, flatnessDistanceMeters, limit);
		arcTo(pivot, widthMeters, heightMeters, 0, 180);
		arcTo(pivot, widthMeters, heightMeters, 180, 0);
	}
}
