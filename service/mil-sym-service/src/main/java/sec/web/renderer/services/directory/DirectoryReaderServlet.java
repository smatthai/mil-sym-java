package sec.web.renderer.services.directory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sec.web.renderer.utils.DirectoryReaderUtils;

/**
 * Servlet implementation class DirectoryReader
 */
@WebServlet(name = "sec-directory-reader", urlPatterns = { "/directoryReader" })
public class DirectoryReaderServlet extends HttpServlet {
	private static final long serialVersionUID = 534810784725663803L;
	private static final Logger LOGGER = Logger.getLogger(DirectoryReaderServlet.class.getName());
	private Properties props;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DirectoryReaderServlet() {
		super();

		props = DirectoryReaderUtils.loadProperties("properties/prop.properties", this.getClass().getClassLoader());		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unused")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("application/json");
			PrintWriter writer = response.getWriter();
			String urlStr = buildURL(request.getServerName(), String.valueOf(request.getServerPort()));

			StringBuilder filePath = new StringBuilder();
			filePath.append(props.getProperty("directoryPath"));
			filePath.append(File.separator);
			filePath.append(props.getProperty("directoryName"));
			filePath.append(File.separator);

			/* Comma delimited String */
			String contents = DirectoryReaderUtils.getDirectoryContent(new File(filePath.toString()));
			
			writer.println(contents);
			
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, e.getMessage());
		}	
	}

	private String buildURL(String serverName, String port) {
		String urlStr = props.getProperty("protocol") + "://" +  
						serverName + ":" +
				        port + "/" +		  
						props.getProperty("directoryName");
		return urlStr;
	}
	

	protected String makeHtmlResponse(List<String> contents) {
		StringBuilder sb = new StringBuilder();
		sb.append("<h2>Content of Resources folder</h2><br />");
		sb.append("<ul>");
		for (String s : contents) {
			sb.append("<li>");
				sb.append("<a href=" + s + ">");
					sb.append(s);
				sb.append("</a>");
			sb.append("</li>");
		}
		sb.append("</ul>");

		return sb.toString();
	}
}
