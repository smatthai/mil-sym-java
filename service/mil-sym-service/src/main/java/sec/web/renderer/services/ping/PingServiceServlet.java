package sec.web.renderer.services.ping;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DirectoryReader
 */
@WebServlet(name = "sec-ping-service-servlet", urlPatterns = { "/ping" })
public class PingServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1798608294319568590L;
	private static final Logger LOGGER = Logger.getLogger(PingServiceServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PingServiceServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletOutputStream os = response.getOutputStream();
		LOGGER.log(Level.INFO, "ping:\t" + Calendar.getInstance().getTime());

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		String pageResult = genHtmlPage(response.getStatus());

		os.println(pageResult);
		os.close();		
	}
	
	private String genHtmlPage(int statusCode) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html>");
			sb.append("<head>");
				sb.append("<title>Mil-Symbology-Renderer Availability Page</title>");
				sb.append("<style>");
					sb.append("h1 {");
						sb.append("font-size: 24px;");
						sb.append("font-family: Cambria;");
						sb.append("color: #458B00;");
						sb.append("text-shadow: 0px 2px 3px #555;");
						sb.append("text-align: center;");
						sb.append("margin-top: 50px;");
					sb.append("}");
				sb.append("</style>");
			sb.append("</head>");
			
			sb.append("<body>");
				sb.append("<h1>Service is up and running:\t" + statusCode + "</h1>");
			sb.append("</body>");
		sb.append("</html>");
		
		
		return sb.toString();
	}
}
