package sec.web.renderer.services.imaging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sec.web.renderer.utils.ImagingUtils;

/**
 * Servlet implementation class DirectoryReader
 */
@WebServlet(name = "sec-image-generator-servlet", urlPatterns = { "/kml/symbolID" })
public class ImageGeneratorServlet extends HttpServlet {
	private static final long serialVersionUID = 851369047318854091L;
	private static final Logger LOGGER = Logger.getLogger(ImageGeneratorServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ImageGeneratorServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OutputStream os = response.getOutputStream();

		try {
			response.setContentType("image/png");
			
			byte[] pngResponse = ImagingUtils.getMilStd2525Png(request.getRequestURI(), request.getQueryString());

			if (pngResponse == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				throw new Exception();
			}

			os.write(pngResponse);
			os.close();

		} catch (Exception exc) {
			LOGGER.log(Level.SEVERE, this.getClass().getName() + exc.getMessage());			
			response.setContentType("text/plain");
			PrintWriter out = response.getWriter();		
			out.println(genHtmlErrorPage(request.getContextPath()));
			out.close();
		}
	}
	
	private String genHtmlErrorPage(String ctxPath) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html>");
			sb.append("<head>");
				sb.append("<title>503</title>");
			sb.append("</head>");
			
			sb.append("<body>");
				sb.append("<h1>Servlet SinglePoint at " + ctxPath + "</h1>");
				sb.append("<BR>url: " + "test" + "<BR>");
			sb.append("</body>");
		sb.append("</html>");
		
		
		return sb.toString();
	}
}
