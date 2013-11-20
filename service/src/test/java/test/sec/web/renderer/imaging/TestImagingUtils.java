package test.sec.web.renderer.imaging;

import static org.junit.Assert.*;
import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import sec.web.renderer.services.imaging.ImageGeneratorServlet;
import sec.web.renderer.utils.ImagingUtils;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

@SuppressWarnings("unused")
public class TestImagingUtils {
	
	protected static ImagingUtils utils = null;	
	protected String servletURL = "http://test.meterware.com/ImageGeneratorServlet";
	
	@BeforeClass
	public static void init()  {
		utils = new ImagingUtils();
	}
	
	@AfterClass
	public static void destroy() throws IOException {
		
	}

//	@Test
//	public void getAllQueryParamsTest() throws MalformedURLException, IOException, SAXException {		
//		ServletRunner sr = new ServletRunner();
//		sr.registerServlet("ImageGeneratorServlet", ImageGeneratorServlet.class.getName());
//		
//		ServletUnitClient client = sr.newClient();
//		WebRequest  request  = new GetMethodWebRequest(servletURL);
//		request.setParameter("t", "unique designation");
//		request.setParameter("h", "additional info");
//		request.setParameter("n", "ENY");
//		
//		WebResponse response = sr.getResponse(request);
//		
//		assertNotNull("no response received", response);
//		
//		String content = response.getText();
//		System.out.println(content);
//		
//		assertTrue(content, content.contains("param2"));
//	}
	
	
//	@Test
//	public void testUtilsPNG() {
//		String url = "/ImageGeneratorServlet/image/123456789012345";
//		String queryStr = "t=unique+designation&h=additional+info&n=ENY";
//		byte[] pngResp = ImagingUtils.getMilStd2525Png(url, queryStr);			
//	}

}
