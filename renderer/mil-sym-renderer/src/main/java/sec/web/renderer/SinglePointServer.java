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

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.BindException;
import java.util.Map;

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
	private static int _instanceCount = 0;
	private HttpServer httpServer;
	private int portNumber = 8888;
	private SinglePointHandler _singlePointHandler = null;
        
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

	private void createHttpServer() {
		try {
			// A maximum backlog can be specified. This is the maximum number
			// of queued incoming connections to allow on the listening socket.
			// Queued connection above this limit may be rejected.
			// If set <=0, a system default value is used.

			int backlog = _backLog;
			httpServer = HttpServer.create(new InetSocketAddress("127.0.0.1", portNumber), backlog);
			_singlePointHandler = new SinglePointHandler();
			httpServer.createContext("/", _singlePointHandler);
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

	class SinglePointHandler implements HttpHandler {

		public SinglePointHandler() {
			initRenderer();
		}

		/**
		 * 
		 * @param symbolID
		 * @return
		 */
		public byte[] getSinglePointByteArray(String symbolID) {
			byte[] pngResponse = null;

			try {
				// symbolID = url.substring(url.lastIndexOf("/") + 1);
				// System.out.println(symbolID);
				pngResponse = getSinglePointBytes(symbolID);
				return pngResponse;
			} catch (Exception exc) {
				// System.err.println(exc.getMessage());
				ErrorLogger.LogException("SinglePointServer", "getSinglePointByteArray", exc, Level.WARNING);
				return null;
			}
		}

		public void handle(HttpExchange exchange) {
			/*
			 * try {
			 * ErrorLogger.LogMessage(exchange.getRequestURI().toString()); }
			 * catch(Exception exc) {
			 * ErrorLogger.LogException("SinglePointServer", "handle", exc); }
			 */
			/*
			 * if (exchange.getRequestURI().toString().equalsIgnoreCase(
			 * "/crossdomain.xml")) { try { String el =
			 * System.getProperty("line.separator"); Headers headers =
			 * exchange.getResponseHeaders(); headers.set("Content-Type",
			 * "text/plain");
			 * 
			 * StringBuilder sb = new StringBuilder(); StringBuilder sbALL = new
			 * StringBuilder(); sb.append(
			 * "<cross-domain-policy xsi:noNamespaceSchemaLocation=\"http://www.adobe.com/xml/schemas/PolicyFile.xsd\">"
			 * ); sb.append(el);
			 * sb.append("\t<allow-access-from domain=\"127.0.0.1\"/>");
			 * sb.append(el);
			 * sb.append("\t<allow-access-from domain=\"localhost\"/>");
			 * sb.append(el); //sb.append(
			 * "\t<site-control permitted-cross-domain-policies=\"master-only\"/>"
			 * ); //sb.append(el); sb.append(
			 * "\t<allow-http-request-headers-from domain=\"localhost\" headers=\"GET\" secure=\"false\"/>"
			 * ); sb.append(el); sb.append(
			 * "\t<allow-http-request-headers-from domain=\"127.0.0.1\" headers=\"GET\" secure=\"false\"/>"
			 * ); sb.append(el); sb.append("</cross-domain-policy>");
			 * 
			 * //sbALL.append("<?xml version=\1.0\"?>"); //sbALL.append(el);
			 * sbALL.append("<cross-domain-policy>"); sbALL.append(el);
			 * sbALL.append("\t<allow-access-from domain=\"*\"/>");
			 * sbALL.append(el); sbALL.append(
			 * "\t<site-control permitted-cross-domain-policies=\"master-only\"/>"
			 * ); sbALL.append(el); sbALL.append("</cross-domain-policy>");
			 * 
			 * 
			 * 
			 * String response = sbALL.toString(); byte[] bytes =
			 * response.getBytes();
			 * exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
			 * bytes.length); exchange.getResponseBody().write(bytes);
			 * exchange.getResponseBody().close(); } catch(Exception excCD) {
			 * ErrorLogger.LogException("SinglePointServer", "handle", excCD); }
			 * 
			 * 
			 * }
			 */

			if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
				String url = null;
				// String url = exchange.getRequestURI().getPath();
				// String url = exchange.getRequestURI().toString();

				byte[] pngResponse = null;

				try {
					url = exchange.getRequestURI().toString();
					String symbolID = url.substring(url.lastIndexOf("/") + 1);
					// System.out.println(symbolID);
					pngResponse = getSinglePointBytes(symbolID);

				} catch (Exception exc) {
					// System.err.println(exc.getMessage());
					ErrorLogger.LogException("SinglePointServer", "handle", exc, Level.WARNING);

					try {
						Headers headers = exchange.getResponseHeaders();
						headers.set("Content-Type", "text/plain");
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
		private byte[] getSinglePointBytes(String symbolCode) throws RendererException, IOException {
			byte[] byteArray = null;

			// This will get an instance of the renderer and initialize if it
			// hasn't
			// been initialized yet.
			initRenderer();
			// ////////////////////////////////////////////

			try {
                                Map<String,String> params = JavaRendererUtilities.createParameterMapFromURL(symbolCode);

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

				if (symbolCode.contains("meta=true")) {
					// PNG with metadata takes about 8-9 miliseconds
					iInfo.SaveImageToPNG(ImageIO.createImageOutputStream(baos));
				}
                                else if (symbolCode.contains("center=true"))
                                {   //center image so you don't have to worry about getting 
                                    //the center point.
                                    BufferedImage bit = null;
                                    bit = ImageInfo.CenterImageOnPoint(iInfo.getImage(), iInfo.getSymbolCenterPoint());
                                    ImageIO.write(bit, "png", baos);
                                }
                                else {
					// regular PNG, takes about 5-6 miliseconds
					BufferedImage image = iInfo.getImage();
					ImageIO.write(image, "png", baos);
				}

				// Send to Byte Array
				baos.flush();
				byteArray = baos.toByteArray();
				baos.close();

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
