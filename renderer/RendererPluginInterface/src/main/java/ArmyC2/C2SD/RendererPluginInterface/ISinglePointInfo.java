package ArmyC2.C2SD.RendererPluginInterface;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * 
 * @author michael.spinelli
 */
public interface ISinglePointInfo {

	/**
	 * The BufferedImage
	 * 
	 * @return the actual image
	 */
	public BufferedImage getImage();

	/**
	 * Center point of the symbol within the image. OR, if the symbol should be
	 * centered on a point that is not the actual center of the image, you'd
	 * return that pixel value here.
	 * 
	 * @return
	 */
	public Point2D getSymbolCenterPoint();

	/**
	 * minimum bounding rectangle for the core symbol. Does not include labels
	 * surrounding the symbol.
	 * 
	 * @return
	 */
	public Rectangle2D getSymbolBounds();

}
