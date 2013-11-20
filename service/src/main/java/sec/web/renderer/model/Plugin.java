package sec.web.renderer.model;

import java.io.File;

public class Plugin extends CommonURL {
	private String pluginName;
	private String pluginPath;
	

	public Plugin() { 
		super();
	}
	
	public Plugin(String name, String path) {
		this.pluginName = name;
		this.pluginPath = path;
	}

	public String getName() {
		return pluginName;
	}

	public void setName(String name) {
		this.pluginName = name;
	}

	public String getPath() {
		return pluginPath;
	}

	public void setPath(String path) {
		this.pluginPath = path;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append(this.pluginName);
		if (this.pluginPath != "" && this.pluginPath != null) {
			sb.append(File.separator);
			sb.append(this.pluginPath);
		}
		
		
		return sb.toString();
	}
	
}
