package sec.geo;

import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

public class GeoBlock extends GeoPath {
	public GeoBlock(GeoPoint p1, GeoPoint p2, double widthMeters, double maxDistanceMeters,
			double flatnessDistanceMeters, int limit) {
		super(maxDistanceMeters, flatnessDistanceMeters, limit);
		
                GlobalCoordinates c1 = toGlobalCoord(p1);
		GlobalCoordinates c2 = toGlobalCoord(p2);                                
		GeodeticCurve curve = geoCalc.calculateGeodeticCurve(REFERENCE_ELLIPSOID, c1, c2);                
		double a1 = curve.getAzimuth();
		double a2 = curve.getReverseAzimuth();             
		double radius = widthMeters / 2;                
                
		GlobalCoordinates c = geoCalc.calculateEndingGlobalCoordinates(REFERENCE_ELLIPSOID, c1, a1 - 90, radius);                
		moveTo(c.getLongitude(), c.getLatitude());
		c = geoCalc.calculateEndingGlobalCoordinates(REFERENCE_ELLIPSOID, c2, a2 + 90, radius);                
		lineTo(c.getLongitude(), c.getLatitude());
		c = geoCalc.calculateEndingGlobalCoordinates(REFERENCE_ELLIPSOID, c2, a2 - 90, radius);                
		lineTo(c.getLongitude(), c.getLatitude());
		c = geoCalc.calculateEndingGlobalCoordinates(REFERENCE_ELLIPSOID, c1, a1 + 90, radius);                
		lineTo(c.getLongitude(), c.getLatitude());
		closePath();
	}
}
