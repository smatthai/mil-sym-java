package ArmyC2.C2SD.RendererPluginInterface;

import java.util.Map;

/**
 * 
 * @author michael.spinelli
 */
public interface ISinglePointRenderer {

	/**
	 * Returns the unique ID of the renderer. This is used to identify which
	 * renderer is to be used by the renderer service for a given render call.
	 * 
	 * @return
	 */
	String getRendererID();

	/**
	 * Determines if the renderer can draw the symbol given the provided info.
	 * 
	 * @param symbolID
	 * @param params
	 * @return
	 */
	Boolean canRender(String symbolID, Map<String,String> params);

	/**
	 * Attempts to render the symbol given the provided info.
	 * 
	 * @param symbolID
	 * @param params
	 * @return
	 */
	ISinglePointInfo render(String symbolID, Map<String,String> params);

}
