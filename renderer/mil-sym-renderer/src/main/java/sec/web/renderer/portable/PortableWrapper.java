package sec.web.renderer.portable;

import ArmyC2.C2SD.Utilities.SymbolUtilities;
import sec.web.renderer.SECRenderer;

public class PortableWrapper {	
	public static void main(String[] args) {
		
                
                /*System.out.println("arguments: ");
                for(int i = 0; i < args.length; i++)
                    System.out.println(args[i]);*/

                Boolean help = false;
                for(int j = 0; j < args.length; j++)
                {
                    if(args[j].equals("-?"))
                        help = true;
                }
                
                if(help)
                {
                    String message = "";
                    message += "Usage: java -jar jarfile -spport:#### -spbacklog:### -mpport:#### -mpbacklog:###";
                    message += "\nWhere options include:";
                    message += "\n\t-?\t\tprint this help message.";
                    message += "\n\t-spport\t\tdesired port for the single point service. (default 6789)";
                    message += "\n\t-spbacklog\tdesired backlog for the single point service.  (default 0, lets system decide)";
                    message += "\n\t-mpport\t\tdesired port for the multi point service. (default 6790)";
                    message += "\n\t-mpbacklog\tdesired backlog for the single point service.  (default 0, lets system decide)";
                    System.out.println(message);
                    return;
                }
                
                SECRenderer sr = SECRenderer.getInstance();
                sr.matchSECWebRendererAppletDefaultRendererSettings();
                sr.refreshPlugins();
                
                String spPort = null;
                String mpPort = null;
                String spBacklog = null;
                String mpBacklog = null;
                String[] parts = null;
                
                for(int i = 0; i < args.length; i++)
                {
                    if(args[i].startsWith("-spport"))
                    {
                        parts = args[i].split(":");
                        if(parts.length == 2)
                        {
                            spPort = parts[1];
                        }
                    }
                    if(args[i].startsWith("-mpport"))
                    {
                        parts = args[i].split(":");
                        if(parts.length == 2)
                        {
                            mpPort = parts[1];
                        }
                    }
                    
                    if(args[i].startsWith("-mpbacklog"))
                    {
                        parts = args[i].split(":");
                        if(parts.length == 2)
                        {
                            mpBacklog = parts[1];
                        }
                    }
                                        
                    if(args[i].startsWith("-spbacklog"))
                    {
                        parts = args[i].split(":");
                        if(parts.length == 2)
                        {
                            spBacklog = parts[1];
                        }
                    }
                    
                    parts = null;
                }    
                
                int sPort = 6789;
                int mPort = 6790;
                
                int sBacklog = 0;
                int mBacklog = 0;
                
                if(spPort != null && SymbolUtilities.isNumber(spPort))
                {
                    sPort = Integer.parseInt(spPort);
                }
                if(mpPort != null && SymbolUtilities.isNumber(mpPort))
                {
                    mPort = Integer.parseInt(mpPort);
                }
                
                if(spBacklog != null && SymbolUtilities.isNumber(spBacklog))
                {
                    sBacklog = Integer.parseInt(spBacklog);
                }
                if(mpBacklog != null && SymbolUtilities.isNumber(mpBacklog))
                {
                    mBacklog = Integer.parseInt(mpBacklog);
                }

                
		sr.startSinglePointServer(sPort, sBacklog);
                sr.startMultiPointServer(mPort, mBacklog);
                
		
		// System tray
		RendererSystemTray tray = new RendererSystemTray();
		tray.createSystemTray();
	}

}
