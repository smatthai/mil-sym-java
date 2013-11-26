package sec.web.renderer.model;

public class CommonURL {

	private String protocol;
	private String host;
	private String port;
	private String contextPath;

	public CommonURL() { }

	public CommonURL(String protocol, String host, String port, String contextPath) {
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.contextPath = contextPath;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();		

		if (this.protocol != null && this.protocol != "") {
			sb.append(this.protocol);
			sb.append("://");
		}

		if (this.host != null && this.host != "") {
			sb.append(this.host);
		}
		
		if (this.port != null && this.port != "") {
			sb.append(":");
			sb.append(this.port);
			sb.append("/");
		}

		if (this.contextPath != null && this.contextPath != "") {
			sb.append(this.contextPath);
		}

		return sb.toString();
	}
}
