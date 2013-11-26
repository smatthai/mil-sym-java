package sec.geo.shape;

import java.awt.Shape;

import sec.geo.kml.KmlOptions.AltitudeMode;

public abstract class AExtrusion {
	private double minAltitudeMeters;
	private double maxAltitudeMeters;
	private Shape shape;
	protected double maxDistanceMeters;
	protected double flatnessDistanceMeters;
	
	protected AltitudeMode altitudeMode;
	protected int limit;
	
	public AExtrusion() {
		maxDistanceMeters = 100000;
		flatnessDistanceMeters = 1;
		limit = 4;
	}
	
	public Shape getShape() {
		if (shape == null) {
			shape = createShape();
		}
		return shape;
	}
	
	protected void shapeChanged() {
		shape = null;
	}
	
	protected abstract Shape createShape();
	
	public double getMinAltitude() {
		return minAltitudeMeters;
	}
	
	public void setMinAltitude(double minAltitudeMeters) {
		this.minAltitudeMeters = minAltitudeMeters;
		shapeChanged();
	}
	
	public double getMaxAltitude() {
		return maxAltitudeMeters;
	}
	
	public void setMaxAltitude(double maxAltitudeMeters) {
		this.maxAltitudeMeters = maxAltitudeMeters;
		shapeChanged();
	}
	
	public void setMaxDistance(double maxDistanceMeters) {
		this.maxDistanceMeters = maxDistanceMeters;
		shapeChanged();
	}
	
	public void setFlatness(double flatnessDistanceMeters) {
		this.flatnessDistanceMeters = flatnessDistanceMeters;
		shapeChanged();
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
		shapeChanged();
	}

	public AltitudeMode getAltitudeMode() {
		return altitudeMode;
	}

	public void setAltitudeMode(AltitudeMode altitudeMode) {
		this.altitudeMode = altitudeMode;
	}
}
