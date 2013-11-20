package sec.geo.shape;

import java.util.HashSet;
import java.util.Set;

public class AComposite {
	protected Set<AExtrusion> elements;
	
	public AComposite() {
		elements = new HashSet<AExtrusion>();
	}
	
	public Set<AExtrusion> getElements() {
		return elements;
	}
}
