package sec.geo.kml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.gavaghan.geodesy.GlobalPosition;

import sec.geo.GeoPoint;
import sec.geo.kml.KmlOptions.AltitudeMode;
import sec.geo.shape.Point;

public class KmlPolygon {
	//private static final String PREFIX = "<Polygon><altitudeMode>relativeToGround</altitudeMode><outerBoundaryIs><LinearRing><coordinates>";
	//private static final String SUFFIX = "</coordinates></LinearRing></outerBoundaryIs></Polygon>";
	private final List<Point> points;
	
	private AltitudeMode altitudeMode = AltitudeMode.ABSOLUTE;
	
	
	//protected final GeodeticCalculator geoCalc;
	protected static final Ellipsoid REFERENCE_ELLIPSOID = Ellipsoid.WGS84;
        private String altitudeModeField = "#ALTITUDEMODE#";
	
	private String PREFIX = "" +
			"				<Polygon>\n" +
			"					<tessellate>1</tessellate>\n" +
			"					<altitudeMode>"+altitudeModeField+"</altitudeMode>\n" +
			"					<outerBoundaryIs><LinearRing><coordinates>";
	private String SUFFIX = "" +
			"					</coordinates></LinearRing></outerBoundaryIs>\n" +
			"				</Polygon>\n";

	
	
	public KmlPolygon() {
		points = new ArrayList<Point>();
	}
	
	public KmlPolygon(List<Point> points, AltitudeMode altitudeMode) {
		this();
		this.points.addAll(points);
		this.altitudeMode = altitudeMode;
	}
	

	public void addPoint(Point point) {
		points.add(point);
	}
	
	public void addPoints(List<Point> points) {
		this.points.addAll(points);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
                
                
                
		sb.append(PREFIX);		
		sb.append(toCoordString());		
		sb.append(SUFFIX);
                
                int altitudeModeIndex = sb.indexOf(altitudeModeField);
                int altitudeModeLength = altitudeModeField.length();
                if(altitudeMode != null)
                        sb.replace(altitudeModeIndex, altitudeModeIndex + altitudeModeLength, altitudeMode.toString());
                
		return sb.toString();
	}

	public String toCoordString() {
		StringBuilder sb = new StringBuilder();
		
		List<Point> orderedPoints = getPointsCounterClockwise(); 
		if(orderedPoints == null)
			return "";
		
		for (Point point : orderedPoints) {
			sb.append(point.getLongitude());
			sb.append(",");
			sb.append(point.getLatitude());
			sb.append(",");
			sb.append(point.getAltitude());
			sb.append(" ");
		}
		
		// Close off the list of coordinates if necessary
		Point point = orderedPoints.get(0);
		if (!point.equals(orderedPoints.get(orderedPoints.size() - 1))) {
			sb.append(point.getLongitude());
			sb.append(",");
			sb.append(point.getLatitude());
			sb.append(",");
			sb.append(point.getAltitude());
			sb.append(" ");
		}
		
		return sb.toString();
	}

	
	public AltitudeMode getAltitudeMode() {
		return altitudeMode;
	}

	public void setAltitudeMode(AltitudeMode altitudeMode) {
		this.altitudeMode = altitudeMode;
	}
	
	public List<Point> getPointsClockwise() {
		if(points == null || points.size() < 3)
			return null;
		
		List<Point> result = points.subList(0, points.size()-1);
		int order = getPointOrder();
		if(order < 0) {			
			Collections.reverse(result);
			return result;
		}
		else return result;
	}
	
	public List<Point> getPointsCounterClockwise() {
		if(points == null || points.size() < 3)
			return null;
		
		List<Point> result = points.subList(0, points.size()-1);
		int order = getPointOrder();
		if(order > 0) {			
			Collections.reverse(result);
			return result;
		}
		else return result;
	}
	
	public int getPointOrder() {
		if(points==null || points.size()<3)
			return 0;
		
		int n = points.size();
		int j, k, count = 0;
		double z;
		for(int i=0; i<n; i++) {
			j = (i+1) % n;
			k = (i+2) % n;
			z = (points.get(j).getLongitude()-points.get(i).getLongitude())*(points.get(k).getLatitude()-points.get(i).getLatitude());
			z -=(points.get(j).getLatitude()-points.get(i).getLatitude())*(points.get(k).getLongitude()-points.get(i).getLongitude());
			if(z < 0)
				count--;
			else if (z > 0)
				count++;
		}
		if(count > 0)
			return -1;	//counterclockwise
		else if(count < 0)
			return 1;	//clockwise
		else return 0;	//invalid
	}
}
