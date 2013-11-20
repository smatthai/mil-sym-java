package sec.web.renderer.services.imaging;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
import sec.web.renderer.utilities.PNGInfo;
import sec.web.renderer.utils.ImagingUtils;
import sec.web.renderer.utils.MultiPointUtils;

@Controller
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

	@RequestMapping(value="/{type}/{symbolId}", method = RequestMethod.GET, produces="image/png", headers="Accept=text/html,image/png")
	@ResponseBody	
	public void getContent(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathVariable("type") String type, @PathVariable("symbolId") String symbolId) throws Exception {
		byte[] png = null;
                String kml = "";
		switch (RenderingDataEnums.fromString(type)) {
		case IMAGE:
			response.setContentType("image/png");
			// response.setHeader("Content-Type", "image/png");

			png = ImagingUtils.getMilStd2525Png(symbolId, ImagingUtils.getURLParameters(request));
			break;

		case KML:
			kml = ImagingUtils.getKmlString(request.getRequestURL().toString(), symbolId, ImagingUtils.getURLParameters(request));
			break;

		default:
			break;
		}
                if(png != null)//write image data
                {
                    response.getOutputStream().write(png);
                    response.getOutputStream().close();
                }
                else //write kml response
                {
                    response.setContentType("application/xml");
                    boolean gzip = false;
                    String acceptEncoding = request.getHeader("Accept-Encoding");
                    //System.out.println(String.valueOf(acceptEncoding));
                    if(acceptEncoding != null && acceptEncoding.contains("gzip"))
                        gzip = true;
                    //disable compression for now. maybe use when we create a batch call.
                    gzip = false;
                    if(gzip==true)
                    {
                        kml = compress(kml);
                        response.setHeader("Content-Length", Integer.toString(kml.length()));
                        response.setHeader("Content-Encoding", "gzip");
                    }//*/


                    PrintWriter out = response.getWriter();
                    out.println(kml);
                    out.close();
                }
	}
	
	@RequestMapping(value = "/{type}/{symbolId}", method = RequestMethod.POST, headers = "Accept=text/html,text/plain,application/xml,application/json")
	@ResponseBody
	public void getMultiPointGraphic(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathVariable("type") String type, @PathVariable("symbolId") String symbolId) throws Exception {
		String kml = null;

		try {
                        /*
                        System.out.println("Printing Request Parameter Map:");
                        printParamMap(request.getParameterMap());
                        System.out.println("\n");
                        System.out.println("Printing Query String:");
                        System.out.println(request.getQueryString());
                        System.out.println("\n");//*/
                    
			switch (RenderingDataEnums.fromString(type)) {

			case MP3D:
				kml = MultiPointUtils.RenderSymbol(symbolId, ImagingUtils.getURLParameters(request));
				break;
                        case MP2D:
				kml = MultiPointUtils.RenderSymbol2D(symbolId, ImagingUtils.getURLParameters(request));
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
                        //System.out.println(String.valueOf(acceptEncoding));
                        if(acceptEncoding != null && acceptEncoding.contains("gzip"))
                            gzip = true;
                        
                        if(gzip==true)
                        {
                            kml = compress(kml);
                            response.setHeader("Content-Length", Integer.toString(kml.length()));
                            response.setHeader("Content-Encoding", "gzip");
                        }//*/
                        
                        
			PrintWriter out = response.getWriter();
			out.println(kml);
			out.close();

		} catch (Exception exc) {
			System.err.println(exc.getMessage());
			exc.printStackTrace();
		}
	}
        
        @RequestMapping(value = "/{type}", method = RequestMethod.POST, headers = "Accept=text/html,text/plain,application/xml,application/json")
	@ResponseBody
	public void getSinglePointBatchInfo(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathVariable("type") String type) throws Exception {
		String json = null;
                Map<String,String> params = null;

		try {
                        /*
                        System.out.println("Printing Request Parameter Map:");
                        printParamMap(request.getParameterMap());
                        System.out.println("\n");
                        System.out.println("Printing Query String:");
                        System.out.println(request.getQueryString());
                        System.out.println("\n");//*/
                    
			switch (RenderingDataEnums.fromString(type)) {

                        case SPBI:
                                params = ImagingUtils.getURLParameters(request);
                                if(params != null && params.containsKey("ICONURLS"))
                                {
                                    //System.out.println("getSPBI");
                                    //System.out.println("request: " + params.get("ICONURLS"));
                                    json = getSinglePointInfoBatch(params.get("ICONURLS"));
                                    //System.out.println(json);
                                }
                                else
                                {
                                    json = "";
                                }
                                break;

			default:
				break;
			}

			
                        response.setContentType("application/json");
			

                        
                        boolean gzip = false;
                        String acceptEncoding = request.getHeader("Accept-Encoding");
                        //System.out.println(String.valueOf(acceptEncoding));
                        if(acceptEncoding != null && acceptEncoding.contains("gzip"))
                            gzip = true;
                        
                        if(gzip==true)
                        {
                            json = compress(json);
                            response.setHeader("Content-Length", Integer.toString(json.length()));
                            response.setHeader("Content-Encoding", "gzip");
                        }//*/
                        
                        
			PrintWriter out = response.getWriter();
			out.println(json);
			out.close();

		} catch (Exception exc) {
			System.err.println(exc.getMessage());
			exc.printStackTrace();
		}
	}
	
	
	
	@RequestMapping(value="/image/message/{msg}", method = RequestMethod.GET)
	@ResponseBody
	public String getMessage(@PathVariable String msg) {
		System.out.println(msg + "\n\n");
		
		return msg;
	}
	
	@RequestMapping(value="/msgs/{msg}", method = RequestMethod.GET)
	@ResponseBody
	public String getMessage(HttpServletRequest request, @PathVariable String msg, @RequestParam(value="t", required=false, defaultValue = EMPTY_STRING) String imgCde) {
		System.out.println(msg + "\n\n");
		System.out.println(imgCde + "\n\n");
		
		String queryString = request.getQueryString();
		System.out.println(queryString);
		
		return msg;
	}

        /**
         * Compress a regular string into a GZIP compressed string.
         * @param str
         * @return 
         */
        private String compress(String str)
        {
            if(str == null || str.length()==0)
            {
                return str;
            }
            String outStr = null;
            try
            {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                GZIPOutputStream gzip = new GZIPOutputStream(out);
                gzip.write(str.getBytes());
                gzip.close();
                outStr = out.toString("ISO-8859-1");
            }
            catch(Exception exc)
            {
                
            }
            return outStr;
        }
        
        /**
         * Decompress a gzip copmressed string into a regular string.
         * @param str
         * @return 
         */
        private String decompress(String str)
        {
            if(str == null || str.length()==0)
            {
                return str;
            }
            String outStr = null;
            try
            {
                GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str.getBytes("ISO-8859-1")));
                BufferedReader bf = new BufferedReader(new InputStreamReader(gis,"ISO-8859-1"));
                outStr = "";
                String line = null;
                while((line=bf.readLine())!=null)
                {
                    outStr += line;
                }
                return outStr;
            }
            catch(Exception exc)
            {
                
            }
            return outStr;
        }
        
        private void printParamMap(Map<String,String[]> map)
        {

            String message = "";
            if(map != null)
            {
                for(Map.Entry<String,String[]> entry: map.entrySet())
                {
                    String key = entry.getKey();
                    String[] val = entry.getValue();

                    message += key + " : ";
                    for(String foo : val)
                    {
                        message += foo + " ";
                    }
                    message += "\n";
                }
            }
            System.out.println(message);
            //ErrorLogger.LogMessage(message);
        }
        
     /**
     * Given a symbol code meant for a single point symbol, returns the
     * anchor point at which to display that image based off the image returned
     * from the URL of the SinglePointServer.
     * @param batch like {"iconURLs":["SFGP------*****?size=35&T=Hello","SHGPE-----*****?size=50"]}
     * @return like {"singlepoints":[{"x":0,"y":0,"boundsx":0,"boundsy":0,"boundswidth":35,"boundsheight":35,"iconwidth":35,"iconwidth":35}, ... ]}
     */
    public String getSinglePointInfoBatch(String batch)
    {
        String info = "";
        Point2D anchor = new Point2D.Double();
        Rectangle2D symbolBounds = new Rectangle2D.Double();
        Dimension2D iconSize = new Dimension();
        JSONObject symbolInfo = null;
        StringBuilder sb = new StringBuilder();
        try
        {

            //must escape '=' so that JSONObject can parse string
            batch = batch.replaceAll("=", "%3D");
            
            String data = null;
            JSONObject jsonSPString = new JSONObject(batch);
            JSONArray jsa = jsonSPString.getJSONArray("iconURLs");
            int len = jsa.length();
            sb.append("{\"singlepoints\":[");
            String item = null;
            for(int i = 0; i < len; i++)
            {
                if(i>0)
                {
                    sb.append(",");
                }
                
                info = jsa.get(i).toString();
                //System.out.println(info);
                //info = java.net.URLDecoder.decode(info, "UTF-8");
                info = info.replaceAll("%3D", "=");
                //System.out.println(info);
                anchor = new Point2D.Double();
                symbolBounds = new Rectangle2D.Double();
                //System.out.println("url: " + info);
                PNGInfo pngInfo = SECRenderer.getInstance().getSymbolImageFromURL(info);
                
                iconSize = new Dimension(pngInfo.getImage().getWidth(), pngInfo.getImage().getHeight());
                item = SECWebRenderer.SymbolDimensionsToJSON(pngInfo.getCenterPoint(), 
                        pngInfo.getSymbolBounds(), iconSize);
                sb.append(item);
            }
            
        }
        catch(Exception exc)
        {
            System.out.println(exc.getMessage());
            exc.printStackTrace();
        }
        finally
        {
            sb.append("]}");
        }
        return sb.toString();
    }
}
