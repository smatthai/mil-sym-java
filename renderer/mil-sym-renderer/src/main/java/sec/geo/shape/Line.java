package sec.geo.shape;

import java.awt.Shape;

import sec.geo.GeoPath;

public class Line extends APath {
	@Override
	protected Shape createShape() {
		GeoPath path = new GeoPath(maxDistanceMeters, flatnessDistanceMeters, limit);
		for (int i = 0; i < points.size(); i++) {
			if (i > 0) {
				path.lineTo(points.get(i));
			} else {
				path.moveTo(points.get(i));
			}
		}
		return path;
	}
}
