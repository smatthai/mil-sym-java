package sec.web.renderer.portable;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import sec.web.renderer.SECRenderer;

public class RendererSystemTray {
	
	public RendererSystemTray() {
		
	}

	public void createSystemTray() {
		try {
			isSysTraySupported();
			
			final PopupMenu popup    = new PopupMenu();
			final TrayIcon trayIcon  = new TrayIcon(createImage("images/globe.png", "SECRenderer tray icon"));
			final SystemTray sysTray = SystemTray.getSystemTray();
			
			
			// create interaction						
			Menu displayMenu = new Menu("Display");
			MenuItem aboutItem = new MenuItem("About");			
			MenuItem exitItem = new MenuItem("Exit");
			
			// Add items to popup
			popup.add(aboutItem);
			popup.addSeparator();
			popup.addSeparator();
			//popup.add(displayMenu);
			popup.add(exitItem);
			
			trayIcon.setPopupMenu(popup);
			
			sysTray.add(trayIcon);				
			
			
			trayIcon.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(null, "SEC's Portable Renderer");
				}
			});
			
			aboutItem.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
                                    SECRenderer sr = SECRenderer.getInstance();
                                    
                                    
                                    String message = "";
                                    if(sr.isSinglePointServerRunning())
                                        message += "\nSingle Point Service is running on 127.0.0.1:"  + String.valueOf(sr.getSinglePointServerPort());
                                    if(sr.isMultiPointServerRunning())
                                        message += "\nMulti Point Service is running on 127.0.0.1:"  + String.valueOf(sr.getMultiPointServerPort());
                                    
                                    JOptionPane.showMessageDialog(null, "This application is capable of Rendering milstd 2525 change b graphics" + message);
				}
			});
			
			exitItem.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					sysTray.remove(trayIcon);
					SECRenderer.getInstance().stopSinglePointServer();
					System.exit(0);
				}
			});
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void isSysTraySupported() throws Exception {
		if (!SystemTray.isSupported()) {
			throw new Exception("System Tray is not supported");
		}
	}
	
	private Image createImage(String path, String descr) throws IOException {
		URL imageURL = RendererSystemTray.class.getResource(path);
		imageURL = RendererSystemTray.class.getClassLoader().getResource(path);
		
		if (imageURL == null) {
			throw new IOException("Resource not found:\t" + path);			
		}
		
		return (new ImageIcon(imageURL, descr)).getImage();
	}
	


}
