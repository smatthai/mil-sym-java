package sec.web.renderer.portable;

import ArmyC2.C2SD.Utilities.RendererSettings;
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
                    message += "Usage: java -cp [location of jars] sec.web.renderer.portable.PortableWrapper -spport:#### -spbacklog:### -mpport:#### -mpbacklog:###";
                    message += "\nWhere options include:";
                    message += "\n\t-?\t\tprint this help message.";
                    message += "\n\t-symstd\t\tspecifies which symbology standard to assume. (default \"2525B\")  Set with \"2525B\" or \"2525C\"";
                    message += "\n\t-spon\t\tstarts the single point service. (default true)";
                    message += "\n\t-spport\t\tdesired port for the single point service. (default 6789)";
                    message += "\n\t-spbacklog\tdesired backlog for the single point service.  (default 0, lets system decide)";
                    message += "\n\t-mpon\t\tstarts the multi point service. (default true)";
                    message += "\n\t-mpport\t\tdesired port for the multi point service. (default 6790)";
                    message += "\n\t-mpbacklog\tdesired backlog for the single point service.  (default 0, lets system decide)";
                    message += "\n\t-acmodifiers\tfire support areas will only show identifying label and labels that fit the area.\n";
                    message += "(default true, set false and all labels are shown all the time)\n";
                    message += "\n\t-ocmtype\tSpecifies if Operational Condition modifier will display as slahses(0) or bars(1,default).";
                    System.out.println(message);
                    return;
                }
                
                SECRenderer sr = SECRenderer.getInstance();
                //sr.matchSECWebRendererAppletDefaultRendererSettings();
                sr.refreshPlugins();
                
                Boolean spOn = true;
                Boolean mpOn = true;
                Boolean acModifiers = true;
                String ocmType = null;
                String spPort = null;
                String mpPort = null;
                String spBacklog = null;
                String mpBacklog = null;
                String[] parts = null;
                int symStd = -1;
                
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
                    
                    if(args[i].startsWith("-spon"))
                    {
                        parts = args[i].split(":");
                        if(parts.length == 2)
                        {
                            spOn = Boolean.parseBoolean(parts[1]);
                        }
                    }
                    
                    if(args[i].startsWith("-mpon"))
                    {
                        parts = args[i].split(":");
                        if(parts.length == 2)
                        {
                            mpOn = Boolean.parseBoolean(parts[1]);
                        }
                    }
                    
                    if(args[i].startsWith("-acmodifiers"))
                    {
                        parts = args[i].split(":");
                        if(parts.length == 2)
                        {
                            acModifiers = Boolean.parseBoolean(parts[1]);
                        }
                    }
                    
                    if(args[i].startsWith("-ocmtype"))
                    {
                        parts = args[i].split(":");
                        if(parts.length == 2)
                        {
                            ocmType = parts[1];
                        }
                    }
                    
                    if(args[i].startsWith("-symstd"))
                    {
                        parts = args[i].split(":");
                        if(parts.length == 2)
                        {
                            if(parts[1].equalsIgnoreCase("2525B"))
                                symStd = RendererSettings.Symbology_2525B;
                            else if(parts[1].equalsIgnoreCase("2525C"))
                                symStd = RendererSettings.Symbology_2525C;
                            else if(parts[1].equalsIgnoreCase("2525D"))
                                symStd = RendererSettings.Symbology_2525D;
                            else if(parts[1].equals("0"))
                                symStd = RendererSettings.Symbology_2525B;
                            else if(parts[1].equals("1"))
                                symStd = RendererSettings.Symbology_2525C;
                            else if(parts[1].equals("2"))
                                symStd = RendererSettings.Symbology_2525D;
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

                if(spOn)
                {
                    sr.startSinglePointServer(sPort, sBacklog);
                }
                
                if(mpOn)
                {
                    sr.startMultiPointServer(mPort, mBacklog);
                }
                
                if(symStd >=0)
                {
                    sr.setDefaultSymbologyStandard(symStd);
                }
                
                if(acModifiers == false)
                {
                    RendererSettings.getInstance().setAutoCollapseModifiers(acModifiers);
                }
                
                if(ocmType != null && SymbolUtilities.isNumber(ocmType))
                {
                    RendererSettings.getInstance().setOperationalConditionModifierType(Integer.parseInt(ocmType));
                }
		
		// System tray
		RendererSystemTray tray = new RendererSystemTray();
		tray.createSystemTray();
	}

}
