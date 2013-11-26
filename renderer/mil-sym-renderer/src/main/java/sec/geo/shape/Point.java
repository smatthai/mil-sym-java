package sec.geo.shape;

import org.gavaghan.geodesy.GlobalPosition;

public class Point {
	private final double longitudeDegrees;
	private final double latitudeDegrees;
	private final double altitudeMeters;
	
	public Point(double longitudeDegrees, double latitudeDegrees) {
		this(longitudeDegrees, latitudeDegrees, 0);
	}
	
	public Point(double longitudeDegrees, double latitudeDegrees, double altitudeMeters) {
		this.longitudeDegrees = longitudeDegrees;
		this.latitudeDegrees = latitudeDegrees;
		this.altitudeMeters = altitudeMeters;
	}
	
	public double getLongitude() {
		return longitudeDegrees;
	}
	
	public double getLatitude() {
		return latitudeDegrees;
	}
	
	public double getAltitude() {
		return altitudeMeters;
	}
	
	public GlobalPosition toGlobalPos() {
		return new GlobalPosition(getLatitude(), getLongitude(), getAltitude());
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Point)) {
			return false;
		}
		Point other = (Point) o;
		return (longitudeDegrees == other.longitudeDegrees) && (latitudeDegrees == other.latitudeDegrees)
				&& (altitudeMeters == other.altitudeMeters);
	}
        
        @Override
        public String toString() {
            return "[" + longitudeDegrees + "," + latitudeDegrees + "," + altitudeMeters + "]";
        }
}
