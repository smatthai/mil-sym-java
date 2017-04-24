/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RenderMultipoints;
import JavaLineArray.arraysupport;
import JavaLineArray.CELineArray;
import JavaTacticalRenderer.clsChannelUtility;
import JavaTacticalRenderer.Modifier2;
import JavaTacticalRenderer.TGLight;
import JavaTacticalRenderer.clsUtility;
import JavaTacticalRenderer.P1;
import JavaLineArray.ref;
import JavaLineArray.POINT2;
import JavaTacticalRenderer.clsMETOC;
import java.util.ArrayList;
import JavaLineArray.Shape2;
import JavaLineArray.TacticalLines;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.Point2D;
//import com.oculus.SharedRenderer.util.LonLatPixelConverter;
import ArmyC2.C2SD.Utilities.ErrorLogger;
import ArmyC2.C2SD.Utilities.RendererException;
import java.awt.geom.Rectangle2D;
import java.awt.Rectangle;
import ArmyC2.C2SD.Utilities.IPointConversion;
import JavaLineArray.lineutility;
import java.awt.Color;
import java.util.HashMap;
import java.awt.BasicStroke;
/**
 * Rendering helper class
 * @author Michael Deutch
 */
public final class clsRenderer2 {
    private static final String _className="clsRenderer2";
    /**
     * MSR and ASR use segment data for segment colors
     * Assumes tg.H has been revised for clipping
     * @param tg
     * @param shapes 
     */
    private static void getMSRShapes(TGLight tg,
            ArrayList<Shape2>shapes)
    {
        try
        {
            int linetype=tg.get_LineType();
            if(linetype != TacticalLines.MSR && linetype != TacticalLines.ASR)
                return;
            
            HashMap hmap=JavaTacticalRenderer.clsUtility.getMSRSegmentColors(tg);
            Shape2 shape=null;
            
            BasicStroke stroke=null;            
            if(tg.get_Client().equalsIgnoreCase("ge"))            
                stroke=JavaTacticalRenderer.clsUtility.getLineStroke2(tg.get_LineThickness(),tg.get_LineStyle(),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);                
            else
                stroke=JavaTacticalRenderer.clsUtility.getLineStroke(tg.get_LineThickness(),tg.get_LineStyle(),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
            
            int j=0;
            Color color=null;
            Shape2 segShape=null;
            shape=new Shape2(Shape2.SHAPE_TYPE_POLYLINE);
            shape.setLineColor(tg.get_LineColor());
            shape.setStroke(stroke);
            
            //if colors are not set then use one shape
            //assumes colors may be set if string is comma delimited
//            String strH=tg.get_H();
//            if(strH != null && !strH.isEmpty())
//            {               
//                String[] strs=strH.split(",");
//                if(strs.length<2)
//                {
//                    //shape=new Shape2(Shape2.SHAPE_TYPE_POLYLINE);
//                    //shape.setLineColor(tg.get_LineColor());
//                    //shape.setStroke(stroke);
//                    shape.moveTo(tg.Pixels.get(0));
//                    for(j=1;j<tg.Pixels.size();j++)
//                    {
//                        shape.lineTo(tg.Pixels.get(j));
//                    }
//                    shapes.add(shape);
//                    return;
//                }
//            }
                        
            //if the hashmap contains the segment then use the color corresponding to the segment
            //in the hashtable to create a one segment shape to add to the shape array.
            //else sdd the segment to the original shape
            Color lastColor=null;   //diagnostic
            double dist=0,dist2=0;
            POINT2 pt0=null,pt1=null;
            POINT2 lastPt=null;
            for(j=0;j<tg.Pixels.size()-1;j++)
            {
//                if(hmap !=null && hmap.containsKey(j))
//                {
//                    color=(Color)hmap.get(j);
//                    segShape = new Shape2(Shape2.SHAPE_TYPE_POLYLINE);                    
//                    segShape.setLineColor(color);
//                    segShape.set_Style(tg.get_LineStyle());
//                    segShape.setStroke(stroke);
//                    segShape.moveTo(tg.Pixels.get(j));
//                    segShape.lineTo(tg.Pixels.get(j+1));                    
//                    shapes.add(segShape);                    
//                }                                                    
                pt0=tg.Pixels.get(j);
                pt1=tg.Pixels.get(j+1);
                if(hmap !=null && hmap.containsKey(j))
                {
                    color=(Color)hmap.get(j);
                    if(color != lastColor)
                    {
                        if(segShape != null)
                            shapes.add(segShape);                    
                        
                        segShape = new Shape2(Shape2.SHAPE_TYPE_POLYLINE);                    
                        segShape.setLineColor(color);
                        segShape.set_Style(tg.get_LineStyle());
                        segShape.setStroke(stroke);
                    }
                    segShape.moveTo(pt0);
                    segShape.lineTo(pt1);                    
                    lastColor=new Color(color.getRGB());
                }                                    
                else
                {
                    //shape=new Shape2(Shape2.SHAPE_TYPE_POLYLINE);
                    //shape.setLineColor(tg.get_LineColor());
                    //shape.setStroke(stroke);
                    if(hmap !=null && hmap.containsKey(j+1))
                    {
                        shape.moveTo(pt0);
                        shape.lineTo(pt1);
                        lastPt=new POINT2(pt1);
                    }
                    else if(hmap !=null && hmap.containsKey(j-1))
                    {
                        shape.moveTo(pt0);
                        shape.lineTo(pt1);                        
                        lastPt=new POINT2(pt1);
                    }
                    else if(j==tg.Pixels.size()-2)
                    {
                        shape.moveTo(pt0);
                        shape.lineTo(pt1);                                                
                    }
                    else
                    {
                        if(lastPt==null)
                        {
                            lastPt=new POINT2(pt0);
                            shape.moveTo(lastPt);
                            //shape.lineTo(lastPt);
                        }                        
                        dist=lineutility.CalcDistanceDouble(pt0, pt1);
                        if(dist>10)
                        {
                            //shape.moveTo(pt0);
                            shape.lineTo(pt1);                                                
                            lastPt=new POINT2(pt1);                            
                        }
                        else
                        {
                            dist2=lineutility.CalcDistanceDouble(lastPt, pt1);
                            if(dist2>10)
                            {
                                //shape.moveTo(pt0);
                                shape.lineTo(pt1);                                                
                                lastPt=new POINT2(pt1);                            
                            }                            
                        }
                    }
                    //shapes.add(shape);
                }
            }
            if(segShape != null)
                shapes.add(segShape);                    
            
            shapes.add(shape);
        }
        catch (Exception exc)
        {
            ErrorLogger.LogException(_className ,"getMSRShapes",
                new RendererException("Failed inside getMSRShapes", exc));
        }
    }
    /**
     * 
     * @param tg
     * @param converter client converter
     * @param isTextFlipped
     * @return
     */
    protected static ArrayList<Shape2> GetLineArray(TGLight tg,
            IPointConversion converter,
            boolean isTextFlipped,
            Object clipBounds)
    {
        ArrayList<Shape2> shapes=new ArrayList();
        try
        {
            if(tg.Pixels==null || tg.Pixels.isEmpty())
                return null;            
            double x=0;
            double y=0;
            double width=0;
            double height=0;
            Rectangle2D clipBounds2=null;
            int rev=tg.getSymbologyStandard();
            
            Rectangle2D clipRect=null;
            ArrayList<Point2D>clipArray=null;
            if(clipBounds != null)
            {
                if(clipBounds.getClass().isAssignableFrom(Rectangle2D.Double.class) || 
                        clipBounds.getClass().isAssignableFrom(Rectangle.class))
                {            
                    //clipRect=(Rectangle2D.Double)clipBounds;
                    clipRect=(Rectangle2D)clipBounds;
                    x=clipRect.getMinX()-50;
                    y=clipRect.getMinY()-50;
                    width=clipRect.getWidth()+100;
                    height=clipRect.getHeight()+100;
                    clipBounds2=new Rectangle2D.Double(x,y,width,height);
                }
                else if(clipBounds.getClass().isAssignableFrom(ArrayList.class))
                {
                    clipArray=(ArrayList<Point2D>)clipBounds;
                    clipBounds2=RenderMultipoints.clsUtility.getMBR(clipArray);
                }
            }
            
            int lineType=tg.get_LineType();
            int minPoints2=clsUtility.GetMinPoints(lineType);
            ref<int[]> minPoints = new ref();
            boolean bolResult = clsUtility.IsChange1Area(lineType, minPoints);
            int bolMeTOC=JavaTacticalRenderer.clsMETOC.IsWeather(tg.get_SymbolId());
            
            //diagnostic
            //tg.modifiers=new ArrayList();
            //if(tg.modifiers==null)
                //tg.modifiers=new ArrayList();
            //uncomment following line for USAS LC
            //clsUtility.ReverseUSASLCPointsByQuadrant(tg);
            ArrayList<POINT2>pts=new ArrayList();
            //uncomment one line for usas1314
            Boolean usas1314=true;
            int j=0;
            switch(tg.get_LineType())
            {
                case TacticalLines.HOLD:
                case TacticalLines.BRDGHD:
                case TacticalLines.HOLD_GE:
                case TacticalLines.BRDGHD_GE:
                    if(tg.Pixels.size()<2)
                        return null;
                    if(usas1314)
                        break;
                    pts.add(tg.Pixels.get(0));
                    for(j=2;j<tg.Pixels.size();j++)
                    {
                        pts.add(tg.Pixels.get(j));
                    }
                    pts.add(tg.Pixels.get(1));
                    tg.Pixels=pts;
                    break;
                case TacticalLines.SINGLEC://reverse single concertina
                case TacticalLines.SINGLEC2:
                    pts=(ArrayList<POINT2>)tg.Pixels.clone();
                    for(j=0;j<tg.Pixels.size();j++)
                        tg.Pixels.set(j, pts.get(pts.size()-j-1));
                    break;
                default:
                    break;
            }

            //set CELineArray.shapes properties
            BufferedImage bi=new BufferedImage(8,8,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d=bi.createGraphics();
            
            //diagnostic
            //if(RenderMultipoints.clsUtility.addModifiersBeforeClipping(tg)==false)
                //Modifier2.AddModifiers(tg,g2d,clipBounds);
            //end section
            
            Shape2 hatchShape=JavaTacticalRenderer.clsUtility.getHatchShape(tg,bi);

            if(tg.Pixels.size()<minPoints2)
            {
                bolResult=false;
            }

            if (bolResult)
            {

                tg.Pixels.clear();
                //Change1TacticalAreas will also handle the modifiers
                bolResult = clsUtilityCPOF.Change1TacticalAreas(tg, lineType, converter, shapes);
            }
            else if(bolMeTOC>0)
            {
                if(tg.Pixels.size()<2)
                    return null;

                try
                {
                    clsMETOC.GetMeTOCShape(tg, shapes,rev);
                }
                catch(Exception exc)
                {
                    ErrorLogger.LogException(_className ,"GetLineArray",
                        new RendererException("Failed inside GetLineArray", exc));
                }
            }
            else
            {
                //this will help with click-drag mode
                if(tg.Pixels.size()<2)
                    if(lineType != TacticalLines.BS_CROSS)
                        return null;
                
                if (CELineArray.CIsChannel(lineType) == 0)
                {
                    if(lineType==TacticalLines.ASR || lineType==TacticalLines.MSR)
                    {
                        getMSRShapes(tg,shapes);
                    }
                    else if(lineType !=TacticalLines.BELT1)
                    {
                        tg.Pixels=arraysupport.GetLineArray2(lineType, tg.Pixels,shapes, clipBounds2,rev,converter);
                    }
                    else if(lineType==TacticalLines.BELT1)
                    {
                        //get the partitions
                        //diagnostic 9-27-11
                        //clsUtility.MovePixels2(tg.Pixels);
                        ArrayList<Shape2> tempShapes= null;
                        ArrayList<P1>partitions=clsChannelUtility.GetPartitions2(tg);
                        ArrayList<POINT2>pixels=null;
                        int l=0,k=0;
                        for(l=0;l<partitions.size();l++)
                        {
                            tempShapes= new ArrayList();
                            pixels=new ArrayList();
                            for(k=partitions.get(l).start;k<=partitions.get(l).end_Renamed+1;k++)
                            {
                                pixels.add(tg.Pixels.get(k));
                            }
                            pixels=arraysupport.GetLineArray2(lineType, pixels, tempShapes, clipBounds2,rev,converter);
                            shapes.addAll(tempShapes);
                        }
                    }
                }
                else //channel type
                {
                    clsChannelUtility.DrawChannel(tg.Pixels, lineType, tg,shapes, null,rev);
                }
            }
            //set CELineArray.shapes properties
            if(bolMeTOC<=0)
            {
                if(lineType!=TacticalLines.ASR && lineType!=TacticalLines.MSR)                
                    JavaTacticalRenderer.clsUtility.SetShapeProperties(tg,shapes,bi);
                RenderMultipoints.clsUtility.ResolveDummyShapes(tg, shapes);
            }

            if(hatchShape != null)
                shapes.add(hatchShape);
            //at this point tg.Pixels has the points from CELineArray
            //the following line adds modifiers for those sybmols which require
            //the calculated points to use for the modifiers.
            //currentlly only BLOCK and CONTAIN use tg.Pixels for computing
            //the modifiers after the call to GetLineArray
            //Modifier2.AddModifiers2(tg);//flipped only for 3d for change 1 symbols
            Modifier2.AddModifiers2RevD(tg,shapes);//flipped only for 3d for change 1 symbols
            Modifier2.addSectorModifiers(tg, converter);

            //boundary has shapes for line break
            Modifier2.GetIntegralTextShapes(tg, g2d, shapes);

            bi.flush();
            g2d.dispose();
            bi=null;
            g2d=null;
        }
        catch (Exception exc)
        {
            ErrorLogger.LogException(_className ,"GetLineArray",
                new RendererException("Failed inside GetLineArray", exc));
        }
        return shapes;
    }
    /**
     * Isolate and others require special handling for the fill shapes.
     * @param tg 
     * @param shapes the existing shapes which characterize the graphic
     */
    static protected void getAutoshapeFillShape(TGLight tg, ArrayList<Shape2>shapes)
    {
        try
        {            
            if(shapes==null || shapes.size()==0)
                return;
            if(tg.Pixels==null || tg.Pixels.size()==0)
                return;
            if(tg.get_FillColor()==null)
                return;
            
            int linetype=tg.get_LineType();
            int j=0;
            Shape2 shape=new Shape2(Shape2.SHAPE_TYPE_FILL);
            shape.setFillColor(tg.get_FillColor());
            shape.setLineColor(null);
            switch(linetype)
            {
                case TacticalLines.RETAIN:
                    if(shapes!=null && !shapes.isEmpty())
                        for(j=0;j<shapes.size();j++)
                            shapes.get(j).setFillColor(null);
                    
                    shape.moveTo(tg.Pixels.get(0));
                    for(j=1;j<26;j++)                    
                        shape.lineTo(tg.Pixels.get(j));
                    
                    shape.lineTo(tg.Pixels.get(0));
                    shapes.add(0,shape);
                    break;
                case TacticalLines.SECURE:
                case TacticalLines.OCCUPY:
                    if(shapes!=null && !shapes.isEmpty())
                        for(j=0;j<shapes.size();j++)
                            shapes.get(j).setFillColor(null);
                    
                    shape.moveTo(tg.Pixels.get(0));
                    for(j=1;j<tg.Pixels.size()-3;j++)                    
                        shape.lineTo(tg.Pixels.get(j));
                    
                    shape.lineTo(tg.Pixels.get(0));
                    shapes.add(0,shape);
                    break;
                case TacticalLines.CONVOY:
                case TacticalLines.HCONVOY:
                    if(shapes!=null && !shapes.isEmpty())
                        for(j=0;j<shapes.size();j++)
                            shapes.get(j).setFillColor(null);
                    
                    shape.moveTo(tg.Pixels.get(0));
                    for(j=1;j<tg.Pixels.size();j++)                    
                        shape.lineTo(tg.Pixels.get(j));
                    
                    shape.lineTo(tg.Pixels.get(0));
                    shapes.add(0,shape);
                    break;
                case TacticalLines.CORDONSEARCH:
                case TacticalLines.CORDONKNOCK:
                case TacticalLines.ISOLATE:
                    //set the fillcolor to null for the existing shapes
                    //we are going to create a new fill shape
                    if(shapes!=null && !shapes.isEmpty())
                        for(j=0;j<shapes.size();j++)
                            shapes.get(j).setFillColor(null);
                    
                    shape.moveTo(tg.Pixels.get(0));
                    for(j=26;j<47;j++)                    
                        shape.lineTo(tg.Pixels.get(j));
                    
                    shape.lineTo(tg.Pixels.get(23));
                    shape.lineTo(tg.Pixels.get(24));
                    shape.lineTo(tg.Pixels.get(25));
                    shape.lineTo(tg.Pixels.get(0));
                    shapes.add(0,shape);
                    break;
                default:
                    return;
            }
        }
        catch (Exception exc)
        {
            ErrorLogger.LogException(_className ,"getAutoshapeFillShape",
                new RendererException("Failed inside getAutoshapeFillShape", exc));
        }
        return;
    }
}
