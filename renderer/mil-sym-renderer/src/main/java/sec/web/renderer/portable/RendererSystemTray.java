package sec.web.renderer.portable;

import ArmyC2.C2SD.Utilities.RendererSettings;
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
			String toolTip = "SEC MilStd 2525 Rendering Service";
			final PopupMenu popup    = new PopupMenu();
			final TrayIcon trayIcon  = new TrayIcon(createImage("images/globe.png", toolTip),toolTip);
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
                        trayIcon.displayMessage("caption", "text", TrayIcon.MessageType.ERROR);
			//trayIcon.setToolTip(toolTip);
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
                                    String std = getSymbologyStandardString();
                                    
                                    message += "\nSymbology Standard set to: " + std;
                                    
                                    if(sr.isSinglePointServerRunning())
                                        message += "\nSingle Point Service is running on 127.0.0.1:"  + String.valueOf(sr.getSinglePointServerPort());
                                    if(sr.isMultiPointServerRunning())
                                        message += "\nMulti Point Service is running on 127.0.0.1:"  + String.valueOf(sr.getMultiPointServerPort());
                                    
                                    JOptionPane.showMessageDialog(null, "This Service is capable of rendering milstd 2525chB & 2525C graphics with USAS additions" + message,"About",JOptionPane.PLAIN_MESSAGE);
				}
			});
			
			exitItem.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					sysTray.remove(trayIcon);
					SECRenderer.getInstance().stopSinglePointServer();
                                        SECRenderer.getInstance().stopMultiPointServer();
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
        
        private String getSymbologyStandardString()
        {
            String std = "2525B";
            int symstd = RendererSettings.getInstance().getSymbologyStandard();
            switch(symstd)
            {
                case RendererSettings.Symbology_2525B:
                    std = "2525B";
                    break;
                case RendererSettings.Symbology_2525C:
                    std = "2525C";
                    break;
                case 2://RendererSettings.Symbology_2525D:
                    std = "2525D";
                    break;//*/
            }
                    
            return std;
        }
	


}
