package test.sec.web.renderer.imaging;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;
import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;

import sec.web.renderer.services.imaging.ImageGeneratorController;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;


//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration( locations = { "test-dispatch-servlet.xml" })
@SuppressWarnings("unused")
public class SpringImagingTests {

	private String BASE_URL = "http://localhost:8080/rendering-sdk/renderer";
	private String KML_URL  = "/kml/SFGP-----------";   // ?name=testKML&description=globeView&lat=-39&lon=-78&alt=25000&id=test1
	private String IMG_URL  = "/image/SFGP-----------"; // ?T=uniquedesignation&n=ENY&h=USA"
	
	
//	@Test
//	public void testMe() {
//		RestTemplate rest = new RestTemplate();
//		
//		rest.getForObject(BASE_URL, new Object[]{});
//	}
	
//	@Test
//	public void getImagingKMLTest() throws MalformedURLException, IOException, SAXException {		
//		ServletRunner sr = new ServletRunner();
//		sr.registerServlet("ImageGeneratorController", ImageGeneratorController.class.getName());
//		
//		ServletUnitClient client = sr.newClient();
//		WebRequest  request  = new GetMethodWebRequest(BASE_URL+KML_URL);
//		request.setParameter("name", "unique designation");
//		request.setParameter("description", "additional info");
//		request.setParameter("lat", "-39");
//		request.setParameter("lon", "-78");
//		request.setParameter("alt", "25000");
//		
//		WebResponse response = sr.getResponse(request);
//		WebResponse resp     = client.getResponse(request);
//		
//		assertNotNull("no response received", response);
//		assertNotNull("no response received", resp);
//		
//		String content = response.getText();
//		System.out.println(content);
//		
//		assertTrue(content, content.contains("name"));
//	}
	
	
//	
//	@Test
//	public void getImagingImageTest() throws MalformedURLException, IOException, SAXException {		
//		ServletRunner sr = new ServletRunner();
//		sr.registerServlet("ImageGeneratorController", ImageGeneratorController.class.getName());
//		
//		ServletUnitClient client = sr.newClient();
//		WebRequest  request  = new GetMethodWebRequest(BASE_URL+IMG_URL);
//		request.setParameter("name", "unique designation");
//		request.setParameter("description", "additional info");
//		request.setParameter("lat", "-39");
//		request.setParameter("lon", "-78");
//		request.setParameter("alt", "25000");
//		
//		WebResponse response = sr.getResponse(request);
//		WebResponse resp     = client.getResponse(request);
//		
//		assertNotNull("no response received", response);
//		assertNotNull("no response received", resp);
//		
//		String content = response.getText();
//		System.out.println(content);
//		
//		assertTrue(content, content.contains("name"));
//	}
	
//	@Test
//	public void testUtilsPNG() {
//		String url = "/ImageGeneratorController/image/123456789012345";
//		String queryStr = "t=unique+designation&h=additional+info&n=ENY";
//		byte[] pngResp = ImagingUtils.getMilStd2525Png(url, queryStr);			
//	}

}
