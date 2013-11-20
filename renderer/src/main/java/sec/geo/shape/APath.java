package sec.geo.shape;

import java.util.ArrayList;
import java.util.List;

import sec.geo.GeoPoint;

public abstract class APath extends AExtrusion {
	protected final List<GeoPoint> points;
	
	public APath() {
		points = new ArrayList<GeoPoint>();
	}
	
	public void addPoint(GeoPoint point) {
		points.add(point);
		shapeChanged();
	}
	
	public void addPoints(List<GeoPoint> points) {
		this.points.addAll(points);
		shapeChanged();
	}
}
