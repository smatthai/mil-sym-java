package sec.geo.shape;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;

import sec.geo.GeoBlock;
import sec.geo.GeoBlock2;
import sec.geo.GeoEllipse;
import sec.geo.GeoPoint;

public class Route extends APath {
	private double leftWidthMeters;
        private double rightWidthMeters;
	
        
	public void setLeftWidth(double widthMeters) {
            this.leftWidthMeters = widthMeters;
            shapeChanged();
	}
        
        public void setRightWidth(double widthMeters) {
            this.rightWidthMeters = widthMeters;
            shapeChanged();
        }
	
	@Override
	protected Shape createShape() {
		Area route = new Area();
		GeoPoint previousPoint = null;                
		for (int i = 0; i < points.size(); i++) {
                        
			GeoPoint point = points.get(i);
                        
                        /*
			if (i > 0 && i < points.size() - 1) {                                
				GeoEllipse ellipse = new GeoEllipse(point, widthMeters, widthMeters, maxDistanceMeters,
						flatnessDistanceMeters, limit);
				route.add(new Area(ellipse));
			}
                        * */
			if (previousPoint != null) {
                                
				// Skip if points are the same -- doesn't take into account height difference
				if(previousPoint.equals(point))
					continue;
				
				// Draw rectangle connection
				GeoBlock2 block = new GeoBlock2(previousPoint, point, this.leftWidthMeters, this.rightWidthMeters, maxDistanceMeters,
						flatnessDistanceMeters, limit);
                                Area area = new Area(block);
				route.add(area);
                                
                         }
                         previousPoint = point;
                }                               
                return route;
	}
}
