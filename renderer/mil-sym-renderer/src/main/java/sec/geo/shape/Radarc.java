package sec.geo.shape;

import java.awt.Shape;
import java.awt.geom.Area;

import sec.geo.GeoArc;
import sec.geo.GeoEllipse;

public class Radarc extends AArc {
	private double minRadiusMeters;
	
	public void setMinRadius(double minRadiusMeters) {
		this.minRadiusMeters = minRadiusMeters;
		shapeChanged();
	}
	
	@Override
	protected Shape createShape() {
		GeoArc arc = new GeoArc(pivot, radiusMeters * 2, radiusMeters * 2, leftAzimuthDegrees, rightAzimuthDegrees,
				maxDistanceMeters, flatnessDistanceMeters, limit);
		Area shape = new Area(arc);
		GeoEllipse ellipse = new GeoEllipse(pivot, minRadiusMeters * 2, minRadiusMeters * 2, maxDistanceMeters,
				flatnessDistanceMeters, limit);
		shape.subtract(new Area(ellipse));
		return shape;
	}
}
