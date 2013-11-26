/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.web.renderer.utilities;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
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

import ArmyC2.C2SD.RendererPluginInterface.ISinglePointInfo;
import ArmyC2.C2SD.Utilities.ErrorLogger;
import ArmyC2.C2SD.Utilities.ImageInfo;

/**
 *
 * @author michael.spinelli
 */
public class PNGInfo
{

    Point2D _centerPoint = null;
    Rectangle2D _symbolBounds = null;
    BufferedImage _image = null;
    public PNGInfo(ImageInfo ii)
    {
        _centerPoint = new Point2D.Double(ii.getSymbolCenterX(), ii.getSymbolCenterY());
        _symbolBounds = ii.getSymbolBounds();
        _image = ii.getImage();
    }
    
    public PNGInfo(ISinglePointInfo spi)
    {
        _centerPoint = spi.getSymbolCenterPoint();
        _symbolBounds = spi.getSymbolBounds();
        _image = spi.getImage();
    }
    
    public PNGInfo(BufferedImage image, Point2D centerPoint, Rectangle2D symbolBounds)
    {
        _image = image;
        _symbolBounds = symbolBounds;
        _centerPoint = centerPoint;
    }
    
     /**
     * Center point of the symbol within the image.
     * With the exception of HQ where the symbol is centered on the bottom
     * of the staff.
     * @return
     */
    public Point2D getCenterPoint()
    {
        return _centerPoint;
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
    
    public BufferedImage getImage()
    {
        return _image;
    }
    
    /**
     * returns the image as a byte[] representing a PNG.
     * @return 
     */
    public byte[] getImageAsByteArray()
    {
        byte[] byteArray = null;
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedImage image = _image;
            ImageIO.write(image, "png", baos);
            //Send to Byte Array
            baos.flush();
            byteArray = baos.toByteArray();
            baos.close();
        }
        catch(Exception exc)
        {
            ErrorLogger.LogException("PNGInfo", "getImageAsByteArray", exc);
        }
        return byteArray;
    }
    
    /**
     * Save image to a file as a PNG
     * @param filePath  full path to the file
     * @return true on success.
     */
    public Boolean saveImageToFile(String filePath)
    {
        try
        {
            File outFile = new File(filePath);
            return ImageIO.write(_image, "png",outFile);
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
    public byte[] getImageAsByteArrayWithMetaInfo()
    {
        byte[] metaImage  = null;
        try
        {
            
            RenderedImage image = (RenderedImage)this._image;
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
                XML.append("<tEXtEntry keyword=\"centerPoint\" value=\"" + "x="+String.valueOf(this._centerPoint.getX())+
                        ",y="+String.valueOf(this._centerPoint.getY())+"\"/>");

                XML.append("<tEXtEntry keyword=\"bounds\" value=\"" + "x="+String.valueOf(_symbolBounds.getX())+
                        ",y="+String.valueOf(_symbolBounds.getY())+
                        ",width="+String.valueOf(_symbolBounds.getWidth())+
                        ",height="+String.valueOf(_symbolBounds.getHeight())+"\"/>");

                XML.append("<tEXtEntry keyword=\"imageExtent\" value=\"" +
                        "width="+String.valueOf(image.getWidth())+
                        ",height="+String.valueOf(image.getHeight())+"\"/>");

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
            }
            else
            {
                ErrorLogger.LogMessage("ImageInfo", "getImageAsByteArrayWithMetaInfo", "no PNG imageWriter available");
                return null;
            }

        }
        catch(Exception exc)
        {
            ErrorLogger.LogException("ImageInfo", "getImageAsByteArrayWithMetaInfo", exc);
            return null;
        }

    }
    
     /**
     * Takes an image and a center point and generates a new, bigger image
     * that has the symbol centered in it
     * @return
     */
    public PNGInfo centerImage()
    {
        BufferedImage image = null;
        Point2D point = null;
        Point2D newCenter = null;
        PNGInfo pi = null;
        BufferedImage bi = null;
        int x = 0;
        int y = 0;
        int height = 0;
        int width = 0;
        

        try
        {
            image = _image;
            point = _centerPoint;
            height = image.getHeight();
            width = image.getWidth();
            
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
            newCenter = new Point2D.Double(bi.getWidth()/2, bi.getHeight()/2);
            
            double bx=_symbolBounds.getX();
            double by=_symbolBounds.getY();
            
            if(newCenter.getX() > bx)
            {
                bx = bx + (newCenter.getX() - _centerPoint.getX());
            }
            if(newCenter.getY() > by)
            {
                by = by + (newCenter.getY() - _centerPoint.getY());
            }
                
            Rectangle2D bounds = new Rectangle2D.Double(bx,by,
                    this.getSymbolBounds().getWidth(), 
                    this.getSymbolBounds().getHeight());
            
            Graphics2D g2d =  bi.createGraphics();
            g2d.drawImage(image, x, y, null);
            pi = new PNGInfo(bi, newCenter, bounds);
        }
        catch(Exception exc)
        {
            ErrorLogger.LogException("ImageInfo", "CenterImage", exc);
        }
        return pi;
    }
    
    /**
     * Returns an image with empty space as needed to make sure the size 
     * represents a square.
     * @return 
     */
    public PNGInfo squareImage()
    {
        BufferedImage image = null;
        Point2D point = null;
        Point2D newCenter = null;
        PNGInfo pi = null;
        BufferedImage bi = null;
        double x = 0;
        double y = 0;
        int height = 0;
        int width = 0;
        

        try
        {
            image = _image;
            point = _centerPoint;
            height = image.getHeight();
            width = image.getWidth();
        
            int newSize = height;
            if(width > height)
                newSize = width;
            
            if(width < newSize)
                x = (newSize - width)/2.0;
            
            if(height < newSize)
                y = (newSize - height)/2.0;
                

            
            
            bi = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_ARGB);
            newCenter = new Point2D.Double(_centerPoint.getX() + x, _centerPoint.getY() + y);
            
            double bx=_symbolBounds.getX();
            double by=_symbolBounds.getY();
            
            if(newCenter.getX() > bx)
            {
                bx = bx + x;
            }
            if(newCenter.getY() > by)
            {
                by = by + y;
            }
                
            Rectangle2D bounds = new Rectangle2D.Double(bx,by,
                    this.getSymbolBounds().getWidth(), 
                    this.getSymbolBounds().getHeight());
            
            Graphics2D g2d =  bi.createGraphics();
            g2d.drawImage(image, (int)x, (int)y, null);
            pi = new PNGInfo(bi, newCenter, bounds);
        }
        catch(Exception exc)
        {
            ErrorLogger.LogException("ImageInfo", "CenterImage", exc);
        }
        return pi;
    }
}
