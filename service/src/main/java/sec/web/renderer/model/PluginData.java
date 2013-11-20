package sec.web.renderer.model;

import java.util.ArrayList;
import java.util.List;

public class PluginData {	

	private long id = 0;

	private List<String> plugins = new ArrayList<String>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<String> getPlugins() {
		return plugins;
	}

	public void setPlugins(List<String> plugins) {
		this.plugins = plugins;
	}
	
}
