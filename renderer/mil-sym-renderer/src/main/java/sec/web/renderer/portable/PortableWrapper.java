package sec.web.renderer.portable;

import sec.web.renderer.SECRenderer;

public class PortableWrapper {	
	public static void main(String[] args) {
		SECRenderer sr = SECRenderer.getInstance();
		sr.startSinglePointServer();
		
		// System tray
		RendererSystemTray tray = new RendererSystemTray();
		tray.createSystemTray();
	}

}
