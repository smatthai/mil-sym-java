/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.web.renderer;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

import ArmyC2.C2SD.RendererPluginInterface.*;
import ArmyC2.C2SD.Utilities.*;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;

/**
 * 
 * @author michael.spinelli
 */
@SuppressWarnings({"unused","rawtypes","unchecked"})
public class SinglePointRendererService {

	private static SinglePointRendererService service;
	private static ServiceLoader<ISinglePointRenderer> loader;
	private static Map<String, ISinglePointRenderer> spRenderers = new HashMap<String, ISinglePointRenderer>();

        private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock(true);
        
	private SinglePointRendererService() {
		try {
			// ErrorLogger.Entering("SinglePointRendererService",
			// "SinglePointRendererService");
			// AddRenderersToPath();
			loader = ServiceLoader.load(ArmyC2.C2SD.RendererPluginInterface.ISinglePointRenderer.class);
			// LoadSPRendererServices();
			// ErrorLogger.Exiting("SinglePointRendererService",
			// "SinglePointRendererService");
		} catch (Exception exc) {
			ErrorLogger.LogException("SinglePointRendererService", "SinglePointRendererService", exc);
			ErrorLogger.LogMessage(exc.getMessage(), true);
		}
	}

	public void LoadSPRendererServices() 
        {
            ISinglePointRenderer temp = null;
            try 
            {
               
                rwl.writeLock().lock();
                loader.reload();

                System.out.println("Looking for renderer plugins...");
                Iterator<ISinglePointRenderer> itr = loader.iterator();
                while (itr.hasNext() == true) {
                        temp = itr.next();
                        if (temp != null) {
                            System.out.println("Found renderer plugin: " + temp.getRendererID());
                                if (spRenderers.containsKey(temp.getRendererID()) == false)
                                        spRenderers.put(temp.getRendererID(), temp);
                        }
                }

            } catch (Exception exc) {
                ErrorLogger.LogException("SinglePointRendererService", "LoadSPREendererServices", exc);
            }
            finally
            {
                rwl.writeLock().unlock();
            }
                
		// System.out.println("end of LoadSPRendererServices()");
	}

	public static synchronized SinglePointRendererService getInstance() {
		if (service == null)
                {
			service = new SinglePointRendererService();
                }
		return service;
	}

        public boolean canRender(String rendererID, String symbolID, Map<String,String> params) {
		// System.out.println("plugin " + rendererID + ": " + symbolID);
		ISinglePointRenderer renderer = spRenderers.get(rendererID);
		if (renderer != null && renderer.equals("")==false)
                {
			return renderer.canRender(symbolID, params);
                }
		else
                {
                    System.out.println("Couldn't find renderer: " + rendererID);
                    return false;
                }
	}
        
        public ISinglePointInfo render(String rendererID, String symbolID, Map<String,String> params) {
		//System.out.println("plugin " + rendererID + ": " + symbolID);
            ISinglePointInfo returnVal = null;
            try
            {
                rwl.readLock().lock();
		ISinglePointRenderer renderer = spRenderers.get(rendererID);
                
		if (renderer != null)
                {
                    //System.out.println("has renderer " + rendererID);    
                    try
                    {
                        returnVal = renderer.render(symbolID, params);
                    }
                    catch(Exception exc)
                    {
                        //using Level.FINER because a null value will cause the
                        //milstd2525 renderer to draw an unknown symbol.
                        String message = "Plugin \"" +
                                rendererID +
                                "\" failed to produce an image for symboldID \"" +
                                symbolID + "\"";
                        ErrorLogger.LogMessage("SinglePointRendererService", "render", message, Level.FINER);
                        ErrorLogger.LogException("SinglePointRendererService", "render", exc, Level.FINER);
                        //System.err.println(exc.getMessage());
                        //exc.printStackTrace();
                    }
                }
		else
                {
			return null;
                }
            }
            catch(Exception exc2)
            {
                System.err.println(exc2.getMessage());
                exc2.printStackTrace();
            }
            finally
            {
                rwl.readLock().unlock();
                return returnVal;
            }
                
	}
        
        /**
         * will process url string and break url parameters into a hash table
         * @param url
         * @return 
         */
        public Map<String,String> processParams(String url)
        {
            String symbolInfo = url;
            Map<String, String> modifiers = new HashMap<String, String>();
            String symbolID = null;
            String parameters = null;
            String key = null;
            String value = null;
            String arrParameters[] = null;
            String arrKeyValue[] = null;
            String temp = null;

            int questionIndex = symbolInfo.indexOf('?');
            try
            {
                if(questionIndex == -1)
                    symbolID = symbolInfo;
                else
                     symbolID = symbolInfo.substring(0, questionIndex);
            }
            catch(Exception exc)
            {
                System.err.println("Error parsing SymbolID");
                System.err.println(exc.getMessage());
            }

            try
            {   //build a map for the other createMilstdSymbol function to use
                //to build a milstd symbol.
                if(questionIndex > 0 && (questionIndex + 1 < symbolInfo.length()))
                {
                    parameters = symbolInfo.substring(questionIndex + 1,symbolInfo.length());
                    arrParameters = parameters.split("&");

                    for(int i = 0; i < arrParameters.length; i++)
                    {
                        arrKeyValue = arrParameters[i].split("=");
                        if(arrKeyValue.length == 2)
                        {
                            key = arrKeyValue[0];
                            value = arrKeyValue[1];
                               
                            temp = java.net.URLDecoder.decode(value, "UTF-8");
                            modifiers.put(key.toUpperCase(), temp);
                        }
                    }
                }
            }
            catch(Exception exc)
            {
                System.err.println("Error parsing \"" + key + "\" parameter from URL");
                System.err.println(exc.getMessage());
            }
            return modifiers;
        }

	public ArrayList<String> getSinglePointRendererIDs() {
		ArrayList<String> renderers = new ArrayList<String>();
		ISinglePointRenderer temp = null;
		Iterator<ISinglePointRenderer> itr = loader.iterator();
		try {
			while (itr.hasNext()) {
				temp = itr.next();
				if (temp != null) {
					renderers.add(temp.getRendererID());
				}
			}
		} catch (Exception exc) {
			ErrorLogger.LogException("SinglePointRendererService", "getSinglePointRendererIDs", exc);
		}
		return renderers;

	}

	public Boolean hasRenderer(String rendererID) {
		try {
			if (spRenderers != null && spRenderers.containsKey(rendererID))
				return true;
			else
				return false;
		} catch (Exception exc) {
			ErrorLogger.LogException("SinglePointRendererService", "hasRenderer", exc);
		}
		return false;
	}


	/**
	 * Load a plugin based on a String URL
	 * 
	 * @param urls a array of String URL paths to the plugins
	 */
	public void AddRenderersToPath(String url) {
		try {
			if (url != null && url.equals("")==false) {
				addURL(new URL(url));
				//loader.reload();
				//LoadSPRendererServices();
			}
		} catch (Exception exc) {
			ErrorLogger.LogException("SinglePointRendererService", "AddRenderersToPath", exc);
		}

	}
        
        /**
	 * Loads plugins based on String URLs
	 * 
	 * @param urls a array of String URL paths to the plugins
	 */
	public void AddRenderersToPath(List<String> urls) {
		try {
                        String url = null;
                        if(urls != null && urls.size() > 0)
                        {
                            for(int i = 0; i < urls.size(); i++)
                            {
                                url = urls.get(i);
                                if (url != null && url.equals("")==false) {
                                        addURL(new URL(url));
                                }
                            }
                            //loader.reload();
                            //LoadSPRendererServices();
                        }
		} catch (Exception exc) {
			ErrorLogger.LogException("", "AddRenderersToPath", exc);
		}

	}

        
	public void AddRenderersFromPluginFolder() {
		try {
			File foo = new File("");
			File directory = new File(foo.getAbsolutePath() + "\\plugins");
			// File directory = new File(foo.getAbsolutePath());

			// ErrorLogger.LogMessage(directory.getAbsolutePath(),
			// Boolean.TRUE);

			if (directory.exists()) {
				// ErrorLogger.LogMessage("exists", Boolean.TRUE);
				File[] files = directory.listFiles();
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.getName().endsWith("jar") == true) {
						// System.out.println(file.toString());
						addURL(file.toURI().toURL());
					}
				}
			}
			//loader.reload();
			//LoadSPRendererServices();
		} catch (Exception exc) {
			ErrorLogger.LogException("SinglePointRendererService", "AddRenderersFromPluginFolder", exc);
		}
	}

	public void AddRenderersToPathByFile(File file) {
		try {
			if (file.exists()) {
				if (file.getName().endsWith("jar") == true) {
					// System.out.println(file.toString());
					addURL(file.toURI().toURL());
				}
			}

			//loader.reload();
			//LoadSPRendererServices();
		} catch (Exception exc) {
			ErrorLogger.LogException("SinglePointRendererService", "AddRenderersToPathByDirectory", exc);
		}
	}

	public void AddRenderersToPathByDirectory(File directory) {
		try {
                        //System.out.println("Loading plugins from: ");
                        //System.out.println(directory.getAbsolutePath());
			if (directory.exists()) {
				// ErrorLogger.LogMessage("exists", Boolean.TRUE);
				File[] files = directory.listFiles();
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.getName().endsWith("jar") == true) {
						//System.out.println(file.toString());
						addURL(file.toURI().toURL());
					}
				}
			}

			//loader.reload();
			//LoadSPRendererServices();
		} catch (Exception exc) {
			ErrorLogger.LogException("SinglePointRendererService", "AddRenderersToPathByDirectory", exc);
		}
	}

	private final Class[] parameters = new Class[] { URL.class };

	/*
	 * private ClassLoader _cl = null; public void setClassLoader(ClassLoader
	 * cl) { _cl = cl; }
	 */

	private Boolean isClassLoaded(String name) {
		Boolean returnVal = false;
		ClassLoader cl = null;
		// if(_cl==null)
		cl = SinglePointRendererService.class.getClassLoader();
		// else
		// cl = _cl;
		try {
			// if(cl instanceof URLClassLoader)
			// System.out.println("yes");
			cl.loadClass(name);
			returnVal = true;
		} catch (ClassNotFoundException cnfe) {

		}
		return returnVal;
	}

	private void addURL(URL u) 
        {
            try 
            {
                URLClassLoader sysLoader = (URLClassLoader) SinglePointRendererService.class.getClassLoader();

                // System.out.println(isClassLoaded("ArmyC2.C2SD.RendererPluginInterface.ISinglePointRenderer"));

                URL urls[] = sysLoader.getURLs();

                // System.out.println("existing paths:");
                for (int i = 0; i < urls.length; i++) {

                        // System.out.println(urls[i].toString());
                        if (urls[i].toString().equalsIgnoreCase(u.toString())) {
                                // already in path
                                return;
                        }
                }
                Class sysclass = URLClassLoader.class;


                Method method = sysclass.getDeclaredMethod("addURL", parameters);
                method.setAccessible(true);
                method.invoke(sysLoader, new Object[] { u });

            } catch (Exception exc) {
                    ErrorLogger.LogException("SinglePointRendererService", "addUrl", exc);
            }

	}

}
