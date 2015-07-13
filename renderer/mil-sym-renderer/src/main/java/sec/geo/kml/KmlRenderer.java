package sec.geo.kml;

import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sec.geo.shape.AComposite;
import sec.geo.shape.AExtrusion;
import sec.geo.shape.Point;

public class KmlRenderer {
	private static final String EXCEPTION = "EXCEPTION:";
	
//	private String lineColorNormal = "00000000";
//	private String polyColorNormal = "7fff8080";
//	private String lineColorHighlight = "ff003fff";
//	private String polyColorHighlight = "af00ff00";
	private String colorDefault = "ff003fff";
	private String descriptionField = "#DESCRIPTION#";
	private String extendedData = "#EXTENDEDDATA#";
	private String colorField = "#COLOR#";
	private String nameField = "#NAME#";
	private String idField = "#ID#";
        private String placemarkidField = "#PLACEMARKID#";
	
	private String KML_START = "<Folder id=\"" + idField + "\">\n";
//			"<?xml version='1.0' encoding='utf-8'?>\n" +
//			"<kml xmlns='http://www.opengis.net/kml/2.2'>\n" +
//			"<Document>\n" +
//			"		<Style id='normal'>\n" +
//			"			<LabelStyle><scale>0</scale></LabelStyle>\n" +
//			"			<LineStyle><color>"+lineColorNormal+"</color></LineStyle>\n" +
//			"			<PolyStyle><color>"+polyColorNormal+"</color></PolyStyle>\n" +
//			"			<BalloonStyle><text>#BALLOONSTYLE#</text></BalloonStyle>\n" +
//			"		</Style>\n" +
//			"		<Style id='highlight'>\n" +
//			"			<LineStyle><color>"+lineColorHighlight+"</color><width>2</width></LineStyle>\n" +
//			"			<PolyStyle><color>"+polyColorHighlight+"</color></PolyStyle>\n" +
//			"			<BalloonStyle><text>#BALLOONSTYLE#</text></BalloonStyle>\n" +
//			"		</Style>\n" +
//			"		<StyleMap id='rollover'>\n" +
//			"			<Pair><key>normal</key><styleUrl>#normal</styleUrl></Pair>\n" +
//			"			<Pair><key>highlight</key><styleUrl>#highlight</styleUrl></Pair>\n" +
//			"		</StyleMap>\n";
	
//	private String KML_END = "</Document>\n</kml>\n";
    private String KML_END = "</Folder>\n";
	
	private String PLACEMARK_START = "" +
			"		<Placemark id=\"" + placemarkidField + "\">\n" +
                        "                      <Style>\n" +
                
                        "                          <PolyStyle>\n" +
                        "                              <color>"+ colorField + "</color>\n" +        
                        "                          </PolyStyle>\n" +
                        "                          <LineStyle>\n" +
                        "                              <color>" + colorField + "</color>\n" +
                        "                          </LineStyle>\n" +
                        "                       </Style>\n" +        
			"			<name>" + nameField + "</name>\n" +
			"			<description>"+descriptionField+"</description>\n" +
			"			<ExtendedData>"+extendedData+"</ExtendedData>\n" +
//			"			<styleUrl>#rollover</styleUrl>\n" +
			"			<MultiGeometry>\n";
	
//	private String polyStartStr = "" +
//			"				<Polygon>" +
//			"					<tessellate>1</tessellate>\n" +
//			"					<altitudeMode>"+altitudeMode+"</altitudeMode>\n" +
//			"					<outerBoundaryIs><LinearRing><coordinates>\n";
//	private String polyEndStr = "" +
//			"					</coordinates></LinearRing></outerBoundaryIs>\n" +
//			"				</Polygon>\n";
	
	private String PLACEMARK_END = "" +
			"			</MultiGeometry>\n" +
			"		</Placemark>\n";
	
	
	private String DEFAULT_EXDAT = "<Data name='sid'><value>#ID#</value></Data><Data name='shapeType'><value>#SHAPETYPE#</value></Data><Data name='lat'><value>#LAT#</value></Data><Data name='lon'><value>#LON#</value></Data><Data name='alt'><value>#ALT#</value></Data>";
	private String DEFAULT_BLSTY = "<!" + "[CDAT" + "A[" + "$[sid]" +"]]"+">";

	
	//private Set<KmlPolygon> polys = null;
	
	public Set<KmlPolygon> renderPolygons(AExtrusion ext) {
		Set<KmlPolygon> polys = new HashSet<KmlPolygon>();
		
		ext.setMaxDistance(200000);
		//ext.setFlatness(1);
		//ext.setLimit(3);
		ext.setFlatness(2);
		ext.setLimit(8);
		
		// Render perimeter polys
		List<Point> perimeterPoints = new ArrayList<Point>();
		PathIterator it = ext.getShape().getPathIterator(null);
		Point pre = null;                
                
		while (!it.isDone()) {
                        
                    double[] strokePoints = new double[6];                            
                    int type = it.currentSegment(strokePoints);

                    double longitudeDegrees = strokePoints[0];
                    double latitudeDegrees = strokePoints[1];
                    switch (type) {
                            case PathIterator.SEG_MOVETO:
                            case PathIterator.SEG_LINETO:
                                    if (pre != null) {
                                            List<Point> ps = new ArrayList<Point>();
                                            ps.add(new Point(pre.getLongitude(), pre.getLatitude(), ext.getMinAltitude()));
                                            ps.add(new Point(pre.getLongitude(), pre.getLatitude(), ext.getMaxAltitude()));
                                            ps.add(new Point(longitudeDegrees, latitudeDegrees, ext.getMaxAltitude()));
                                            ps.add(new Point(longitudeDegrees, latitudeDegrees, ext.getMinAltitude()));
                                            ps.add(new Point(pre.getLongitude(), pre.getLatitude(), ext.getMinAltitude()));
                                            polys.add(new KmlPolygon(ps, ext.getAltitudeMode()));
                                    }
                                    pre = new Point(longitudeDegrees, latitudeDegrees);
                                    perimeterPoints.add(pre);
                    }
                    it.next();
		}
		                
		// Render top and bottom poly if the perimeter is complete
		if (perimeterPoints.size() > 0) {
                    // In some weird cases, for routes, when it builds an area, it will drop the closing
                    // point in the shape.   Route uses an area.  This causes this condition to not execute.
                    // adding the first point to the perimeterPoints fixes the issue, not sure if it causes any
                    // side effects.
                    if (perimeterPoints.get(0).equals(perimeterPoints.get(perimeterPoints.size() - 1))) {

                        polys.add(new KmlPolygon(transformPoints(perimeterPoints, ext.getMinAltitude()), ext.getAltitudeMode()));
                        polys.add(new KmlPolygon(transformPoints(perimeterPoints, ext.getMaxAltitude()), ext.getAltitudeMode()));
                    } else {
                        perimeterPoints.add(perimeterPoints.get(0));

                        polys.add(new KmlPolygon(transformPoints(perimeterPoints, ext.getMinAltitude()), ext.getAltitudeMode()));
                        polys.add(new KmlPolygon(transformPoints(perimeterPoints, ext.getMaxAltitude()), ext.getAltitudeMode()));
                    }
		}
                
		
		return polys;
	}

        public String getPlacemarkKml(AExtrusion ext, String id, String name, String description, String lineColor, String fillColor) {
            
            try {
                StringBuilder sb = new StringBuilder();
                Set<KmlPolygon> polys = renderPolygons(ext);
                sb.append(PLACEMARK_START);
                int placemarkIdIndex = sb.indexOf(placemarkidField);
                int placemarkIdLength = placemarkidField.length();
                if (id != null)
                {
                    sb.replace(placemarkIdIndex, placemarkIdIndex + placemarkIdLength, id + "_mg");
                }

                int descriptionIndex = sb.indexOf(descriptionField);
                int descriptionLength = descriptionField.length();
                if(description != null)
                        sb.replace(descriptionIndex, descriptionIndex + descriptionLength, "<![CDATA["  + description + "]]>");
                int colorIndex = sb.indexOf(colorField);
                int colorLength = colorField.length();                
                if(fillColor != null) {
                        sb.replace(colorIndex, colorIndex + colorLength, fillColor);
                }
                else sb.replace(colorIndex, colorIndex + colorLength, colorDefault);

                int lineColorIndex = sb.indexOf(colorField, colorIndex + colorLength);
                if(lineColor != null) {			
                        sb.replace(lineColorIndex, lineColorIndex + colorLength, lineColor);
                }
                else sb.replace(lineColorIndex, lineColorIndex + colorLength, colorDefault);

                int nameIndex = sb.indexOf(nameField);
                int nameLength = nameField.length();
                if(name != null)			
                        sb.replace(nameIndex, nameIndex + nameLength, "<![CDATA["  + name + "]]>");

                for(KmlPolygon poly : polys) {			

                        sb.append(poly.toString());			
                }

                sb.append(PLACEMARK_END);
                return sb.toString();
            }
            catch(Exception e) {
                    e.printStackTrace();
                    return EXCEPTION+e.getMessage();
            }

            
            
        }
	public String getKml(AExtrusion ext, String id, String name, String description, String lineColor, String fillColor) {
		try {
			//System.out.println("Get KML");
			
	
			StringBuilder sb = new StringBuilder();
	                        
			sb.append(KML_START);
			int idIndex = sb.indexOf(idField);
			int idLength = idField.length();
			sb.replace(idIndex, idIndex + idLength, id);
	
			//System.out.println("KML START - " + sb.toString());
                        
                        sb.append(getPlacemarkKml(ext, id, name, description, lineColor, fillColor));
				
			sb.append(KML_END);
	
			//System.out.println(sb.toString());
			return sb.toString();
		}
		catch(Exception e) {
			e.printStackTrace();
			return EXCEPTION+e.getMessage();
		}
	}
	
	public String getKml(AComposite com, String id, String name, String description, String lineColor, String fillColor) {
		StringBuilder sb = new StringBuilder();
		//Set<KmlPolygon> polys;
		sb.append(KML_START);
                int idIndex = sb.indexOf(idField);
                int idLength = idField.length();
                sb.replace(idIndex, idIndex + idLength, id);
                
		for(AExtrusion ext : com.getElements()) {
			String extStr = getPlacemarkKml(ext, id, name, description, lineColor, fillColor);
			
			if(!extStr.startsWith(EXCEPTION))
				sb.append(extStr);
		}
		
		sb.append(KML_END);
		//System.out.println(sb.toString());
		return sb.toString();
	}
	
	
	public String[] getCoords(AExtrusion ext) {
		Set<KmlPolygon> polys = renderPolygons(ext);
		
		// Iterate through the polygons and produce an array of KML coordinates
		String[] coords = new String[polys.size()];
		int i = 0;
		for (KmlPolygon poly : polys) {
			coords[i] = poly.toCoordString();
			i++;
		}
		return coords;
	}
	
	private List<Point> transformPoints(List<Point> points, double altitudeMeters) {
		List<Point> returnPoints = new ArrayList<Point>();
		for (Point p : points) {
			returnPoints.add(new Point(p.getLongitude(), p.getLatitude(), altitudeMeters));
		}
		return returnPoints;
	}
}
