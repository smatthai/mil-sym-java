package sec.web.renderer.utilities;

import ArmyC2.C2SD.RendererPluginInterface.ISinglePointInfo;
import ArmyC2.C2SD.Utilities.ErrorLogger;
import ArmyC2.C2SD.Utilities.ImageInfo;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Node;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.Iterator;

public class SVGInfo {

    private Point2D centerPoint;
    private Rectangle2D symbolBounds;
    private BufferedImage image;
    private SVGGraphics2D svgGraphics;

    public SVGInfo(ImageInfo ii) {
        this.centerPoint = new Point2D.Double(ii.getSymbolCenterX(), ii.getSymbolCenterY());
        this.symbolBounds = ii.getSymbolBounds();
        this.image = ii.getImage();
    }

    public SVGInfo(ISinglePointInfo spi) {
        this.centerPoint = spi.getSymbolCenterPoint();
        this.symbolBounds = spi.getSymbolBounds();
        this.image = spi.getImage();
        this.svgGraphics = spi.getSvgGraphics();
    }

    public SVGInfo(BufferedImage image, Point2D centerPoint, Rectangle2D symbolBounds) {
        this.image = image;
        this.symbolBounds = symbolBounds;
        this.centerPoint = centerPoint;
    }

    public SVGInfo(BufferedImage image, Point2D centerPoint, Rectangle2D symbolBounds, SVGGraphics2D svgGraphics2D) {
        this.image = image;
        this.symbolBounds = symbolBounds;
        this.centerPoint = centerPoint;
        this.svgGraphics = svgGraphics2D;
    }

    /**
     * Center point of the symbol within the image.
     * With the exception of HQ where the symbol is centered on the bottom
     * of the staff.
     *
     * @return
     */
    public Point2D getCenterPoint() {
        return centerPoint;
    }

    /**
     * minimum bounding rectangle for the core symbol. Does
     * not include modifiers, display or otherwise.
     *
     * @return
     */
    public Rectangle2D getSymbolBounds() {
        return symbolBounds;
    }

    public BufferedImage getImage() {
        return image;
    }

    /**
     * returns the image as a byte[] representing a PNG.
     *
     * @return
     */
    public byte[] getImageAsByteArray() {
        byte[] byteArray = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // BufferedImage image = image;
            ImageIO.write(image, "png", baos);
            //Send to Byte Array
            baos.flush();
            byteArray = baos.toByteArray();
            baos.close();
        } catch (Exception exc) {
            ErrorLogger.LogException("SVGInfo", "getImageAsByteArray", exc);
        }
        return byteArray;
    }

    /**
     * Save image to a file as a PNG
     *
     * @param filePath full path to the file
     * @return true on success.
     */
    public Boolean saveImageToFile(String filePath) {
        try {
            return ImageIO.write(image, "png", new File(filePath));
        } catch (Exception exc) {
            ErrorLogger.LogException("ImageInfo", "SaveImageToFile", exc);
            return false;
        }
    }

    /**
     * Unlike SaveImageToFile, this only writes to PNGs and it includes
     * positional metadata in the PNG.  Entered as tEXtEntry elements of tEXt
     * metadata keywords are "centerPoint" and
     * "bounds". Values formatted as "x=#,y=#" and "x=#,y=#,width=#,height=#"
     * Bounds is the MBR of the symbol and does not include any modifiers.
     * @return true on success.
     */
    public byte[] getImageAsByteArrayWithMetaInfo() {
        byte[] metaImage = null;
        try {

            RenderedImage image = (RenderedImage) this.image;
            Iterator<ImageWriter> itr = ImageIO.getImageWritersBySuffix("png");
            String metaDataFormatName = "";

            if (itr.hasNext()) {
                ImageWriter iw = itr.next();
                IIOMetadata meta = iw.getDefaultImageMetadata(new ImageTypeSpecifier(image), null);

                //create & populate metadata
                metaDataFormatName = meta.getMetadataFormatNames()[0];
                StringBuilder XML = new StringBuilder("");
                XML.append("<" + metaDataFormatName + ">");//"</javax_imageio_png_1.0>"
                XML.append("<tEXt>");
                //XML.append("<tEXtEntry keyword=\"symbolCenterX\" value=\"" + String.valueOf(_symbolCenterX)+"\"/>");
                XML.append("<tEXtEntry keyword=\"centerPoint\" value=\"" + "x=" + String.valueOf(this.centerPoint.getX()) +
                        ",y=" + String.valueOf(this.centerPoint.getY()) + "\"/>");

                XML.append("<tEXtEntry keyword=\"bounds\" value=\"" + "x=" + String.valueOf(symbolBounds.getX()) +
                        ",y=" + String.valueOf(symbolBounds.getY()) +
                        ",width=" + String.valueOf(symbolBounds.getWidth()) +
                        ",height=" + String.valueOf(symbolBounds.getHeight()) + "\"/>");

                XML.append("<tEXtEntry keyword=\"imageExtent\" value=\"" +
                        "width=" + String.valueOf(image.getWidth()) +
                        ",height=" + String.valueOf(image.getHeight()) + "\"/>");

                XML.append("</tEXt>");
                XML.append("</" + metaDataFormatName + ">");//"</javaximageio_png_1.0>"

                //ErrorLogger.LogMessage(XML.toString());
                DOMResult domresult = new DOMResult();
                TransformerFactory.newInstance().newTransformer().transform(new StreamSource(new StringReader(XML.toString())), domresult);
                Node document = domresult.getNode();

                //apply metadata
                meta.mergeTree(metaDataFormatName, document.getFirstChild());

                //Render PNG to Memory
                IIOImage iioImage = new IIOImage(image, null, null);
                iioImage.setMetadata(meta);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                ImageOutputStream ios = ImageIO.createImageOutputStream(bytes);

                iw.setOutput(ios);
                iw.write(null, iioImage, null); //iw.write(metadata, iioImage, null);
                ios.close();

                iw.dispose();
                iw = null;
                itr = null;
                iioImage = null;

                bytes.flush();
                metaImage = bytes.toByteArray();
                bytes.close();

                return metaImage;
            } else {
                ErrorLogger.LogMessage("ImageInfo", "getImageAsByteArrayWithMetaInfo", "no PNG imageWriter available");
                return null;
            }

        } catch (Exception exc) {
            ErrorLogger.LogException("ImageInfo", "getImageAsByteArrayWithMetaInfo", exc);
            return null;
        }

    }

    /**
     * String representation of the svg.
     *
     * @return string r
     */
    public String toSVGAsString() {
        return new String(toSVG());
    }

    /**
     * String representation of the svg.
     *
     * @return string r
     */
    public byte[] toSVG() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        OutputStreamWriter osw = new OutputStreamWriter(baos);
        try {
            this.getSvgGraphics().stream(osw);
            osw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(osw);
            this.getSvgGraphics().dispose();
        }

        return baos.toByteArray();
    }

    public void setCenterPoint(Point2D centerPoint) {
        this.centerPoint = centerPoint;
    }

    public void setSymbolBounds(Rectangle2D symbolBounds) {
        this.symbolBounds = symbolBounds;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public SVGGraphics2D getSvgGraphics() {
        return svgGraphics;
    }

    public void setSvgGraphics(SVGGraphics2D svgGraphics) {
        this.svgGraphics = svgGraphics;
    }

    /**
     * This method will wrap PNG images that do not contain vector graphics.
     * This will mostly be needed for supporting 3rd party plugins without graphics data.
     *
     * @param drawMode 0 - normal, 1 - center, 2 - square
     * @return
     */
    public String wrapImageAsSVG(int drawMode) {
        String svg = "<svg></svg>";
        if (image != null) {
            int x = 0;
            int y = 0;
            int width = image.getWidth();
            int height = image.getHeight();
            int svgWidth = width;
            int svgHeight = height;

            if (width > 0 && height > 0) {
                String b64 = "data:image/png;base64," + Base64.encode(getImageAsByteArray());

                // Normal
                if (drawMode == 0) {
                    svg = "<svg version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
                            "width=\"" + width +
                            "\" height=\"" + height +
                            "\"><image x=\"0\" y=\"0\"" +
                            " width=\"" + width +
                            "\" height=\"" + height +
                            "\" xlink:href=\"" + b64 +  "\" /></svg>";

                } else if (drawMode == 1) { /*center*/
                    if (centerPoint.getY() > svgHeight - centerPoint.getY()) {
                        svgHeight = (int)(centerPoint.getY() * 2.0);
                        y = 0;
                    } else {
                        svgHeight = (int)((svgHeight - centerPoint.getY()) * 2);
                        y = (int)((svgHeight / 2) - centerPoint.getY());
                    }

                    if (centerPoint.getX() > svgWidth - centerPoint.getX()) {
                        svgWidth = (int)(centerPoint.getX() * 2.0);
                        x = 0;
                    } else {
                        svgWidth = (int)((svgWidth - centerPoint.getX()) * 2);
                        x = (int)((svgWidth / 2) - centerPoint.getX());
                    }

                    svg = "<svg version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
                            "width=\"" + svgWidth +
                            "\" height=\"" + svgHeight +
                            "\"><image x=\"" + x + "\" y=\"" + y + "\"" +
                            " width=\"" + width +
                            "\" height=\"" + height +
                            "\" xlink:href=\"" + b64 +  "\" /></svg>";

                } else if (drawMode == 2)  { /*Square*/
                    int newSize = svgHeight;
                    if (svgWidth > svgHeight) {
                        newSize = width;
                    }

                    if( svgWidth < newSize) {
                        x = (int) ((newSize - svgWidth) / 2.0);
                    }

                    if (svgHeight < newSize) {
                        y = (int) ((newSize - svgHeight) / 2.0);
                    }

                    svg = "<svg version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
                            "width=\"" + newSize +
                            "\" height=\"" + newSize +
                            "\"><image x=\"" + x + "\" y=\"" + y + "\"" +
                            " width=\"" + width +
                            "\" height=\"" + height +
                            "\" xlink:href=\"" + b64 +  "\" /></svg>";
                }
            }
        }
        return svg;
    }
}
