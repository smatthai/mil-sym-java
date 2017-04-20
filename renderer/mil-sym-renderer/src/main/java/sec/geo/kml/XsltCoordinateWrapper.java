package sec.geo.kml;

import java.util.StringTokenizer;

import sec.geo.GeoPoint;
import sec.geo.shape.APath;
import sec.geo.shape.Circle;
import sec.geo.shape.Line;
import sec.geo.shape.Orbit;
import sec.geo.shape.Polyarc;
import sec.geo.shape.Polygon;
import sec.geo.shape.Radarc;
import sec.geo.shape.Route;
import sec.geo.shape.Ellipse;
import java.util.ArrayList;
public class XsltCoordinateWrapper {
	public static Line getLine(String[] points, KmlOptions.AltitudeMode altitudeMode, double minAltitude, double maxAltitude) {
		Line line = new Line();
		addPoints(points, line);

		line.setAltitudeMode(altitudeMode);
		line.setMinAltitude(minAltitude);
		line.setMaxAltitude(maxAltitude);
		
		return line;
	}
	
	public static Circle getCircle(double pivotX, double pivotY, KmlOptions.AltitudeMode altitudeMode, double radius, double minAltitude,
			double maxAltitude) {

		Circle circle = new Circle();
		circle.setAltitudeMode(altitudeMode);
		circle.setPivot(new GeoPoint(pivotX, pivotY));
		circle.setRadius(radius);
		circle.setMinAltitude(minAltitude);
		circle.setMaxAltitude(maxAltitude);

		return circle;
	}
	public static Ellipse getEllipse(double pivotX, double pivotY, KmlOptions.AltitudeMode altitudeMode, double semiMajor, double semiMinor, double minAltitude,
			double maxAltitude, double rotation) {

		Ellipse ellipse = new Ellipse(semiMajor,semiMinor,rotation);
		ellipse.setAltitudeMode(altitudeMode);
		ellipse.setPivot(new GeoPoint(pivotX, pivotY));
		ellipse.setMinAltitude(minAltitude);
		ellipse.setMaxAltitude(maxAltitude);
		return ellipse;
	}
	
	public static Orbit getOrbit(double point1X, double point1Y, double point2X, double point2Y, KmlOptions.AltitudeMode altitudeMode, double width,
			double minAltitude, double maxAltitude) {
		
		Orbit orbit = new Orbit();
		orbit.addPoint(new GeoPoint(point1X, point1Y));
		orbit.addPoint(new GeoPoint(point2X, point2Y));
		orbit.setAltitudeMode(altitudeMode);
		orbit.setWidth(width);
		orbit.setMinAltitude(minAltitude);
		orbit.setMaxAltitude(maxAltitude);
		
		return orbit;
	}
	
	public static Route getRoute(String[] points, KmlOptions.AltitudeMode altitudeMode, double leftWidth, double rightWidth, double minAltitude, double maxAltitude) {
		
		Route route = new Route();
		addPoints(points, route);
		route.setAltitudeMode(altitudeMode);
		route.setLeftWidth(leftWidth);
                route.setRightWidth(rightWidth);
		route.setMinAltitude(minAltitude);
		route.setMaxAltitude(maxAltitude);

		return route;
	}
	
	public static Polygon getPolygon(String[] points, KmlOptions.AltitudeMode altitudeMode, double minAltitude, double maxAltitude) {
		Polygon polygon = new Polygon();
		addPoints(points, polygon);
		polygon.setAltitudeMode(altitudeMode);
		polygon.setMinAltitude(minAltitude);
		polygon.setMaxAltitude(maxAltitude);
		
		return polygon;
	}
	
	public static Radarc getRadarc(double pivotX, double pivotY, KmlOptions.AltitudeMode altitudeMode, double innerRadius, double outerRadius,
			double leftAzimuth, double rightAzimuth, double minAltitude, double maxAltitude) {
		Radarc radarc = new Radarc();
		radarc.setAltitudeMode(altitudeMode);
		radarc.setPivot(new GeoPoint(pivotX, pivotY));
		radarc.setMinRadius(innerRadius);
		radarc.setRadius(outerRadius);
		radarc.setLeftAzimuthDegrees(leftAzimuth);
		radarc.setRightAzimuthDegrees(rightAzimuth);
		radarc.setMinAltitude(minAltitude);
		radarc.setMaxAltitude(maxAltitude);
		
		return radarc;
	}
	
	public static Polyarc getPolyarc(String[] points, double pivotX, double pivotY, KmlOptions.AltitudeMode altitudeMode, double radius,
			double leftAzimuth, double rightAzimuth, double minAltitude, double maxAltitude) {
		Polyarc polyarc = new Polyarc();
		addPoints(points, polyarc);
		polyarc.setAltitudeMode(altitudeMode);
		polyarc.setPivot(new GeoPoint(pivotX, pivotY));
		polyarc.setRadius(radius);
		polyarc.setLeftAzimuthDegrees(leftAzimuth);
		polyarc.setRightAzimuthDegrees(rightAzimuth);
		polyarc.setMinAltitude(minAltitude);
		polyarc.setMaxAltitude(maxAltitude);
		
		return polyarc;
	}
	
	//==========================================================================================
	//==========================================================================================
	//==========================================================================================
	
	public static String getLineKml(String[] points, String id, String name, String description, String lineColor, String fillColor, KmlOptions.AltitudeMode altitudeMode, double minAltitude, double maxAltitude) {
		KmlRenderer renderer = new KmlRenderer();		
		return renderer.getKml(getLine(points, altitudeMode, minAltitude, maxAltitude), id, name, description, lineColor, fillColor);
	
	}
	
	public static String getCircleKml(double pivotX, double pivotY, String id, String name, String description, String lineColor, String fillColor, KmlOptions.AltitudeMode altitudeMode, double radius, double minAltitude,
			double maxAltitude) {
		KmlRenderer renderer = new KmlRenderer();
		return renderer.getKml(getCircle(pivotX, pivotY, altitudeMode, radius, minAltitude, maxAltitude), id, name, description, lineColor, fillColor);
	}
        
        public static ArrayList<GeoPoint> getEllipsePoints(double pivotX, double pivotY, KmlOptions.AltitudeMode altitudeMode, double semiMajor, double semiMinor, double minAltitude,
			double maxAltitude, double rotation)
        {
                Ellipse ellipse=getEllipse(pivotX, pivotY, altitudeMode, semiMajor, semiMinor, minAltitude, maxAltitude, rotation); 
                ArrayList<GeoPoint> pts=ellipse.getEllipsePoints();
                return pts;
        }
	
	public static String getOrbitKml(double point1X, double point1Y, double point2X, double point2Y, String id, String name, String description, String lineColor, String fillColor, KmlOptions.AltitudeMode altitudeMode, double width,
			double minAltitude, double maxAltitude) {
		KmlRenderer renderer = new KmlRenderer();
		return renderer.getKml(getOrbit(point1X, point1Y, point2X, point2Y, altitudeMode, width, minAltitude, maxAltitude), id, name, description, lineColor, fillColor);
	}
	
	public static String getRouteKml(String[] points, String id, String name, String description, String lineColor, String fillColor, KmlOptions.AltitudeMode altitudeMode, double leftWidth, double rightWidth, double minAltitude, double maxAltitude) {
		KmlRenderer renderer = new KmlRenderer();		
		return renderer.getKml(getRoute(points, altitudeMode, leftWidth, rightWidth, minAltitude, maxAltitude), id, name, description, lineColor, fillColor);				
	}
	
	public static String getPolygonKml(String[] points, String id, String name, String description, String lineColor, String fillColor, KmlOptions.AltitudeMode altitudeMode, double minAltitude, double maxAltitude) {
		KmlRenderer renderer = new KmlRenderer();
		return renderer.getKml(getPolygon(points, altitudeMode, minAltitude, maxAltitude), id, name, description, lineColor, fillColor);
	}
	
	public static String getRadarcKml(double pivotX, double pivotY, String id, String name, String description, String lineColor, String fillColor, KmlOptions.AltitudeMode altitudeMode, double innerRadius, double outerRadius,
			double leftAzimuth, double rightAzimuth, double minAltitude, double maxAltitude) {
		KmlRenderer renderer = new KmlRenderer();
		return renderer.getKml(getRadarc(pivotX, pivotY, altitudeMode, innerRadius, outerRadius, leftAzimuth, rightAzimuth, minAltitude, maxAltitude), id, name, description, lineColor, fillColor);
	}
	
	public static String getPolyarcKml(String[] points, double pivotX, double pivotY, String id, String name, String description, String lineColor, String fillColor, KmlOptions.AltitudeMode altitudeMode, double radius,
			double leftAzimuth, double rightAzimuth, double minAltitude, double maxAltitude) {
		KmlRenderer renderer = new KmlRenderer();
		return renderer.getKml(getPolyarc(points, pivotX, pivotY, altitudeMode, radius, leftAzimuth, rightAzimuth, minAltitude, maxAltitude), id, name, description, lineColor, fillColor);
	}
	
	
	//==========================================================================================
	//==========================================================================================
	//==========================================================================================
	
	
	public static String[] plotLine(String[] points, KmlOptions.AltitudeMode altitudeMode, double minAltitude, double maxAltitude) {
		KmlRenderer renderer = new KmlRenderer();
		return renderer.getCoords(getLine(points, altitudeMode, minAltitude, maxAltitude));
	}
	
	public static String[] plotCircle(double pivotX, double pivotY, KmlOptions.AltitudeMode altitudeMode, double radius, double minAltitude,
			double maxAltitude) {
		KmlRenderer renderer = new KmlRenderer();
		return renderer.getCoords(getCircle(pivotX, pivotY, altitudeMode, radius, minAltitude, maxAltitude));
	}
	
	public static String[] plotOrbit(double point1X, double point1Y, double point2X, double point2Y, KmlOptions.AltitudeMode altitudeMode, double width,
			double minAltitude, double maxAltitude) {
		KmlRenderer renderer = new KmlRenderer();
		return renderer.getCoords(getOrbit(point1X, point1Y, point2X, point2Y, altitudeMode, width, minAltitude, maxAltitude));
	}
	
	public static String[] plotRoute(String[] points, KmlOptions.AltitudeMode altitudeMode, double leftWidth, double rightWidth, double minAltitude, double maxAltitude) {
		KmlRenderer renderer = new KmlRenderer();
		return renderer.getCoords(getRoute(points, altitudeMode, leftWidth, rightWidth, minAltitude, maxAltitude));
	}
	
	public static String[] plotPolygon(String[] points, KmlOptions.AltitudeMode altitudeMode, double minAltitude, double maxAltitude) {
		KmlRenderer renderer = new KmlRenderer();
		return renderer.getCoords(getPolygon(points, altitudeMode, minAltitude, maxAltitude));
	}
	
	public static String[] plotRadarc(double pivotX, double pivotY, KmlOptions.AltitudeMode altitudeMode, double innerRadius, double outerRadius,
			double leftAzimuth, double rightAzimuth, double minAltitude, double maxAltitude) {
		KmlRenderer renderer = new KmlRenderer();
		return renderer.getCoords(getRadarc(pivotX, pivotY, altitudeMode, innerRadius, outerRadius, leftAzimuth, rightAzimuth, minAltitude, maxAltitude));
	}
	
	public static String[] plotPolyarc(String[] points, double pivotX, double pivotY, KmlOptions.AltitudeMode altitudeMode, double radius,
			double leftAzimuth, double rightAzimuth, double minAltitude, double maxAltitude) {
		KmlRenderer renderer = new KmlRenderer();
		return renderer.getCoords(getPolyarc(points, pivotX, pivotY, altitudeMode, radius, leftAzimuth, rightAzimuth, minAltitude, maxAltitude));
	}
		
	private static void addPoints(String[] points, APath path) {
		for (String sPoint : points) {
			StringTokenizer tokenizer = new StringTokenizer(sPoint, ",");
			double longitude = Double.parseDouble(tokenizer.nextToken());
			double latitude = Double.parseDouble(tokenizer.nextToken());
			path.addPoint(new GeoPoint(longitude, latitude));
		}
	}
}
