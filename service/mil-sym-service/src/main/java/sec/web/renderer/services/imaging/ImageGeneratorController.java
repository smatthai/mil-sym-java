package sec.web.renderer.services.imaging;

import ArmyC2.C2SD.Utilities.MilStdAttributes;
import ArmyC2.C2SD.Utilities.SymbolUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import sec.web.json.utilities.JSONArray;
import sec.web.json.utilities.JSONObject;
import sec.web.renderer.SECRenderer;
import sec.web.renderer.SECWebRenderer;
import sec.web.renderer.model.RenderingDataEnums;
import sec.web.renderer.utilities.JavaRendererUtilities;
import sec.web.renderer.utilities.PNGInfo;
import sec.web.renderer.utils.ImagingUtils;
import sec.web.renderer.utils.MultiPointUtils;

@Controller
@SuppressWarnings("unused")
public class ImageGeneratorController {
	public final String EMPTY_STRING = "";
	
	public ImageGeneratorController() {
		try {
			SECRenderer.getInstance().printManifestInfo();
		} catch (Exception exc1) {
			System.err.println(exc1.getMessage());
			exc1.printStackTrace();
		}
		try {
			ImagingUtils.reloadPlugins();
		} catch (Exception exc2) {
			System.err.println(exc2.getMessage());
			exc2.printStackTrace();
		}
	}

	@RequestMapping(value = "/{type}/{symbolId}", method = RequestMethod.GET, produces = "image/png", headers = "Accept=text/html,image/png")
	@ResponseBody
	public void getContent(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathVariable("type") String type, @PathVariable("symbolId") String symbolId) throws Exception {
		byte[] png = null;
                PNGInfo pi = null;
		String kml = null;
                String svg = null;
                String svgz = null;
                int svgDrawMode = 0;
                Boolean isIcon = false;
                Map<String, String> params = null;
		switch (RenderingDataEnums.fromString(type)) {
		case IMAGE:
			response.setContentType("image/png");
			// response.setHeader("Content-Type", "image/png");

			png = ImagingUtils.getMilStd2525PngBytes(symbolId,
					ImagingUtils.getURLParameters(request));
			break;
                    
                case KML:
			kml = ImagingUtils.getKmlString(request.getRequestURL().toString(),
					symbolId, ImagingUtils.getURLParameters(request));
			break;
                    
                case SVG:
                        response.setContentType("image/svg+xml");
			// response.setHeader("Content-Type", "image/png");
                        params = ImagingUtils.getURLParameters(request);
                        
                        if(params.containsKey("ICON") && 
                                (Boolean.parseBoolean(params.get("ICON")) == true))
                        {
                            //svgDrawMode = 0;
                            params = JavaRendererUtilities.parseIconParameters(symbolId, params);
                            symbolId = JavaRendererUtilities.sanitizeSymbolID(symbolId);
                            isIcon = true;
                        }
                        
                        pi = ImagingUtils.getMilStd2525Png(symbolId, params);
                        
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
                        
                        svg = pi.toSVG(svgDrawMode);
			break;
                case SVGZ:
                        response.setContentType("image/svg+xml");
			// response.setHeader("Content-Type", "image/png");
                        params = ImagingUtils.getURLParameters(request);
                        
                         
			pi = ImagingUtils.getMilStd2525Png(symbolId, params);
                        
                        if(params.containsKey("ICON") && 
                                (Boolean.parseBoolean(params.get("ICON")) == true))
                        {
                            //svgDrawMode = 0;
                            params = JavaRendererUtilities.parseIconParameters(symbolId, params);
                            pi = pi.squareImage();
                        }
                        
                        if(params.containsKey("CENTER") && 
                                (Boolean.parseBoolean(params.get("CENTER")) == true))
                        {
                            svgDrawMode = 1;
                        }
                        else if(params.containsKey("SQAURE") && 
                                (Boolean.parseBoolean(params.get("SQAURE")) == true))
                        {
                            svgDrawMode = 2;
                        }
                        
                        svgz = pi.toSVG(svgDrawMode);//*/
			break;

		default:
			break;
		}
		if (png != null)// write image data
		{
			response.getOutputStream().write(png);
			response.getOutputStream().close();
		}
                else if(kml != null){ // write kml response
			
                    response.setContentType("application/xml");
                    boolean gzip = false;
                    String acceptEncoding = request.getHeader("Accept-Encoding");
                    // System.out.println(String.valueOf(acceptEncoding));
                    if (acceptEncoding != null && acceptEncoding.contains("gzip"))
                            gzip = true;
                    // disable compression for now. maybe use when we create a batch call.
                    gzip = false;
                    if (gzip == true) {
                            kml = compress(kml);
                            response.setHeader("Content-Length", Integer.toString(kml.length()));
                            response.setHeader("Content-Encoding", "gzip");
                    }// */

                    PrintWriter out = response.getWriter();
                    out.print(kml);
                    out.close();
		}
                else if(svg != null)
                {
                    response.setContentType("image/svg+xml");
                    PrintWriter out = response.getWriter();
                    out.print(svg);
                    out.close();
                }
                else if(svgz != null)
                {
                    response.setContentType("image/svg+xml");
                    boolean gzip = false;
                    String acceptEncoding = request.getHeader("Accept-Encoding");
                    
                    if (acceptEncoding != null && acceptEncoding.contains("gzip"))
                            gzip = true;

                    if (gzip == true) {
                            svgz = compress(svgz);
                            response.setHeader("Content-Length", Integer.toString(svgz.length()));
                            response.setHeader("Content-Encoding", "gzip");
                    }

                    PrintWriter out = response.getWriter();
                    out.print(svgz);
                    out.close();
                }
	}
	
	@RequestMapping(value = "/{type}/{symbolId}", method = RequestMethod.POST, headers = "Accept=text/html,text/plain,application/xml,application/json")
	@ResponseBody
	public void getMultiPointGraphic(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathVariable("type") String type, @PathVariable("symbolId") String symbolId) throws Exception {
		String kml = null;

		try {
			switch (RenderingDataEnums.fromString(type)) {

			case MP3D:
				kml = MultiPointUtils.RenderSymbol(symbolId,
						ImagingUtils.getURLParameters(request));
				break;
			case MP2D:
				kml = MultiPointUtils.RenderSymbol2D(symbolId,
						ImagingUtils.getURLParameters(request));
				break;
			default:
				break;
			}

			if (kml.startsWith("<")) {
				response.setContentType("application/xml");
			} else if (kml.startsWith("{")) {
				response.setContentType("application/json");
			}

			boolean gzip = false;
			String acceptEncoding = request.getHeader("Accept-Encoding");

			if (acceptEncoding != null && acceptEncoding.contains("gzip"))
				gzip = true;

			if (gzip == true) {
				kml = compress(kml);
				response.setHeader("Content-Length",
						Integer.toString(kml.length()));
				response.setHeader("Content-Encoding", "gzip");
			}// */

			PrintWriter out = response.getWriter();
			out.print(kml);
			out.close();

		} catch (Exception exc) {
			System.err.println(exc.getMessage());
			exc.printStackTrace();
		}
	}
        
	@RequestMapping(value = "/{type}", method = RequestMethod.POST, headers = "Accept=text/html,text/plain,application/xml,application/json")
	@ResponseBody
	public void getSinglePointBatchInfo(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathVariable("type") String type) throws Exception {
		String json = null;
		Map<String, String> params = null;

		try {
			switch (RenderingDataEnums.fromString(type)) {

			case SPBI:
				params = ImagingUtils.getURLParameters(request);
				if (params != null && params.containsKey("ICONURLS")) {
					json = getSinglePointInfoBatch(params.get("ICONURLS"));
				} else {
					json = "";
				}
				break;

			default:
				break;
			}

			response.setContentType("application/json");

			boolean gzip = false;
			String acceptEncoding = request.getHeader("Accept-Encoding");

			if (acceptEncoding != null && acceptEncoding.contains("gzip"))
				gzip = true;

			if (gzip == true) {
				json = compress(json);
				response.setHeader("Content-Length", Integer.toString(json.length()));
				response.setHeader("Content-Encoding", "gzip");
			}

			PrintWriter out = response.getWriter();
			out.println(json);
			out.close();

		} catch (Exception exc) {
			System.err.println(exc.getMessage());
			exc.printStackTrace();
		}
	}

        /**
         * Compress a regular string into a GZIP compressed string.
         * @param str
         * @return 
         */
	private String compress(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		String outStr = null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes());
			gzip.close();
			outStr = out.toString("ISO-8859-1");
		} catch (Exception exc) {

		}
		return outStr;
	}
        
        /**
         * Decompress a gzip copmressed string into a regular string.
         * @param str
         * @return 
         */
	private String decompress(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		String outStr = null;
		try {
			GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str.getBytes("ISO-8859-1")));
			BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "ISO-8859-1"));
			outStr = "";
			String line = null;
			while ((line = bf.readLine()) != null) {
				outStr += line;
			}
		} catch (Exception exc) {

		}
		return outStr;
	}
        
	private void printParamMap(Map<String, String[]> map) {
		String message = "";
		if (map != null) {
			for (Map.Entry<String, String[]> entry : map.entrySet()) {
				String key = entry.getKey();
				String[] val = entry.getValue();

				message += key + " : ";
				for (String foo : val) {
					message += foo + " ";
				}
				message += "\n";
			}
		}
		System.out.println(message);
	}
        
     /**
     * Given a symbol code meant for a single point symbol, returns the
     * anchor point at which to display that image based off the image returned
     * from the URL of the SinglePointServer.
     * @param batch like {"iconURLs":["SFGP------*****?size=35&T=Hello","SHGPE-----*****?size=50"]}
     * @return like {"singlepoints":[{"x":0,"y":0,"boundsx":0,"boundsy":0,"boundswidth":35,"boundsheight":35,"iconwidth":35,"iconwidth":35}, ... ]}
     */
	public String getSinglePointInfoBatch(String batch) {
		String info = "";
		Point2D anchor = new Point2D.Double();
		Rectangle2D symbolBounds = new Rectangle2D.Double();
		Dimension2D iconSize = new Dimension();
		JSONObject symbolInfo = null;
		StringBuilder sb = new StringBuilder();
		try {

			// must escape '=' so that JSONObject can parse string
			batch = batch.replaceAll("=", "%3D");

			String data = null;
			JSONObject jsonSPString = new JSONObject(batch);
			JSONArray jsa = jsonSPString.getJSONArray("iconURLs");
			int len = jsa.length();
			sb.append("{\"singlepoints\":[");
			String item = null;
			
			for (int i = 0; i < len; i++) {
				if (i > 0) {
					sb.append(",");
				}

				info = jsa.get(i).toString();
				info = info.replaceAll("%3D", "=");

				anchor = new Point2D.Double();
				symbolBounds = new Rectangle2D.Double();
				// System.out.println("url: " + info);
				PNGInfo pngInfo = SECRenderer.getInstance().getSymbolImageFromURL(info);

				iconSize = new Dimension(pngInfo.getImage().getWidth(), pngInfo.getImage().getHeight());
				item = SECWebRenderer.SymbolDimensionsToJSON(pngInfo.getCenterPoint(), pngInfo.getSymbolBounds(), iconSize);
				sb.append(item);
			}

		} catch (Exception exc) {
			System.out.println(exc.getMessage());
			exc.printStackTrace();
		} finally {
			sb.append("]}");
		}
		return sb.toString();
	}
}
