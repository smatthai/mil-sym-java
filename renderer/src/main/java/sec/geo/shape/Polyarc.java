package sec.geo.shape;

import java.awt.Shape;

import sec.geo.GeoPath;
import sec.geo.GeoPoint;

public class Polyarc extends APath implements IArc {
	private GeoPoint pivot;
	private double radiusMeters;
	private double leftAzimuthDegrees, rightAzimuthDegrees;
	
	@Override
	public void setRadius(double radiusMeters) {
		this.radiusMeters = radiusMeters;
		shapeChanged();
	}
	
	@Override
	public void setPivot(GeoPoint pivot) {
		this.pivot = pivot;
		shapeChanged();
	}
	
	public void setRightAzimuthDegrees(double rightAzimuthDegrees) {
		this.rightAzimuthDegrees = rightAzimuthDegrees;
		shapeChanged();
	}
	
	public void setLeftAzimuthDegrees(double leftAzimuthDegrees) {
		this.leftAzimuthDegrees = leftAzimuthDegrees;
		shapeChanged();
	}
	
	@Override
	protected Shape createShape() {
		GeoPath shape = new GeoPath(maxDistanceMeters, flatnessDistanceMeters, limit);
		for (int i = 0; i < points.size(); i++) {
			GeoPoint point = points.get(i);
			if (i == 0) {
				shape.moveTo(point);
			} else {
				shape.lineTo(point);
			}
		}
		shape.arcTo(pivot, radiusMeters * 2, radiusMeters * 2, leftAzimuthDegrees, rightAzimuthDegrees);
		shape.closePath();
		return shape;
	}
}
