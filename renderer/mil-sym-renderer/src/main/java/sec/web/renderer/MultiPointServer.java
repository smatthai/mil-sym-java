/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sec.web.renderer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import ArmyC2.C2SD.Utilities.ErrorLogger;
import com.sun.net.httpserver.Filter;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author Robert.Bathmann
 */
@SuppressWarnings({"unused","restriction"})
public class MultiPointServer {

	private HttpServer httpServer;

	private int portNumber = 6790;


        
        //backlog number <= 0 means use system default
        // I think the java default value is 50
        private int _backLog = 0;
        //private int _backLog = 200;

	public MultiPointServer(int port) {
		this.portNumber = port;
		createHttpServer();

	}
        
        public MultiPointServer(int port, int backlog) {
		this.portNumber = port;
		createHttpServer();

	}

	private void createHttpServer() {
		try {
			// A maximum backlog can be specified. This is the maximum number
			// of queued incoming connections to allow on the listening socket.
			// Queued connection above this limit may be rejected.
			// If set <=0, a system default value is used.

			int backlog = _backLog;
			httpServer = HttpServer.create(new InetSocketAddress("127.0.0.1", portNumber), backlog);

                        HttpContext c3d = httpServer.createContext("/mil-sym-service/renderer/mp3d/", new MultiPointHandler(false));
                        HttpContext c2d = httpServer.createContext("/mil-sym-service/renderer/mp2d/", new MultiPointHandler(true));
                        //for parsing post parameters
                        c3d.getFilters().add(new ParameterFilter());
                        c2d.getFilters().add(new ParameterFilter());
			httpServer.setExecutor(Executors.newCachedThreadPool());
		} catch(BindException bexc){
                    String strTypicalPortInUseMessage = "Address already in use: bind";
                    if(bexc.getMessage().startsWith(strTypicalPortInUseMessage))
                    {
                        System.out.println("Port " 
                            + String.valueOf(portNumber) 
                            + " already in use. Incrementing...");
                    }
                    else
                    {
                        System.err.println(bexc.getMessage());
                        bexc.printStackTrace();
                    }
                    
                    portNumber++;
                    //System.err.println(ErrorLogger.getStackTrace(bexc));
                    createHttpServer();
                    // throw(exc);
                } 
                catch (IOException exc) {
			System.err.println(exc.getMessage());
			System.err.println(ErrorLogger.getStackTrace(exc));
			//portNumber++;
			//createHttpServer();
			// throw(exc);
		} catch (Exception exc2) {
			System.err.println(exc2.getMessage());
			System.err.println(ErrorLogger.getStackTrace(exc2));
		}
	}

	public void start() {
		try {
			httpServer.start();
			System.out.println("Multi point server started on port #" + String.valueOf(portNumber));
		} catch (Exception e) {
			portNumber++;
			createHttpServer();
		} finally {

		}
	}

	public void stop() {

		if (httpServer != null) {
			try {
				httpServer.stop(0);
			} catch (Exception e) {
				httpServer = null;
			}
		}

	}

	public int getPortNumber() {
		return this.portNumber;
	}


       
        class MultiPointHandler implements HttpHandler {
            private SECRenderer sr = null;
            private Boolean is2D = false;
            
            public MultiPointHandler(Boolean for2D) {
                sr = SECRenderer.getInstance();
                is2D = for2D;
            }
            /**
             * copies Map to a new Map and cleans out values with script tags
             * @param params
             * @return 
             */
            private Map<String,String> copyParams(Map<String,String> params)
            {
                Map<String,String> copy = new HashMap<String, String>();
                String tempVal = null;
                
                copy.putAll(params);
                Iterator it = copy.entrySet().iterator();
                boolean retry = false;
                while(it.hasNext())
                {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>)it.next();
                    tempVal = entry.getValue();
                    if(tempVal != null)
                    {
                        String test = tempVal.toLowerCase();
                        if(test.contains("script>"))
                        {
                            entry.setValue("");
                        }
                        /*if(entry.getKey().equals("CONTROLPOINTS"))
                        {
                            if(entry.getValue() == null)
                                retry = true;
                        }//*/
                    }
                }//*/
                
                /*if(retry)
                {
                    try
                    {
                        Thread.sleep(50);
                        ErrorLogger.LogMessage("SinglePointServer", "getMultiPointKML", "waiting for values to populate", Level.WARNING,false);
                    }
                    catch(InterruptedException ie)
                    {
                        Thread.currentThread().interrupt();
                    }
                    copy.clear();
                    copy.putAll(params);
                    it = copy.entrySet().iterator();
                
                    while(it.hasNext())
                    {
                        Map.Entry<String, String> entry = (Map.Entry<String, String>)it.next();
                        tempVal = entry.getValue();
                        if(tempVal != null)
                        {
                            String test = tempVal.toLowerCase();
                            if(test.contains("script>"))
                            {
                                entry.setValue("");
                            }
                        }
                    }
                }//*/

                return copy;
            }
                        
            private byte[] getMultiPointKML(HttpExchange he, String symbolCode)
            {
                byte[] byteArray = null;
                String kml = null;
                int origCount = 0;
                int origCount2 = 0;
                int newCount = 0;
                int attempts = 0;
                int limit = 4;
                try
                {
                    //Map<String,String> params = JavaRendererUtilities.createParameterMapFromURL(symbolCode);
                    Map<String,String> params = (Map<String,String>)he.getAttribute("parameters");
                    origCount = params.size();
                    while(origCount < 11)
                    {
                        //ErrorLogger.LogMessage("SinglePointServer", "getMultiPointKML", "param size: " + String.valueOf(params.size()), Level.WARNING,false);
                        try
                        {
                            Thread.sleep(10);
                            origCount = params.size();
                            //ErrorLogger.LogMessage("SinglePointServer", "getMultiPointKML", "Had to wait", Level.WARNING,false);
                            //ErrorLogger.LogMessage("SinglePointServer", "getMultiPointKML", "param size: " + String.valueOf(params.size()), Level.WARNING,false);
                        }
                        catch(InterruptedException ie)
                        {
                            Thread.currentThread().interrupt();
                        }//*/
                        attempts++;
                        if(attempts >= limit)
                        {
                            //ErrorLogger.LogMessage("SinglePointServer", "getMultiPointKML", "param size: " + String.valueOf(origCount), Level.WARNING,false);
                            break;
                        }
                    }
                    
                    //copy and clean values
                    params = copyParams(params);
                    
                    String symbolID = null;
                    
                    int questionIndex = symbolCode.lastIndexOf('?');
                    if(questionIndex == -1)
                        symbolID = java.net.URLDecoder.decode(symbolCode, "UTF-8");
                    else
                        symbolID = java.net.URLDecoder.decode(symbolCode.substring(0, questionIndex), "UTF-8");


                    String id="";
                    String name="";
                    String description="";
                    String controlPoints="";
                    String altitudeMode="";
                    double scale=0;
                    String bbox="";
                    String modifiers="";
                    int format  = 0;
                    int symStd = 0;
                    int pWidth = 0;
                    int pHeight = 0;
                    String temp = "";
                    try
                    {
                        id = params.get("ID");
                        name = params.get("NAME");
                        description = params.get("DESCRIPTION");
                        controlPoints = params.get("CONTROLPOINTS");
                        if(params.containsKey("ALTITUDEMODE"))//3D
                            altitudeMode = params.get("ALTITUDEMODE");
                        if(params.containsKey("SCALE"))//3D
                        {
                            temp = params.get("SCALE");
                            scale = Double.valueOf(temp);
                        }
                        bbox = params.get("BBOX");
                        modifiers = params.get("MODIFIERS");
                        
                        temp = params.get("FORMAT");
                        format = Integer.parseInt(temp);
                        temp = params.get("SYMSTD");
                        symStd = Integer.parseInt(temp);
                        if(params.containsKey("PIXELWIDTH"))//2D
                        {
                            temp = params.get("PIXELWIDTH");
                            pWidth = Integer.parseInt(temp);
                        }
                        if(params.containsKey("PIXELHEIGHT"))//2D
                        {
                            temp = params.get("PIXELHEIGHT");
                            pHeight = Integer.parseInt(params.get("PIXELHEIGHT"));
                        }
                    }
                    catch(Exception exc1)
                    {
                        ErrorLogger.LogException("SinglePointServer", "getMultiPointKML", exc1, Level.WARNING);
                    }
                    try
                    {
                        if(is2D == false)
                        {
                            kml = SECRenderer.getInstance().RenderMultiPointSymbol(
                                    id, name, description, symbolID, controlPoints, 
                                    altitudeMode, scale, bbox, modifiers, format, symStd);
                        }
                        else
                        {
                            kml = SECRenderer.getInstance().RenderMultiPointSymbol2D(
                                    id, name, description, symbolID, controlPoints, 
                                    pWidth, pHeight, bbox, modifiers, format, symStd);
                        }
                    }
                    catch(Exception foo)
                    {
                        ErrorLogger.LogException("SinglePointServer", "getMultiPointKML", foo, Level.WARNING);
                        //new String[]{"blah", "hey", "yo"}
                        String[] parameters = null;
                        if(is2D == false)
                            parameters = new String[]{id, name, description, symbolID, controlPoints, altitudeMode, String.valueOf(scale), bbox, modifiers, String.valueOf(format), String.valueOf(symStd)};
                        else
                            parameters = new String[]{id, name, description, symbolID, controlPoints, String.valueOf(pWidth), String.valueOf(pHeight), bbox, modifiers, String.valueOf(format), String.valueOf(symStd)};
                        ErrorLogger.LogMessage("SinglePointServer", "getMultiPointKML", "parameters: ", Level.WARNING, parameters, false);
                    }
                    
                    if(kml != null && kml.equals("")==false)
                    {
                        byteArray = kml.getBytes("UTF-8");
                    }
                }
                catch(UnsupportedEncodingException uee)
                {
                    ErrorLogger.LogException("SinglePointServer", "getMultiPointKML", uee, Level.WARNING);
                }
                catch(NumberFormatException nfe)
                {
                    ErrorLogger.LogException("SinglePointServer", "getMultiPointKML", nfe, Level.WARNING);
                }
                catch(ConcurrentModificationException cme)
                {
                    ErrorLogger.LogException("SinglePointServer", "getMultiPointKML", cme, Level.WARNING);
                }
                catch(Exception exc)
                {
                    ErrorLogger.LogException("SinglePointServer", "getMultiPointKML", exc, Level.WARNING);
                }
                return byteArray;
            }
            
            public void handle(HttpExchange exchange) {


			if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                            //get format
                            int format = -1;
                            String contentType = "text/plain;charset=UTF-8";
                            Map<String,String> params = (Map<String,String>)exchange.getAttribute("parameters");
                            if(params != null && params.containsKey("FORMAT"))
                            {
                                format = Integer.parseInt(params.get("FORMAT"));
                                if(format == 0)
                                   contentType = "text/xml;charset=UTF-8"; 
                                else if(format == 1 || format == 2)
                                   contentType = "application/json;charset=UTF-8"; 
                            }
                                
                            
				String url = null;
				// String url = exchange.getRequestURI().getPath();
				// String url = exchange.getRequestURI().toString();
                                String allowOrigin;
                                allowOrigin = exchange.getRemoteAddress().toString();
				byte[] kmlResponse = null;

				try {
					url = exchange.getRequestURI().toString();
					String symbolID = url.substring(url.lastIndexOf("/") + 1);
					// System.out.println(symbolID);
					kmlResponse = getMultiPointKML(exchange, symbolID);

				} catch (Exception exc) {
					// System.err.println(exc.getMessage());
					ErrorLogger.LogException("MultiPointServer", "handle", exc, Level.WARNING);

					try {
						Headers headers = exchange.getResponseHeaders();
						headers.set("Content-Type", "text/plain;charset=UTF-8");
                                                if(allowOrigin.contains("127.0.0.1"))
                                                    headers.set("Access-Control-Allow-Origin", "*");
						exchange.sendResponseHeaders(503, 0);
						exchange.getResponseBody().close();
					} catch (IOException ex) {
						// System.err.println(ex.getMessage());
						ErrorLogger.LogException("MultiPointServer", "handle", ex, Level.WARNING);
					} catch (Exception exc2) {
						ErrorLogger.LogException("MultiPointServer", "handle", exc2, Level.WARNING);
					}
				}

				if (kmlResponse != null) {

					OutputStream responseBody = null;
					try {
						Headers headers = exchange.getResponseHeaders();
						headers.set("Content-Type", contentType);
                                                if(allowOrigin.contains("127.0.0.1"))
                                                    headers.set("Access-Control-Allow-Origin", "*");//127.0.0.1
						exchange.sendResponseHeaders(200, 0);

						responseBody = exchange.getResponseBody();
						responseBody.write(kmlResponse);
						responseBody.close();
					} catch (IOException exc) {
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
						System.err.println("MultiPointServer.handle() tried sending response");
						// System.err.println(date.toString());
						System.err.println(sdf.format(date));
						System.err.println(exchange.getRequestURI().toString());
						System.err.println(exc.getMessage());

					} catch (Exception exc3) {
						ErrorLogger.LogException("MultiPointServer", "handle", exc3, Level.WARNING);
					}
				}
			}
		}
        }
        
	
        
    /**
     * from:
     * whowish-programming.blogspot.com/2011/04/get-post-parameters-from-java-http.html
     * with modifications
     */
    public class ParameterFilter extends Filter 
    {

        @Override
        public String description() {
            return "Parses the requested URI for parameters";
        }

        @Override
        public void doFilter(HttpExchange exchange, Filter.Chain chain)
            throws IOException {
            parseGetParameters(exchange);
            parsePostParameters(exchange);
            chain.doFilter(exchange);
        }

        private void parseGetParameters(HttpExchange exchange)
            throws UnsupportedEncodingException {

            Map<String,String> parameters = new HashMap<String, String>();
            URI requestedUri = exchange.getRequestURI();
            String query = requestedUri.getRawQuery();
            parseQuery(query, parameters);
            exchange.setAttribute("parameters", parameters);
        }

        private void parsePostParameters(HttpExchange exchange)
            throws IOException {

            if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
                @SuppressWarnings("unchecked")
                Map parameters =
                    (Map)exchange.getAttribute("parameters");
                InputStreamReader isr =
                    new InputStreamReader(exchange.getRequestBody(),"utf-8");
                BufferedReader br = new BufferedReader(isr);
                String query = br.readLine();
                parseQuery(query, parameters);
            }
        }

         //@SuppressWarnings("unchecked")
         private void parseQuery(String query, Map<String,String> parameters)
             throws UnsupportedEncodingException {

             if (query != null) {
                 String pairs[] = query.split("[&]");

                 for (String pair : pairs) {
                     String param[] = pair.split("[=]");

                     String key = null;
                     String value = null;
                     if (param.length > 0) {
                         key = URLDecoder.decode(param[0],
                             System.getProperty("file.encoding")).toUpperCase();
                     }

                     if (param.length > 1) {
                         value = URLDecoder.decode(param[1],
                             System.getProperty("file.encoding"));
                     }

                      parameters.put(key, value);

                 }
             }
        }
    }

}
