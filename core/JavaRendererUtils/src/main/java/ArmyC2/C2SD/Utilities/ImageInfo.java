/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ArmyC2.C2SD.Utilities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.StringReader;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Node;

/**
 * Object that holds an image of the symbol and all the information
 * needed to place the symbol on the screen.
 * @author michael.spinelli
 */
public class ImageInfo {

     public static final String FormatPNG = "png";
    public static final String FormatJPG = "jpg";

    private BufferedImage _Image = null;
    private int _X = 0;
    private int _Y = 0;
    private int _symbolCenterX = 0;
    private int _symbolCenterY = 0;
    Rectangle2D _symbolBounds = null;


    /**
     * 
     */
    protected ImageInfo()
    {

    }

    /**
     * ImageInfo holds and image and holds the position at which the image
     * should be drawn.  Use for Multipoint and single point graphics.
     * @param image
     * @param x position of where the image should be drawn
     * @param y position of where the image should be drawn
     */
    public ImageInfo(BufferedImage image, int x, int y)
    {
        _Image = image;
        _X = x;
        _Y = y;
        _symbolCenterX = image.getWidth()/2;
        _symbolCenterY = image.getHeight()/2;
    }

    /**
     * ImageInfo holds and image and holds the position at which the image
     * should be drawn.  Use this if the image is a single point graphic.
     * @param image
     * @param x position of where the image should be drawn
     * @param y position of where the image should be drawn
     * @param symbolCenterX center point of image may be different center
     * point of the symbol within the image. (single point graphics)
     * @param symbolCenterY center point of image may be different center
     * point of the symbol within the image.  (single point graphics)
     * @deprecated 
     */
    public ImageInfo(BufferedImage image, int x, int y, int symbolCenterX, int symbolCenterY)
    {
        _Image = image;
        _X = x;
        _Y = y;
        _symbolCenterX = symbolCenterX;
        _symbolCenterY = symbolCenterY;
    }

    /**
     *
     * ImageInfo holds and image and holds the position at which the image
     * should be drawn.  Use this if the image is a single point graphic.
     * @param image
     * @param x position of where the image should be drawn
     * @param y position of where the image should be drawn
     * @param symbolCenterX center point of image may be different center
     * point of the symbol within the image. (single point graphics)
     * @param symbolCenterY center point of image may be different center
     * point of the symbol within the image.  (single point graphics)
     * @param symbolBounds minimum bounding rectangle for the core symbol. Does
     * not include modifiers, display or otherwise.
     */
    public ImageInfo(BufferedImage image, int x, int y, int symbolCenterX, int symbolCenterY, Rectangle2D symbolBounds)
    {
        _Image = image;
        _X = x;
        _Y = y;
        _symbolCenterX = symbolCenterX;
        _symbolCenterY = symbolCenterY;
        _symbolBounds = symbolBounds;
    }

    /**
     * The BufferedImage
     * @return the actual image
     */
    public BufferedImage getImage()
    {
        return _Image;
    }

    /**
     * X position of where the image should be drawn
     * @return
     */
    public int getX()
    {
        return _X;
    }


    /**
     * Y position of where the image should be drawn
     * @return
     */
    public int getY()
    {
        return _Y;
    }

    /**
     * position of where the image should be drawn
     * @return
     */
    public Point getPoint()
    {
        return new Point(_X, _Y);
    }

    /**
     * Center point of the symbol within the image.
     * With the exception of HQ where the symbol is centered on the bottom
     * of the staff.
     * @return
     */
    public int getSymbolCenterX()
    {
        return _symbolCenterX;
    }

    /**
     * Center point of the symbol within the image.
     * With the exception of HQ where the symbol is centered on the bottom
     * of the staff.
     * @return
     */
    public int getSymbolCenterY()
    {
        return _symbolCenterY;
    }

    /**
     * Center point of the symbol within the image.
     * With the exception of HQ where the symbol is centered on the bottom
     * of the staff.
     * @return
     */
    public Point getSymbolCenterPoint()
    {
        return new Point(_symbolCenterX, _symbolCenterY);
    }

    /**
     * minimum bounding rectangle for the core symbol. Does
     * not include modifiers, display or otherwise.
     * @return
     */
    public Rectangle2D getSymbolBounds()
    {
        return _symbolBounds;
    }

    /**
     * Save image to a file as a PNG or JPG
     * @param filePath  full path to the file
     * @param imageFormat  "jpg" or "png"
     * @return true on success.
     */
    public Boolean SaveImageToFile(String filePath, String imageFormat)
    {
        try
        {
            File outFile = new File(filePath);
            return ImageIO.write(_Image, imageFormat,outFile);
        }
        catch(Exception exc)
        {
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
     * @param ios  full path to the file.  Usage Like FileOutputStream out =
     * new FileOutputStream(filePath);
     * SaveImageToPNG(ImageIO.createImageOutputStream(out));
     * OR
     * ByteArrayOutputStream bytes = new ByteArrayOutputStream();
     * SaveImageToPNG(ImageIO.createImageOutputStream(bytes));
     * ImageOutputStream is closed before SaveImageToPNG exits.
     * Don't forget to close the streams when done.
     * @return true on success.
     */
    public Boolean SaveImageToPNG(ImageOutputStream ios)
    {
        try
        {
            RenderedImage image = (RenderedImage)_Image;
            Iterator<ImageWriter> itr = ImageIO.getImageWritersBySuffix("png");
            String metaDataFormatName = "";

            if(itr.hasNext())
            {
                ImageWriter iw = itr.next();
                IIOMetadata meta = iw.getDefaultImageMetadata(new ImageTypeSpecifier(image), null);

                //create & populate metadata
                metaDataFormatName = meta.getMetadataFormatNames()[0];
                StringBuilder XML = new StringBuilder("");
                XML.append("<"+metaDataFormatName+">");//"</javax_imageio_png_1.0>"
                XML.append("<tEXt>");
                //XML.append("<tEXtEntry keyword=\"symbolCenterX\" value=\"" + String.valueOf(_symbolCenterX)+"\"/>");
                XML.append("<tEXtEntry keyword=\"centerPoint\" value=\"" + "x="+String.valueOf(_symbolCenterX)+
                        ",y="+String.valueOf(_symbolCenterY)+"\"/>");

                XML.append("<tEXtEntry keyword=\"bounds\" value=\"" + "x="+String.valueOf(_symbolBounds.getX())+
                        ",y="+String.valueOf(_symbolBounds.getY())+
                        ",width="+String.valueOf(_symbolBounds.getWidth())+
                        ",height="+String.valueOf(_symbolBounds.getHeight())+"\"/>");

                XML.append("<tEXtEntry keyword=\"imageExtent\" value=\"" +
                        "width="+String.valueOf(_Image.getWidth())+
                        ",height="+String.valueOf(_Image.getHeight())+"\"/>");

                XML.append("</tEXt>");
                XML.append( "</"+metaDataFormatName+">");//"</javax_imageio_png_1.0>"

                //ErrorLogger.LogMessage(XML.toString());
                DOMResult domresult = new DOMResult();
                TransformerFactory.newInstance().newTransformer().transform(new StreamSource(new StringReader(XML.toString())), domresult);
                Node document = domresult.getNode();

                //test
//                Node foo = document.getFirstChild();
//                for(int i=0; i<foo.getChildNodes().getLength(); i++)
//                            ErrorLogger.LogMessage(foo.getChildNodes().item(i).getNodeName());

                
                //apply metadata
                meta.mergeTree(metaDataFormatName, document.getFirstChild());

                //test
//                foo = meta.getAsTree(meta.getMetadataFormatNames()[0]);
//                //foo = foo.getFirstChild();
//                for(int i=0; i<foo.getChildNodes().getLength(); i++)
//                            ErrorLogger.LogMessage(foo.getChildNodes().item(i).getNodeName());

                //Render PNG to Memory
                IIOImage iioImage = new IIOImage(image, null, null);
                iioImage.setMetadata(meta);

                //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                //iw.setOutput(ImageIO.createImageOutputStream(bytes));
                //iw.write(null, iioImage, null);

                iw.setOutput(ios);
                iw.write(null, iioImage, null); //iw.write(metadata, iioImage, null);
                ios.close();

                iw.dispose();
                iw = null;
                itr = null;
                iioImage = null;

                return true;
            }
            else
            {
                ErrorLogger.LogMessage("ImageInfo", "SaveImageToPNG", "no PNG imageWriter available");
                return false;
            }

        }
        catch(Exception exc)
        {
            ErrorLogger.LogException("ImageInfo", "SaveImageToFile", exc);
            return false;
        }

    }



    /**
     * Takes an image and a center point and generates a new, bigger image
     * that has the symbol centered in it
     * @param image
     * @param point
     * @return
     */
    public static BufferedImage CenterImageOnPoint(BufferedImage image, Point2D point)
    {
        BufferedImage bi = null;
        int x = 0;
        int y = 0;
        int height = 0;
        int width = 0;
        height = image.getHeight();
        width = image.getWidth();

        try
        {
            if(point.getY() > height - point.getY())
            {
                height = (int)(point.getY() * 2.0);
                y=0;
            }
            else
            {
                height = (int)((height - point.getY()) * 2);
                y = (int)((height / 2) - point.getY());
            }

            if(point.getX() > width - point.getX())
            {
                width = (int)(point.getX() * 2.0);
                x=0;
            }
            else
            {
                width = (int)((width - point.getX()) * 2);
                x = (int)((width / 2) - point.getX());
            }


            bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d =  bi.createGraphics();
            g2d.drawImage(image, x, y, null);
        }
        catch(Exception exc)
        {
            ErrorLogger.LogException("ImageInfo", "CenterImageOnPoint", exc);
        }
        return bi;
    }

    /**
     * Determines if you can write your desired format.
     * @param format "png", "jpg", "gif", etc...
     * @return true/false
     */
    public static Boolean CanWriteImageFormat(String format)
    {
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(format);
        return iter.hasNext();
    }

    /**
     * Convenience method that returns a scaled instance of the
     * provided {@code BufferedImage}. NEEDS WORK.
     * Alternate option is getScaledInstance off of the BufferedImage object.
     *
     * @param srcImage The image to be scaled.
     * @param targetWidth the desired width of the scaled instance,
     *    in pixels
     * @param targetHeight the desired height of the scaled instance,
     *    in pixels
     * @param hint one of the rendering hints that corresponds to
     *    {@code RenderingHints.KEY_INTERPOLATION} (e.g.
     *    {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @param higherQuality if true, this method will use a multi-step
     *    scaling technique that provides higher quality than the usual
     *    one-step technique (only useful in downscaling cases, where
     *    {@code targetWidth} or {@code targetHeight} is
     *    smaller than the original dimensions, and generally only when
     *    the {@code BILINEAR} hint is specified). NEEDS WORK.
     * @param keepProportion Don't stretch the original image to fit into the
     * target height/width.
     * @return a scaled version of the original {@code BufferedImage}
     * 
     */
    public static BufferedImage getScaledInstance(BufferedImage srcImage,
                                           int targetWidth,
                                           int targetHeight,
                                           Object hint,
                                           boolean higherQuality,
                                           boolean keepProportion)
    {
        int type = (srcImage.getTransparency() == Transparency.OPAQUE) ?
            BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = (BufferedImage)srcImage;
        int w, h;

        //keep things proportioned///////////
        double ratio = 0;
        double SrcW = srcImage.getWidth();
        double SrcH = srcImage.getHeight();
        double tW = targetWidth;
        double tH = targetHeight;

        if(keepProportion)
        {
            ratio = Math.min((tH / SrcH), (tW / SrcW));
            //ratio = (SrcW / tW);
            targetWidth = (int)((SrcW) * ratio);
            targetHeight = (int)((SrcH) * ratio);
        }///////////////////////////////////

        if (higherQuality)
        {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = srcImage.getWidth();
            h = srcImage.getHeight();
        }
        else
        {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do
        {
            if (higherQuality && w > targetWidth)
            {
                w /= 2;
                if (w < targetWidth)
                {
                    w = targetWidth;
                }
            }

            if (higherQuality && h > targetHeight)
            {
                h /= 2;
                if (h < targetHeight)
                {
                    h = targetHeight;
                }
            }


            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }

}
