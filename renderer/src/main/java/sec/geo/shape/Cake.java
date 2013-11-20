package sec.geo.shape;

import sec.geo.GeoPoint;

public class Cake extends AComposite implements IPivot {
	private GeoPoint pivot;
	
	public Cake() {
		super();
		pivot = new GeoPoint();
	}
	
	public void addLayer(AExtrusion layer) {
		if (layer instanceof IPivot) {
			((IPivot) layer).setPivot(pivot);
			elements.add(layer);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public void setPivot(GeoPoint pivot) {
		this.pivot = pivot;
		for (AExtrusion layer : elements) {
			((IPivot) layer).setPivot(pivot);
			layer.shapeChanged();
		}
	}
}
