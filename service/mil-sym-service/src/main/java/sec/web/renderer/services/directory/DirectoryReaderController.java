package sec.web.renderer.services.directory;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import sec.web.renderer.services.directory.listeners.DirectoryWatcherVJ6;
import sec.web.renderer.utils.IoUtilities;

@Controller
public class DirectoryReaderController {

	public DirectoryReaderController() { }
	
	@RequestMapping(value="/pluginList", method = RequestMethod.GET)
	@ResponseBody
	public String getContent(@Context HttpServletRequest request, @Context HttpServletResponse response, @RequestParam(value="type", defaultValue="delimited") String type) {
		String result = "";
		String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI().substring(1), request.getContextPath());

		try {
			/* Comma delimited String */
			String dirContents = ""; //= utils.getContentAsString(',');
			
			ArrayList<String> fileList = DirectoryWatcherVJ6.scheduler.getFileNames();
					
			if (type.equalsIgnoreCase("delimited")) {
				dirContents = IoUtilities.buildUrlDelimitedList(fileList, ',', baseUrl);
			} else {
				dirContents = IoUtilities.buildJsonUrlList(fileList, baseUrl);
			}

			if (!"".equals(dirContents) || dirContents != null) {
				result = dirContents;
			}
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}

		return result;
	}
	
	@RequestMapping(value="/message/{msg}", method = RequestMethod.GET)
	@ResponseBody
	public String getMessage(@PathVariable String msg) {

		return msg;
	}
}
