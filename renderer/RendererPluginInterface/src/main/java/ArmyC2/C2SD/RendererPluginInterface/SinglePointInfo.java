package ArmyC2.C2SD.RendererPluginInterface;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * 
 * @author michael.spinelli
 */
public class SinglePointInfo implements ISinglePointInfo {

	private BufferedImage _Image = null;
	private Point2D _centerPoint = null;
	Rectangle2D _symbolBounds = null;

	public SinglePointInfo(BufferedImage bi) {
		_Image = bi;
		_centerPoint = new Point2D.Double(bi.getWidth() / 2, bi.getHeight() / 2);
		_symbolBounds = new Rectangle2D.Double(0, 0, bi.getWidth(), bi.getHeight());
	}

	public SinglePointInfo(BufferedImage bi, Point2D centerPoint, Rectangle2D symbolBounds) {
		_Image = bi;
		_centerPoint = centerPoint;
		_symbolBounds = symbolBounds;
	}

	@Override
	public BufferedImage getImage() {
		return _Image;
	}

	@Override
	public Rectangle2D getSymbolBounds() {
		return _symbolBounds;
	}

	@Override
	public Point2D getSymbolCenterPoint() {
		return _centerPoint;
	}

}
