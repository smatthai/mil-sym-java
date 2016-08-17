/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sec.web.renderer;

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
import ArmyC2.C2SD.Utilities.ImageInfo;
import ArmyC2.C2SD.Utilities.MilStdAttributes;
import ArmyC2.C2SD.Utilities.MilStdSymbol;
import ArmyC2.C2SD.Utilities.RendererException;
import ArmyC2.C2SD.Utilities.SinglePointFont;
import ArmyC2.C2SD.Utilities.SinglePointLookup;
import ArmyC2.C2SD.Utilities.SymbolDefTable;
import ArmyC2.C2SD.Utilities.SymbolUtilities;
import ArmyC2.C2SD.Utilities.UnitDefTable;
import ArmyC2.C2SD.Utilities.UnitFontLookup;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.BindException;
import java.util.Map;
import static sec.web.renderer.utilities.JavaRendererUtilities.sanitizeSymbolID;
import sec.web.renderer.utilities.PNGInfo;

/**
 * 
 * @author Robert.Bathmann
 */
@SuppressWarnings({"unused","restriction"})
public class SinglePointServer {

	private static IJavaRenderer jr = null;
        private static TacticalGraphicIconRenderer tgir = null;
	private SinglePointRendererService plugins = null;
	private static boolean _preloadedRenderer = false;
        private SinglePointHandler _singlePointHandler = null;
	private HttpServer httpServer;
        //private HttpServer httpServerMP;
	private int portNumber = 6789;

        
        //backlog number <= 0 means use system default
        // I think the java default value is 50
        private int _backLog = 0;
        //private int _backLog = 200;

	public SinglePointServer(int port) {
		this.portNumber = port;
		createHttpServer();

	}
        
        public SinglePointServer(int port, int backlog) {
		this.portNumber = port;
		createHttpServer();
	}

	private void createHttpServer() 
        {
            try {
                    // A maximum backlog can be specified. This is the maximum number
                    // of queued incoming connections to allow on the listening socket.
                    // Queued connection above this limit may be rejected.
                    // If set <=0, a system default value is used.
                    _singlePointHandler = new SinglePointHandler(0);
                    int backlog = _backLog;
                    httpServer = HttpServer.create(new InetSocketAddress("127.0.0.1", portNumber), backlog);

                    httpServer.createContext("/", _singlePointHandler);
                    httpServer.createContext("/mil-sym-service/renderer/image/", new SinglePointHandler(RENDER_TYPE_IMAGE));
                    httpServer.createContext("/mil-sym-service/renderer/kml/", new SinglePointHandler(RENDER_TYPE_KML));
                    httpServer.createContext("/mil-sym-service/renderer/svg/", new SinglePointHandler(RENDER_TYPE_SVG));

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
			System.out.println("Single point server started on port #" + String.valueOf(portNumber));
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

	/**
	 * 
	 * @param SymbolInfo
	 *            - same string you'd send to singlePointServer.handle()
	 * @param center
	 *            - Pixel location that represents where the image should be
	 *            centered.
	 * @deprecated - use version with Rentangle2D bounds parameter.
	 */
	public void getSinglePointDimensions(String SymbolInfo, Point2D center) {
		Rectangle2D bounds = new Rectangle2D.Double();
		getSinglePointDimensions(SymbolInfo, center, bounds);
	}

	/**
	 * 
	 * @param SymbolInfo
	 *            - same string you'd send to singlePointServer.handle()
	 * @param center
	 *            - pixel location that represents where the image should be
	 *            centered.
	 * @param bounds
	 *            - Minimum Bounding Rectangle of the core symbol
	 */
	public void getSinglePointDimensions(String SymbolInfo, Point2D center, Rectangle2D bounds) {
		_singlePointHandler.getSinglePointDimensions(SymbolInfo, center, bounds, null);
	}

	/**
	 * 
	 * @param SymbolInfo
	 *            - same string you'd send to singlePointServer.handle()
	 * @param center
	 *            - pixel location that represents where the image should be
	 *            centered.
	 * @param bounds
	 *            - Minimum Bounding Rectangle of the core symbol
	 * @param iconExtent
	 *            - width and height of the image.
	 */
	public void getSinglePointDimensions(String SymbolInfo, Point2D center, Rectangle2D bounds, Dimension2D iconExtent) {
		_singlePointHandler.getSinglePointDimensions(SymbolInfo, center, bounds, iconExtent);
	}

	public byte[] getSinglePointByteArray(String SymbolInfo) {
		return _singlePointHandler.getSinglePointByteArray(SymbolInfo);
	}

	public void setTacticalGraphicPointSize(int size) {
		jr.setSinglePointTGSymbolSize(size);
	}

	public void setUnitPointSize(int size) {
		jr.setUnitSymbolSize(size);
	}
        
        private int RENDER_TYPE_IMAGE = 0;
        private int RENDER_TYPE_KML = 1;
        private int RENDER_TYPE_SVG = 2;
            
	class SinglePointHandler implements HttpHandler {

            
            
                private int _renderType = 0;
                private SECRenderer sr = SECRenderer.getInstance();
		public SinglePointHandler(int renderType) {
			initRenderer();
                        _renderType = renderType;
		}

		/**
		 * 
		 * @param symbolID
		 * @return
		 */
		public byte[] getSinglePointByteArray(String symbolID) 
                {
                    byte[] pngResponse = null;

                    try 
                    {
                        // symbolID = url.substring(url.lastIndexOf("/") + 1);
                        // System.out.println(symbolID);
                        Map<String,String> params = JavaRendererUtilities.createParameterMapFromURL(symbolID);
                        pngResponse = getSinglePointBytes(symbolID, params);
                        return pngResponse;
                    } catch (Exception exc) {
                        // System.err.println(exc.getMessage());
                        ErrorLogger.LogException("SinglePointServer", "getSinglePointByteArray", exc, Level.WARNING);
                        return null;
                    }
		}

		public void handle(HttpExchange exchange) 
                {

                    String allowOrigin = exchange.getRemoteAddress().toString();
                    if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                            String url = null;
                            // String url = exchange.getRequestURI().getPath();
                            // String url = exchange.getRequestURI().toString();

                            byte[] pngResponse = null;
                            byte[] kmlResponse = null;
                            byte[] svgResponse = null;
                            int svgDrawMode = 0;
                            Boolean isIcon = false;

                            try {
                                    url = exchange.getRequestURI().toString();
                                    url = exchange.getLocalAddress().toString() + url;
                                    if(url.startsWith("/"))
                                        url = url.substring(1);
                                    String symbolID = url.substring(url.lastIndexOf("/") + 1);

                                    Map<String,String> params = JavaRendererUtilities.createParameterMapFromURL(url);
                                    // System.out.println(symbolID);
                                    if(_renderType == RENDER_TYPE_IMAGE)
                                    {
                                        if(params.containsKey(MilStdAttributes.DrawAsIcon))
                                        {
                                            isIcon = Boolean.parseBoolean(params.get(MilStdAttributes.DrawAsIcon));
                                            if(isIcon)
                                            {
                                                params = JavaRendererUtilities.parseIconParameters(symbolID, params);
                                            }
                                        }

                                        pngResponse = getSinglePointBytes(symbolID, params);
                                    }
                                    else if(_renderType == RENDER_TYPE_KML)
                                    {
                                        if(url.indexOf("?") > -1)
                                            url = url.substring(0,url.indexOf("?"));
                                        String kml = sr.getSymbolImageKML(url, symbolID, params);
                                        kmlResponse = kml.getBytes();
                                    }
                                    else if(_renderType == RENDER_TYPE_SVG)
                                    {
                                        if(symbolID.indexOf("?") > -1)
                                            symbolID = symbolID.substring(0,symbolID.indexOf("?"));

                                        if(params.containsKey(MilStdAttributes.DrawAsIcon)&& 
                                                (Boolean.parseBoolean(params.get(MilStdAttributes.DrawAsIcon)) == true))
                                        {
                                            params = JavaRendererUtilities.parseIconParameters(symbolID, params);
                                            symbolID = sanitizeSymbolID(symbolID);
                                            isIcon = true;
                                        }

                                        PNGInfo pi = sr.getMilStdSymbolImage(symbolID, params);

                                        if(isIcon)
                                            pi = pi.squareImage();
                                        else
                                        {
                                            if(params.containsKey("CENTER") && 
                                                    (Boolean.parseBoolean(params.get("CENTER")) == true))
                                            {
                                                svgDrawMode = 1;
                                            }
                                            else if(params.containsKey("SQUARE") && 
                                                    (Boolean.parseBoolean(params.get("SQUARE")) == true))
                                            {
                                                svgDrawMode = 2;
                                            }
                                        }

                                        svgResponse = pi.toSVG(svgDrawMode).getBytes();   
                                    }
                                    else
                                    {
                                        pngResponse = getSinglePointBytes(symbolID, params);
                                    }

                            } catch (Exception exc) {
                                    // System.err.println(exc.getMessage());
                                    ErrorLogger.LogException("SinglePointServer", "handle", exc, Level.WARNING);

                                    try {
                                            Headers headers = exchange.getResponseHeaders();
                                            headers.set("Content-Type", "text/plain");
//                                                if(allowOrigin.contains("127.0.0.1"))
//                                                    headers.set("Access-Control-Allow-Origin", allowOrigin);//*;//127.0.0.1
                                            exchange.sendResponseHeaders(503, 0);
                                            exchange.getResponseBody().close();
                                    } catch (IOException ex) {
                                            // System.err.println(ex.getMessage());
                                            ErrorLogger.LogException("SinglePointServer", "handle", ex, Level.WARNING);
                                    } catch (Exception exc2) {
                                            ErrorLogger.LogException("SinglePointServer", "handle", exc2, Level.WARNING);
                                    }
                            }

                            if (pngResponse != null) {

                                    OutputStream responseBody = null;
                                    try {
                                            Headers headers = exchange.getResponseHeaders();
                                            headers.set("Content-Type", "image/png");
//                                                if(allowOrigin.contains("127.0.0.1"))
//                                                    headers.set("Access-Control-Allow-Origin", allowOrigin);//127.0.0.1
                                            exchange.sendResponseHeaders(200, 0);

                                            responseBody = exchange.getResponseBody();
                                            responseBody.write(pngResponse);
                                            responseBody.close();
                                    } catch (IOException exc) {
                                            Date date = new Date();
                                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
                                            System.err.println("SinglePointServer.handle() tried sending response");
                                            // System.err.println(date.toString());
                                            System.err.println(sdf.format(date));
                                            System.err.println(exchange.getRequestURI().toString());
                                            System.err.println(exc.getMessage());

                                    } catch (Exception exc3) {
                                            ErrorLogger.LogException("SinglePointServer", "handle", exc3, Level.WARNING);
                                    }
                            }

                            else if(kmlResponse != null)
                            {
                                OutputStream responseBody = null;
                                    try {
                                            Headers headers = exchange.getResponseHeaders();
                                            headers.set("Content-Type", "text/xml");
                                            //only required for POST
                                            /*if(allowOrigin.contains("127.0.0.1"))
                                                headers.set("Access-Control-Allow-Origin", "*");//127.0.0.1*/
                                            exchange.sendResponseHeaders(200, 0);

                                            responseBody = exchange.getResponseBody();
                                            responseBody.write(kmlResponse);
                                            responseBody.close();
                                    } catch (IOException exc) {
                                            Date date = new Date();
                                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
                                            System.err.println("SinglePointServer.handle() tried sending response");
                                            // System.err.println(date.toString());
                                            System.err.println(sdf.format(date));
                                            System.err.println(exchange.getRequestURI().toString());
                                            System.err.println(exc.getMessage());

                                    } catch (Exception exc3) {
                                            ErrorLogger.LogException("SinglePointServer", "handle", exc3, Level.WARNING);
                                    }
                            }

                            else if(svgResponse != null)
                            {
                                OutputStream responseBody = null;
                                    try {
                                            Headers headers = exchange.getResponseHeaders();
                                            headers.set("Content-Type", "image/svg+xml");
                                            //only required for POST
                                            /*if(allowOrigin.contains("127.0.0.1"))
                                                headers.set("Access-Control-Allow-Origin", "*");//127.0.0.1*/
                                            exchange.sendResponseHeaders(200, 0);

                                            responseBody = exchange.getResponseBody();
                                            responseBody.write(svgResponse);
                                            responseBody.close();
                                    } catch (IOException exc) {
                                            Date date = new Date();
                                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
                                            System.err.println("SinglePointServer.handle() tried sending response");
                                            // System.err.println(date.toString());
                                            System.err.println(sdf.format(date));
                                            System.err.println(exchange.getRequestURI().toString());
                                            System.err.println(exc.getMessage());

                                    } catch (Exception exc4) {
                                            ErrorLogger.LogException("SinglePointServer", "handle", exc4, Level.WARNING);
                                    }
                            }
			}
		}

		/**
		 * Initialize renderer settings Synchronized so that the first several
		 * calls don't try to all init the renderer. Only needs to happen once.
		 * 
		 * @author Spinelli
		 */
		private synchronized void initRenderer() {
			try {
				if (jr == null) 
                                {
                                    jr = JavaRenderer.getInstance();

                                    // sets the font size for the unit characters
                                    jr.setUnitSymbolSize(50);
                                    // sets the font size for the TG single point characters
                                    jr.setSinglePointTGSymbolSize(80);

                                    preloadRenderer();

				}
                                if(tgir == null)
                                {
                                    tgir = TacticalGraphicIconRenderer.getInstance();
                                }
				if (plugins == null)
                                {
					plugins = SinglePointRendererService.getInstance();
                                }
			} catch (Exception exc) {
				// System.err.println(exc.getMessage());
				// System.err.println(ErrorLogger.getStackTrace(exc));
				ErrorLogger.LogException("SinglePointServer", "initRenderer", exc, Level.WARNING);
			}
		}

		/**
		 * Try to make sure everything is loaded during server creation so later
		 * calls to the renderer don't crap out due to the font files not being
		 * loaded. Sometimes the 'X's appear because of font loading errors.
		 * This is an attempt to fix that.
		 * 
		 * @author Spinelli
		 */
		private void preloadRenderer() {
			try {
				if (_preloadedRenderer == false) {
					MilStdSymbol foo = null;
					foo = new MilStdSymbol("SFGPUCDM-------", "", null, null);

					UnitFontLookup.getInstance();
					SinglePointLookup.getInstance();
					SymbolDefTable.getInstance();
					UnitDefTable.getInstance();
					foo = null;
					SinglePointFont.getInstance().getSPFont(50);
					SinglePointFont.getInstance().getUnitFont(50);

					_preloadedRenderer = true;

					// System.out.println("preloaded");
				}
			} catch (Exception exc) {
				// System.err.println(exc.getMessage());
				// System.err.println(ErrorLogger.getStackTrace(exc));
				ErrorLogger.LogException("SinglePointServer", "preloadRenderer", exc, Level.WARNING);
			}
		}

		/**
		 * @param SymbolInfo
		 *            - same string you'd send to singlePointServer.handle()
		 *            example:
		 *            “SFGPUCDMH-*****?LineColor=0x000000&FillColor=0xFFFFFF
		 *            &size=35”
		 * @param center
		 *            - pixel location that represents where the image should be
		 *            centered.
		 * @param bounds
		 *            - Minimum Bounding Rectangle of the core symbol
		 * @author Spinelli
		 */
		public void getSinglePointDimensions(String SymbolInfo, Point2D center, Rectangle2D bounds, Dimension2D iconExtent) 
                {
                    initRenderer();
                    try 
                    {

                        // prepare implement IPointConversion or use our basic point
                        Map<String,String> params = JavaRendererUtilities.createParameterMapFromURL(SymbolInfo);

                        // check if plugin renderer was requested
                        String renderer = params.get(MilStdAttributes.Renderer);
                        if(renderer==null || renderer.equals(""))
                        {
                            renderer=SinglePoint2525Renderer.RENDERER_ID;
                        }
                        if(plugins.hasRenderer(renderer)==false)
                        {
                            //if renderer id doesn't exist or is no good, set to default plugin.
                            renderer=SinglePoint2525Renderer.RENDERER_ID;
                        }

                        String pluginSymbolID = null;

                        ISinglePointInfo spi = null;
                        if (renderer != null && plugins != null && plugins.hasRenderer(renderer)) {
                            int questionIndex = SymbolInfo.indexOf('?');
                            if (questionIndex == -1)
                                    pluginSymbolID = SymbolInfo;
                            else
                                    pluginSymbolID = SymbolInfo.substring(0, questionIndex);
                            // try to render with plugin
                            // System.out.println(symbol.getSymbolID());
                            // System.out.println("plugin " + renderer + ": " +
                            // pluginSymbolID);
                            spi = plugins.render(renderer, pluginSymbolID, params);
                        }

                        ImageInfo iInfo;

                        if (spi != null && spi.getImage() != null) 
                        { // if plugin render success, 
                            //create image info with result
                            iInfo = new ImageInfo(spi.getImage(), 0, 0, (int) spi.getSymbolCenterPoint().getX(), (int) spi
                                            .getSymbolCenterPoint().getY(), spi.getSymbolBounds());

                            center.setLocation(iInfo.getSymbolCenterPoint());
                            bounds.setFrame(iInfo.getSymbolBounds());
                            if (iconExtent != null)
                                iconExtent.setSize(iInfo.getImage().getWidth(), iInfo.getImage().getHeight());
                        } 

                    } catch (Exception exc) {
                            System.out.println(exc.getMessage());
                    }
            }


                
		/**
		 * 
		 * @param symbolCode
		 * @return
		 * @throws RendererException
		 * @throws IOException
		 */
		private byte[] getSinglePointBytes(String symbolCode, Map<String,String> params) throws RendererException, IOException {
			byte[] byteArray = null;

			// This will get an instance of the renderer and initialize if it
			// hasn't
			// been initialized yet.
			initRenderer();
			// ////////////////////////////////////////////

			try {

				// check if plugin renderer was requested
                                String renderer = params.get(MilStdAttributes.Renderer);
                                if(renderer==null || renderer.equals(""))
                                {
                                    renderer=SinglePoint2525Renderer.RENDERER_ID;
                                }
                                if(plugins.hasRenderer(renderer)==false)
                                {
                                    //if renderer id doesn't exist or is no good, set to default plugin.
                                    renderer=SinglePoint2525Renderer.RENDERER_ID;
                                }
                                
				String pluginSymbolID = null;

				ISinglePointInfo spi = null;
				if (plugins != null && plugins.hasRenderer(renderer)) {
					int questionIndex = symbolCode.lastIndexOf('?');
                                        if(questionIndex == -1)
                                            pluginSymbolID = java.net.URLDecoder.decode(symbolCode, "UTF-8");
                                        else
                                             pluginSymbolID = java.net.URLDecoder.decode(symbolCode.substring(0, questionIndex), "UTF-8");

					// try to render with plugin
					//System.out.println("plugin " + renderer + ": " + pluginSymbolID);
					spi = plugins.render(renderer, pluginSymbolID, params);
				}
                                //if plugin failed, default to 2525C plugin.
                                if(spi==null || (spi!=null && spi.getImage()==null))
                                {
                                    spi = plugins.render(SinglePoint2525Renderer.RENDERER_ID, pluginSymbolID, params);
                                }

				ImageInfo iInfo = null;

				if (spi != null && spi.getImage() != null) 
                                { // if plugin render success, 
                                    //create image info with result
					iInfo = new ImageInfo(spi.getImage(), 0, 0, (int) spi.getSymbolCenterPoint().getX(), (int) spi
							.getSymbolCenterPoint().getY(), spi.getSymbolBounds());
				} 

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                PNGInfo pi = null;
                                if (params.containsKey("ICON") && Boolean.parseBoolean(params.get("ICON"))==true)
                                {   //center image so you don't have to worry about getting 
                                    //the center point.
                                    BufferedImage bit = null;
                                    pi = new PNGInfo(iInfo);
                                    bit = pi.squareImage().getImage();
                                    ImageIO.write(bit, "png", baos);
                                }
                                else if (params.containsKey("BUFFER"))
                                {
                                    int eWidth = 0;
                                    int eHeight = 0;
                                    int ecX = 0;
                                    int ecY = 0;
                                    int buffer = 0;
                                    
                                    if(params.containsKey("EWIDTH"))
                                    {
                                        eWidth = Math.round(Float.parseFloat(params.get("EWIDTH")));
                                    }
                                    if(params.containsKey("EHEIGHT"))
                                    {
                                        eHeight = Math.round(Float.parseFloat(params.get("EHEIGHT")));
                                    }
                                    if(params.containsKey("ECENTERX"))
                                    {
                                        ecX = Math.round(Float.parseFloat(params.get("ECENTERX")));
                                    }
                                    if(params.containsKey("ECENTERY"))
                                    {
                                        ecY = Math.round(Float.parseFloat(params.get("ECENTERY")));
                                    }
                                    if(params.containsKey("BUFFER"))
                                    {
                                        buffer = Integer.parseInt(params.get("BUFFER"));
                                    }
                                    
                                    pi = new PNGInfo(iInfo);
                                    if(eWidth > 0 && eHeight > 0 && ecX > 0 && ecY > 0 && buffer > 0)
                                    {
                                        pi = pi.fitImage(eWidth, eHeight, ecX, ecY, buffer);
                                        ImageIO.write(pi.getImage(), "png", baos);
                                    }
                                    else
                                    {
                                        BufferedImage image = iInfo.getImage();
					ImageIO.write(image, "png", baos);
                                    }
                                }
                                else 
                                {
                                    pi = new PNGInfo(iInfo);
                                    boolean center = false;
                                    boolean meta = false;
                                    if (params.containsKey("CENTER"))
                                    {
                                        center = Boolean.parseBoolean(params.get("CENTER"));
                                    }
                                    if(params.containsKey("META"))
                                    {
                                        meta = Boolean.parseBoolean(params.get("META"));
                                    }
                                        
                                    //center image so you don't have to worry about getting 
                                    //the center point.
                                    if(center)
                                        pi = pi.centerImage();
                                    
                                    if (meta) 
                                    {
					// PNG with metadata takes about 8-9 milisecondsi
                                        byteArray = pi.getImageAsByteArrayWithMetaInfo();
                                    }
                                    else 
                                    {
                                        // regular PNG, takes about 5-6 miliseconds
                                        byteArray = pi.getImageAsByteArray();
                                    }
                                }
                                
                                
                                if(byteArray == null)
                                {
                                    // Send to Byte Array
                                    baos.flush();
                                    byteArray = baos.toByteArray();
                                    baos.close();
                                }

				/*
				 * //cleanup symbol = null; pConverter = null; image = null;//
				 */
			} catch (Exception exc) {
				// System.out.println(exc.getMessage());\
				ErrorLogger.LogException("SinglePointServer", "getSinglePointBytes", exc, Level.WARNING);
			}

			return byteArray;
		}

	}
        
}