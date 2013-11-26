package sec.web.renderer.services.ping;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PingServiceController {

	public PingServiceController() {
	}

	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	@ResponseBody
	public void ping(@Context HttpServletResponse response) {

		try {
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getOutputStream().println(response.getStatus());

//			response.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		return response.getStatus();
	}
}
