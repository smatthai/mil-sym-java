package ArmyC2.C2SD.RendererPluginInterface;

import org.apache.batik.svggen.SVGGraphics2D;

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
	 * Anchor point of the symbol within the image. Typically the center
         * but not always.
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

	SVGGraphics2D getSvgGraphics();
	void setSvgGraphics(SVGGraphics2D svgGraphics);
}
