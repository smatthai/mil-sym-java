/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sec.web.renderer;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import sec.web.renderer.utilities.JavaRendererUtilities;
import ArmyC2.C2SD.RendererPluginInterface.ISinglePointInfo;
import ArmyC2.C2SD.Rendering.IJavaRenderer;
import ArmyC2.C2SD.Rendering.JavaRenderer;
import ArmyC2.C2SD.Rendering.TacticalGraphicIconRenderer;
import ArmyC2.C2SD.Utilities.ErrorLogger;
import ArmyC2.C2SD.Utilities.IPointConversion;
import ArmyC2.C2SD.Utilities.ImageInfo;
import ArmyC2.C2SD.Utilities.MilStdAttributes;
import ArmyC2.C2SD.Utilities.MilStdSymbol;
import ArmyC2.C2SD.Utilities.PointConversionDummy;
import ArmyC2.C2SD.Utilities.RendererException;
import ArmyC2.C2SD.Utilities.RendererSettings;
import ArmyC2.C2SD.Utilities.SinglePointFont;
import ArmyC2.C2SD.Utilities.SinglePointLookup;
import ArmyC2.C2SD.Utilities.SymbolDef;
import ArmyC2.C2SD.Utilities.SymbolDefTable;
import ArmyC2.C2SD.Utilities.SymbolUtilities;
import ArmyC2.C2SD.Utilities.UnitDefTable;
import ArmyC2.C2SD.Utilities.UnitFontLookup;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            
            private byte[] getMultiPointKML(HttpExchange he, String symbolCode)
            {
                byte[] byteArray = null;
                String kml = null;
                try
                {
                    //Map<String,String> params = JavaRendererUtilities.createParameterMapFromURL(symbolCode);
                    Map<String,String> params = (Map<String,String>)he.getAttribute("parameters");
                    
                    for (Map.Entry<String, String> entry : params.entrySet())
                    {
                        String test = entry.getValue().toLowerCase();
                        if(test.contains("script>"))
                        {
                            entry.setValue("");
                        }
                    }

                    String symbolID = null;
                    
                    int questionIndex = symbolCode.lastIndexOf('?');
                    if(questionIndex == -1)
                        symbolID = java.net.URLDecoder.decode(symbolCode, "UTF-8");
                    else
                        symbolID = java.net.URLDecoder.decode(symbolCode.substring(0, questionIndex), "UTF-8");
                    
                    

                    if(is2D == false)
                    {
                        String id = params.get("ID");
                        String name = params.get("NAME");
                        String description = params.get("DESCRIPTION");
                        String controlPoints = params.get("CONTROLPOINTS");
                        String altitudeMode = params.get("ALTITUDEMODE");
                        double scale = Double.valueOf(params.get("SCALE"));
                        String bbox = params.get("BBOX");
                        String modifiers = params.get("MODIFIERS");
                        int format = Integer.parseInt(params.get("FORMAT"));
                        int symStd = Integer.parseInt(params.get("SYMSTD"));
                        
                        kml = SECRenderer.getInstance().RenderMultiPointSymbol(
                                id, name, description, symbolID, controlPoints, 
                                altitudeMode, scale, bbox, modifiers, format, symStd);
                    }
                    else
                    {
                        String id = params.get("ID");
                        String name = params.get("NAME");
                        String description = params.get("DESCRIPTION");
                        String controlPoints = params.get("CONTROLPOINTS");
                        int pWidth = Integer.parseInt(params.get("PIXELWIDTH"));
                        int pHeight = Integer.parseInt(params.get("PIXELHEIGHT"));
                        String bbox = params.get("BBOX");
                        String modifiers = params.get("MODIFIERS");
                        int format = Integer.parseInt(params.get("FORMAT"));
                        int symStd = Integer.parseInt(params.get("SYMSTD"));
                        
                        kml = SECRenderer.getInstance().RenderMultiPointSymbol2D(
                                id, name, description, symbolID, controlPoints, 
                                pWidth, pHeight, bbox, modifiers, format, symStd);
                    }
                    
                    if(kml != null && kml.equals("")==false)
                    {
                        byteArray = kml.getBytes();
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
                catch(Exception exc)
                {
                    ErrorLogger.LogException("SinglePointServer", "getMultiPointKML", exc, Level.WARNING);
                }
                return byteArray;
            }
            
            public void handle(HttpExchange exchange) {


			if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
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
						headers.set("Content-Type", "text/plain");
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
						headers.set("Content-Type", "text/xml");
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

            Map<String,String> parameters = new HashMap();
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

         @SuppressWarnings("unchecked")
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
