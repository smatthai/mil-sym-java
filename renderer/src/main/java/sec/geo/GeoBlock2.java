package sec.geo;

import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import static sec.geo.GeoPath.REFERENCE_ELLIPSOID;

public class GeoBlock2 extends GeoPath {
	public GeoBlock2(GeoPoint p1, GeoPoint p2, double leftWidthMeters, double rightWidthMeters, double maxDistanceMeters,
			double flatnessDistanceMeters, int limit) {
		super(maxDistanceMeters, flatnessDistanceMeters, limit);
		
                GlobalCoordinates c1 = toGlobalCoord(p1);
		GlobalCoordinates c2 = toGlobalCoord(p2);                                
		GeodeticCurve curve = geoCalc.calculateGeodeticCurve(REFERENCE_ELLIPSOID, c1, c2);                
		double a1 = curve.getAzimuth();
		double a2 = curve.getReverseAzimuth();             
		double leftRadius = leftWidthMeters;                
                double rightRadius = rightWidthMeters;                
                
		GlobalCoordinates c = geoCalc.calculateEndingGlobalCoordinates(REFERENCE_ELLIPSOID, c1, a1 - 90, leftRadius);                
		moveTo(c.getLongitude(), c.getLatitude());
		c = geoCalc.calculateEndingGlobalCoordinates(REFERENCE_ELLIPSOID, c2, a2 + 90, leftRadius);                
		lineTo(c.getLongitude(), c.getLatitude());
		c = geoCalc.calculateEndingGlobalCoordinates(REFERENCE_ELLIPSOID, c2, a2 - 90, rightRadius);                
		lineTo(c.getLongitude(), c.getLatitude());
		c = geoCalc.calculateEndingGlobalCoordinates(REFERENCE_ELLIPSOID, c1, a1 + 90, rightRadius);                
		lineTo(c.getLongitude(), c.getLatitude());
		closePath();  
	}
}
