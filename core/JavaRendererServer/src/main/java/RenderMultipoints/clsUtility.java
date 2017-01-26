/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RenderMultipoints;
import ArmyC2.C2SD.Utilities.PointConversion;
import ArmyC2.C2SD.Utilities.IPointConversion;
import java.awt.Point;
import java.awt.geom.Point2D;
import JavaTacticalRenderer.TGLight;
import JavaLineArray.TacticalLines;
import JavaLineArray.lineutility;
import JavaLineArray.POINT2;
import java.util.ArrayList;
import ArmyC2.C2SD.Utilities.ErrorLogger;
import ArmyC2.C2SD.Utilities.RendererException;
import JavaLineArray.Shape2;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
/**
 * Server general utility class
 * @author Michael Deutch
 */
public final class clsUtility {
    private static final String _className="clsUtility";
    protected static Point POINT2ToPoint(POINT2 pt2)
    {
        Point pt=new Point();
        pt.x=(int)pt2.x;
        pt.y=(int)pt2.y;
        return pt;
    }
    protected static POINT2 PointToPOINT2(Point pt)
    {
        POINT2 pt2=new POINT2(pt.x,pt.y);
        return pt2;
    }
    protected static Point2D.Double POINT2ToPoint2D(POINT2 pt2)
    {
        Point2D.Double pt2d=new Point2D.Double(pt2.x,pt2.y);
        return pt2d;
    }
    protected static ArrayList<POINT2> Points2DToPOINT2(ArrayList<Point2D>pts2d)
    {
        ArrayList<POINT2>pts=new ArrayList();
        POINT2 pt=null;
        for(int j=0;j<pts2d.size();j++)        
        {
            pt=new POINT2(pts2d.get(j).getX(),pts2d.get(j).getY());
            pts.add(pt);
        }        
        return pts;
    }
    protected static POINT2 Point2DToPOINT2(Point2D.Double pt2d)
    {
        POINT2 pt2=new POINT2(pt2d.x,pt2d.y);
        return pt2;
    }
    /**
     * @deprecated   
     * @param tg
     * @return 
     */
    protected static boolean addModifiersBeforeClipping(TGLight tg)
    {
        boolean result=false;
        int linetype=tg.get_LineType();
        switch(linetype)
        {
            case TacticalLines.TORPEDO:
            case TacticalLines.OPTICAL:
            case TacticalLines.ELECTRO:
            case TacticalLines.ACOUSTIC:
            case TacticalLines.BEARING:
            case TacticalLines.TWOWAY:
            case TacticalLines.ALT:
            case TacticalLines.ONEWAY:
            case TacticalLines.ASR:
            case TacticalLines.MSR:
            case TacticalLines.HCONVOY:
            case TacticalLines.CONVOY:
            case TacticalLines.MFP:
            case TacticalLines.RFL:
            case TacticalLines.NFL:
            case TacticalLines.CFL:
            case TacticalLines.FSCL:
            case TacticalLines.FPF:
            case TacticalLines.LINTGT:
            case TacticalLines.LINTGTS:
            case TacticalLines.MSDZ:
            case TacticalLines.GAP:
            case TacticalLines.IL:
            case TacticalLines.DIRATKAIR:
            case TacticalLines.PDF:
            case TacticalLines.FEBA:
            case TacticalLines.DIRATKFNT:
            case TacticalLines.AAFNT:
            case TacticalLines.AC:
            case TacticalLines.SAAFR:
            case TacticalLines.LLTR:
            case TacticalLines.UAV:
            case TacticalLines.MRR:
            case TacticalLines.UAV_USAS:
            case TacticalLines.MRR_USAS:
            case TacticalLines.BOUNDARY:
            case TacticalLines.WDRAWUP:
            case TacticalLines.WITHDRAW:
            case TacticalLines.RETIRE:
            case TacticalLines.RIP:
            case TacticalLines.DELAY:
            case TacticalLines.CATK:
            case TacticalLines.CATKBYFIRE:
            case TacticalLines.SCREEN:
            case TacticalLines.COVER:
            case TacticalLines.GUARD:
            case TacticalLines.SCREEN_REVC:
            case TacticalLines.COVER_REVC:
            case TacticalLines.GUARD_REVC:
            case TacticalLines.FLOT:
            case TacticalLines.LC:
            case TacticalLines.PL:
            case TacticalLines.LL:
            case TacticalLines.FCL:
            case TacticalLines.LOA:
            case TacticalLines.LOD:
            case TacticalLines.LDLC:
            case TacticalLines.PLD:
//            case TacticalLines.HOLD:
//            case TacticalLines.BRDGHD:
//            case TacticalLines.HOLD_GE:
//            case TacticalLines.BRDGHD_GE:
            case TacticalLines.RELEASE:
                result = true;
                break;
            default:
                break;
        }
        if(JavaTacticalRenderer.clsUtility.isClosedPolygon(linetype)==true)
            result=true;
        return result;
    }
    protected static void FilterPoints(TGLight tg)
    {
        try
        {
            int lineType = tg.get_LineType();
            double minSpikeDistance = 0;
            switch (lineType)
            {
                //case TacticalLines.LC:
                case TacticalLines.ATDITCH:
                case TacticalLines.ATDITCHC:
                case TacticalLines.ATDITCHM:
                case TacticalLines.FLOT:
                case TacticalLines.FORT:
                case TacticalLines.FORTL:
                case TacticalLines.STRONG:
                    minSpikeDistance=25;
                    break;
                case TacticalLines.LC:
                case TacticalLines.OBSAREA:
                case TacticalLines.OBSFAREA:
                case TacticalLines.ENCIRCLE:
                case TacticalLines.BELT:    //belt as an area
                case TacticalLines.BELT1:   //belt as a line (USAS)
                case TacticalLines.ZONE:
                case TacticalLines.LINE:
                case TacticalLines.ATWALL:
                //case TacticalLines.ATWALL3D:
                case TacticalLines.UNSP:
                case TacticalLines.SFENCE:
                case TacticalLines.DFENCE:
                case TacticalLines.DOUBLEA:
                case TacticalLines.LWFENCE:
                case TacticalLines.HWFENCE:
                case TacticalLines.SINGLEC:
                case TacticalLines.DOUBLEC:
                case TacticalLines.TRIPLE:
                    minSpikeDistance=35;
                    break;
                case TacticalLines.UCF:
                case TacticalLines.CF:
                case TacticalLines.CFG:
                case TacticalLines.CFY:
                    minSpikeDistance=60;
                    break;
                case TacticalLines.SF:
                case TacticalLines.USF:
                case TacticalLines.OCCLUDED:
                case TacticalLines.UOF:
                    minSpikeDistance=60;//was 120
                    break;
                case TacticalLines.SFG:
                case TacticalLines.SFY:
                    minSpikeDistance=60;//was 180
                    break;
                case TacticalLines.WFY:
                case TacticalLines.WFG:
                case TacticalLines.OFY:
                    minSpikeDistance=60;//was 120
                    break;
                case TacticalLines.WF:
                case TacticalLines.UWF:
                    minSpikeDistance=40;
                    break;

                case TacticalLines.RIDGE:
                case TacticalLines.ICE_EDGE_RADAR:  //METOCs
                case TacticalLines.ICE_OPENINGS_FROZEN:
                case TacticalLines.CRACKS_SPECIFIC_LOCATION:
                    minSpikeDistance=35;
                    break;
                default:
                    return;
            }
            int j=0;
            double dist=0;
            ArrayList<POINT2>pts=new ArrayList();
            ArrayList<POINT2>ptsGeo=new ArrayList();
            pts.add(tg.Pixels.get(0));
            ptsGeo.add(tg.LatLongs.get(0));
            POINT2 lastGoodPt=tg.Pixels.get(0);
            POINT2 currentPt=null;
            POINT2 currentPtGeo=null;
            boolean foundGoodPt=false;
            for(j=1;j<tg.Pixels.size();j++)
            {
                //we can not filter out the original end points
                currentPt=tg.Pixels.get(j);
                currentPtGeo=tg.LatLongs.get(j);
                //diagnostic    1-5-12
                if(currentPt.style==-1)
                {
                    lastGoodPt=currentPt;
                    pts.add(currentPt);
                    ptsGeo.add(currentPtGeo);
                    foundGoodPt=true;
                    currentPt.style=0;
                    continue;
                }
                //end diagnostic
                dist=lineutility.CalcDistanceDouble(lastGoodPt, currentPt);
                switch(lineType)
                {
                    case TacticalLines.LC:
                        if(dist>minSpikeDistance)
                        {
                            lastGoodPt=currentPt;
                            pts.add(currentPt);
                            ptsGeo.add(currentPtGeo);
                            foundGoodPt=true;
                        }
                        else
                        {   //the last point is no good
                            //replace the last good point with the last point
                            if(j==tg.Pixels.size()-1)
                            {
                                pts.set(pts.size()-1, currentPt);
                                ptsGeo.set(ptsGeo.size()-1, currentPtGeo);
                            }
                        }
                        break;
                    default:
                        if(dist>minSpikeDistance || j==tg.Pixels.size()-1)
                        {
                            lastGoodPt=currentPt;
                            pts.add(currentPt);
                            ptsGeo.add(currentPtGeo);
                            foundGoodPt=true;
                        }
                        break;
                }
            }
            if(foundGoodPt==true)
            {
                tg.Pixels=pts;
                tg.LatLongs=ptsGeo;
            }
        }
        catch(Exception exc)
        {
            ErrorLogger.LogException("clsUtility", "FilterPoints",
                    new RendererException("Failed inside FilterPoints", exc));

        }
    }

    protected static ArrayList<POINT2> PixelsToLatLong(ArrayList<POINT2> pts, IPointConversion converter)
    {
        int j=0;
        POINT2 pt=null;
        POINT2 ptGeo=null;
        ArrayList<POINT2> ptsGeo=new ArrayList();
        for(j=0;j<pts.size();j++)
        {
            pt=pts.get(j);
            ptGeo=PointPixelsToLatLong(pt,converter);
            ptsGeo.add(ptGeo);
        }
        return ptsGeo;
    }
    /**
     * 
     * @param tg
     * @return true if auto-shape
     */
    protected static boolean isAutoshape(TGLight tg)
    {
        try
        {
            int linetype=tg.get_LineType();
            switch(linetype)
            {
                //the Tasks
                case TacticalLines.BBS_RECTANGLE:
                case TacticalLines.BS_RECTANGLE:
                case TacticalLines.BS_ELLIPSE:
                case TacticalLines.PBS_ELLIPSE:
                case TacticalLines.PBS_CIRCLE:
                case TacticalLines.BS_CROSS:
                case TacticalLines.BS_BBOX:
                case TacticalLines.BBS_POINT:
                case TacticalLines.CORDONKNOCK:
                case TacticalLines.CORDONSEARCH:
                case TacticalLines.BLOCK:
                case TacticalLines.BREACH:
                case TacticalLines.BYPASS:
                case TacticalLines.CANALIZE:
                case TacticalLines.CLEAR:
                case TacticalLines.CONTAIN:
                case TacticalLines.DELAY:
                case TacticalLines.DISRUPT:
                case TacticalLines.FIX:
                case TacticalLines.FOLLA:
                case TacticalLines.FOLSP:
                case TacticalLines.ISOLATE:
                case TacticalLines.OCCUPY:
                case TacticalLines.PENETRATE:
                case TacticalLines.RIP:
                case TacticalLines.RETAIN:
                case TacticalLines.RETIRE:
                case TacticalLines.SECURE:
                case TacticalLines.SCREEN:
                case TacticalLines.COVER:
                case TacticalLines.GUARD:
                case TacticalLines.SCREEN_REVC:
                case TacticalLines.COVER_REVC:
                case TacticalLines.GUARD_REVC:
                case TacticalLines.SEIZE:
                case TacticalLines.SEIZE_REVC:
                case TacticalLines.WITHDRAW:
                case TacticalLines.WDRAWUP:
                //autoshapes which are not Tasks
                case TacticalLines.SARA:
                case TacticalLines.AC:
                case TacticalLines.MRR:
                case TacticalLines.MRR_USAS:
                case TacticalLines.SAAFR:
                case TacticalLines.LLTR:
                case TacticalLines.UAV:
                case TacticalLines.UAV_USAS:
                case TacticalLines.DECEIVE:
                case TacticalLines.PDF:
                case TacticalLines.IL:
                case TacticalLines.ATKBYFIRE:
                case TacticalLines.SPTBYFIRE:
                case TacticalLines.AMBUSH:
                case TacticalLines.CLUSTER:
                case TacticalLines.GAP:
                case TacticalLines.MNFLDBLK:
                case TacticalLines.MNFLDFIX:
                case TacticalLines.MNFLDDIS:
                case TacticalLines.TURN:
                case TacticalLines.PLANNED:
                case TacticalLines.ROADBLK:
                case TacticalLines.ESR1:
                case TacticalLines.ESR2:
                case TacticalLines.TRIP:
                case TacticalLines.EASY:
                case TacticalLines.BYDIF:
                case TacticalLines.BYIMP:
                case TacticalLines.ASLTXING:
                case TacticalLines.BRIDGE:
                case TacticalLines.FERRY:
                case TacticalLines.FORDSITE:
                case TacticalLines.FORDIF:
                case TacticalLines.MFLANE:
                case TacticalLines.RAFT:
                case TacticalLines.FOXHOLE:
                case TacticalLines.MSDZ:
                case TacticalLines.CONVOY:
                case TacticalLines.HCONVOY:
                case TacticalLines.LINTGT:
                case TacticalLines.LINTGTS:
                case TacticalLines.FPF:
                case TacticalLines.BEARING:
                case TacticalLines.ACOUSTIC:
                case TacticalLines.ELECTRO:
                case TacticalLines.OPTICAL:
                case TacticalLines.TORPEDO:
                    return true;
            }

        }
        catch (Exception exc)
        {
            ErrorLogger.LogException(_className, "isAutoshape",
                    new RendererException("Failed inside isAutoshape", exc));
        }
        return false;
    }

    protected static ArrayList<POINT2> LatLongToPixels(ArrayList<POINT2> pts, IPointConversion converter)
    {
        int j=0;
        POINT2 pt=null;
        POINT2 ptPixels=null;
        ArrayList<POINT2> ptsPixels=new ArrayList();
        for(j=0;j<pts.size();j++)
        {
            pt=pts.get(j);
            ptPixels=PointLatLongToPixels(pt,converter);
            ptsPixels.add(ptPixels);
        }
        return ptsPixels;
    }

    private static POINT2 PointLatLongToPixels(POINT2 ptLatLong,IPointConversion converter)
    {
        POINT2 pt2 = new POINT2();
        try
        {
            //Point2D pt2d=POINT2ToPoint2D(ptLatLong);
            Point2D pt2d=new Point2D.Double(ptLatLong.x,ptLatLong.y);
            //Point pt=converter.GeoToPixels(pt2d);
            Point2D pt2d2=converter.GeoToPixels(pt2d);

            //pt2=PointToPOINT2(pt);
            pt2=new POINT2(pt2d2.getX(),pt2d2.getY());
            pt2.style=ptLatLong.style;

        } catch (Exception e) {
            JavaTacticalRenderer.clsUtility.WriteFile("Error in clsUtility.PointLatLongToPixels");
        }
        return pt2;
    }

    protected static void FilterAXADPoints(TGLight tg, IPointConversion converter) {
        try {
            int lineType = tg.get_LineType();
            switch (lineType) {
                case TacticalLines.CATK:
                case TacticalLines.CATKBYFIRE:
                case TacticalLines.AAFNT:
                case TacticalLines.AXAD:
                case TacticalLines.AIRAOA:
                case TacticalLines.AAAAA:
                case TacticalLines.SPT:
                case TacticalLines.MAIN:
                    break;
                default:
                    return;
            }
            int j=0;
            ArrayList<POINT2> pts = new ArrayList();
            ArrayList<POINT2> ptsGeo = new ArrayList();
            POINT2 pt0 = tg.Pixels.get(0);
            POINT2 pt1 = tg.Pixels.get(1);

            //Point2D pt1Geo2d=converter.convertPixelsToLonLat(pt1.x, pt1.y);
            Point pt=new Point((int)pt1.x,(int)pt1.y);
            Point2D pt1Geo2d=converter.PixelsToGeo(pt);

            POINT2 pt1geo=new POINT2(pt1Geo2d.getX(),pt1Geo2d.getY());
            POINT2 ptj=null,ptjGeo=null;
            POINT2 controlPt=tg.Pixels.get(tg.Pixels.size()-1); //the control point
            POINT2 pt0Relative=lineutility.PointRelativeToLine(pt0, pt1, pt0, controlPt);
            double relativeDist=lineutility.CalcDistanceDouble(pt0Relative, controlPt);
            relativeDist += 5;
            double pt0pt1dist=lineutility.CalcDistanceDouble(pt0, pt1);
            boolean foundGoodPoint=false;
            if(relativeDist>pt0pt1dist)
            {
                //first point is too close, begin rebuilding the arrays
                //tg.Pixels.set(1, pt1);
                pts.add(pt0);
                //pt1Geo2d=converter.convertPixelsToLonLat(pt0.x, pt0.y);
                pt=new Point((int)pt0.x,(int)pt0.y);
                pt1Geo2d=converter.PixelsToGeo(pt);

                pt1geo=new POINT2(pt1Geo2d.getX(),pt1Geo2d.getY());
                //tg.LatLongs.set(1, pt1geo);
                ptsGeo.add(pt1geo);
                //create a good first point and add it to the array
                pt1=lineutility.ExtendAlongLineDouble(pt0, pt1, relativeDist);
                pts.add(pt1);

                //pt1Geo2d=converter.convertPixelsToLonLat(pt1.x, pt1.y);
                pt=new Point((int)pt1.x,(int)pt1.y);
                pt1Geo2d=converter.PixelsToGeo(pt);
                pt1geo=new POINT2(pt1Geo2d.getX(),pt1Geo2d.getY());


                //tg.LatLongs.set(1, pt1geo);
                ptsGeo.add(pt1geo);
            }
            else
            {
                //the first point is good, there is no need to do anything
                foundGoodPoint=true;
                pts=tg.Pixels;
                ptsGeo=tg.LatLongs;
                //return;
            }

            //do not add mores points to the array until we find at least one good point
            if(foundGoodPoint==false)
            {
                for(j=2;j<tg.Pixels.size()-1;j++)
                {
                    ptj=tg.Pixels.get(j);
                    ptjGeo=tg.LatLongs.get(j);
                    if(foundGoodPoint)
                    {
                       //then stuff the remainder of the arrays with the original points
                        pts.add(ptj);
                        ptsGeo.add(ptjGeo);
                    }
                    else    //no good points yet
                    {
                        //calculate the distance and continue if it is no good
                        pt0pt1dist=lineutility.CalcDistanceDouble(pt0, ptj);
                        if(relativeDist>pt0pt1dist)
                            continue;
                        else
                        {
                           //found a good point
                           pts.add(ptj);
                           ptsGeo.add(ptjGeo);
                           //set the boolean so that it will stuff the array with the rest of the points
                           foundGoodPoint=true;
                        }
                    }
                }
                //finally add the control point to the arrays and set the arrays
                pts.add(controlPt);
                //pt1Geo2d=converter.convertPixelsToLonLat(controlPt.x, controlPt.y);
                pt=new Point((int)controlPt.x, (int)controlPt.y);
                pt1Geo2d=converter.PixelsToGeo(pt);

                pt1geo=new POINT2(pt1Geo2d.getX(),pt1Geo2d.getY());
                ptsGeo.add(pt1geo);
            }   //end if foundGoodPoint is false

            //add all the successive points which are far enough apart
            POINT2 lastGoodPt=pts.get(1);
            POINT2 currentPt=null;
            POINT2 currentPtGeo=null;
            double dist=0;
            tg.Pixels=new ArrayList();
            tg.LatLongs=new ArrayList();
            for(j=0;j<2;j++)
            {
                tg.Pixels.add(pts.get(j));
                tg.LatLongs.add(ptsGeo.get(j));
            }
            for(j=2;j<pts.size()-1;j++)
            {
                currentPt=pts.get(j);
                currentPtGeo=ptsGeo.get(j);
                dist=lineutility.CalcDistanceDouble(currentPt, lastGoodPt);
                if(dist>5)
                {
                    lastGoodPt=currentPt;
                    tg.Pixels.add(currentPt);
                    tg.LatLongs.add(currentPtGeo);
                }
            }
            //add the control point
            tg.Pixels.add(pts.get(pts.size()-1));
            tg.LatLongs.add(ptsGeo.get(ptsGeo.size()-1));
        }
        catch (Exception exc) {
            ErrorLogger.LogException("clsUtility", "FilterAXADPoints",
                    new RendererException("Failed inside FilterAXADPoints", exc));

        }
        return;
    }
    /**
     *
     * @param tg
     */
    protected static void RemoveDuplicatePoints(TGLight tg)
    {
        try
        {
            //do not remove autoshape duplicate points
//            if(isAutoshape(tg))
//                return;
            switch (tg.get_LineType()) {
                case TacticalLines.UAV:
                case TacticalLines.MRR:
                case TacticalLines.LLTR:
                case TacticalLines.AC:
                case TacticalLines.SAAFR:
                    break;
                default:
                    if(isAutoshape(tg))
                        return;
            }

            //we assume tg.H to have colors if it is comma delimited.
            //only exit if colors are not set
            switch(tg.get_LineType())   //preserve segment data
            {
                case TacticalLines.CATK:
                case TacticalLines.AXAD:
                case TacticalLines.AIRAOA:
                case TacticalLines.AAAAA:
                case TacticalLines.SPT:
                case TacticalLines.AAFNT:		//40
                case TacticalLines.MAIN:
                case TacticalLines.CATKBYFIRE:	//80
                    return;
                case TacticalLines.BOUNDARY:
                case TacticalLines.MSR:
                case TacticalLines.ASR:
                    String strH=tg.get_H();
                    if(strH != null && !strH.isEmpty())
                    {
                        String[] strs=strH.split(",");
                        if(strs.length>1)
                            return;
                    }
                    break;
                default:
                    break;
            }
            int linetype=tg.get_LineType();
            if(JavaTacticalRenderer.clsUtility.IsChange1Area(linetype, null))
                return;
            
            int minSize=2;
            if(JavaTacticalRenderer.clsUtility.isClosedPolygon(tg.get_LineType()))
                minSize=3;
            
            POINT2 ptCurrent=null;
            POINT2 ptLast=null;
            for(int j=1;j<tg.Pixels.size();j++)
            {
                ptLast=new POINT2(tg.Pixels.get(j-1));
                ptCurrent=new POINT2(tg.Pixels.get(j));
                //if(ptCurrent.x==ptLast.x && ptCurrent.y==ptLast.y)
                if (Math.abs(ptCurrent.x - ptLast.x)<0.5 && Math.abs(ptCurrent.y - ptLast.y)<0.5)
                {
                    if(tg.Pixels.size()>minSize)
                    {
                        tg.Pixels.remove(j);
                        tg.LatLongs.remove(j);
                        j=1;//start over if this happens
                    }
                }
            }
        }
        catch(Exception exc)
        {
            ErrorLogger.LogException("clsUtility", "FilterVerticalSegments",
                    new RendererException("Failed inside FilterVerticalSegments", exc));

        }
        return;
    }
    /**
     * @deprecated
     * The GE renderer needs a way to process geo points in case they are passed
     * for Change 1 symbols. Uses a best fit approach to build a converter. Public
     * so that testers can use it.
     * @param latLongs geographic points
     * @return geographic to pixels coordinate converter
     */
    public static IPointConversion BestFitConverter(ArrayList<POINT2> latLongs)
    {
        IPointConversion converter=null;
        try
        {
            int j=0,pixelWidth=1000,pixelHeight=1000;
            double geoTop=Double.MIN_NORMAL,geoBottom=Double.MAX_VALUE,
                    geoLeft=Double.MAX_VALUE,geoRight=Double.MIN_NORMAL;

            for(j=0;j<latLongs.size();j++)
            {
                if(latLongs.get(j).y>geoTop)
                    geoTop=latLongs.get(j).y;
                if(latLongs.get(j).y<geoBottom)
                    geoBottom=latLongs.get(j).y;
                if(latLongs.get(j).x>geoRight)
                    geoRight=latLongs.get(j).x;
                if(latLongs.get(j).x<geoLeft)
                    geoLeft=latLongs.get(j).x;
            }
            converter=new PointConversion(pixelWidth,pixelHeight,geoTop,geoLeft,geoBottom,geoRight);
        }
        catch (Exception exc) {
            ErrorLogger.LogException("clsUtility", "BestFitConverter",
                    new RendererException("Failed inside BestFitConverter", exc));

        }
        return converter;
    }
    protected static POINT2 PointPixelsToLatLong(POINT2 ptPixels,IPointConversion converter)
    {
        POINT2 pt2 = new POINT2();
        try
        {
            //Point pt=POINT2ToPoint(ptPixels);
            Point2D pt2d=new Point2D.Double(ptPixels.x,ptPixels.y);
            //Point2D.Double pt2d=converter.PixelsToGeo(pt);
            pt2d=converter.PixelsToGeo(pt2d);
            //pt2=Point2DToPOINT2(pt2d);
            pt2=new POINT2(pt2d.getX(),pt2d.getY());
            pt2.style=ptPixels.style;
        }
        catch(Exception exc)
        {
            ErrorLogger.LogException("clsUtility" ,"PointPixelsToLatLong",
                    new RendererException("Could not convert point to geo", exc));
        }
        return pt2;
    }
    /**
     * resolves properties for shapes that need it because we don't call ResolveModifierShapes as does CPOF
     * @param tg
     * @param shapes
     */
    protected static void ResolveDummyShapes(TGLight tg, ArrayList<Shape2>shapes)
    {
        try
        {
            int shapeStyle=-1;
            Shape2 shape=null;
            switch(tg.get_LineType())
            {
                case TacticalLines.DUMMY:
                    String status=tg.get_Status();
                    for(int j=0;j<shapes.size();j++)
                    {
                        shape=shapes.get(j);
                        shapeStyle=shape.get_Style();
                        if(shapeStyle != 1)
                        {
                            shape.setFillColor(tg.get_FillColor());
                            shape.set_Fillstyle(tg.get_FillStyle());
                        }
                        else if(j==0 && status.equalsIgnoreCase("A"))
                        {
                            shape.setFillColor(tg.get_FillColor());
                            shape.set_Fillstyle(tg.get_FillStyle());                            
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        catch(Exception exc)
        {
            ErrorLogger.LogException("clsUtility" ,"ResolveDummyShapes",
                    new RendererException("ResolveDummyShapes", exc));
        }
        return;
    }
    protected static Rectangle2D getMBR(ArrayList<Point2D> clipBounds)
    {
        Rectangle2D rect=null;
        try
        {
            int j=0;
            Point2D pt=null;
            double xmax=clipBounds.get(0).getX(),xmin=xmax,ymax=clipBounds.get(0).getY(),ymin=ymax;
            for(j=0;j<clipBounds.size();j++)
            {
                pt=clipBounds.get(j);
                if(pt.getX()<xmin)
                    xmin=pt.getX();
                if(pt.getX()>xmax)
                    xmax=pt.getX();
                if(pt.getY()<=ymin)
                    ymin=pt.getY();
                if(pt.getY()>ymax)
                    ymax=pt.getY();
            }
            rect=new Rectangle2D.Double(xmin, ymin, xmax-xmin, ymax-ymin);
        }
        catch (Exception exc) {
            ErrorLogger.LogException(_className, "AddBoundaryPointsForLines",
                    new RendererException("Failed inside AddBoundaryPointsForLines", exc));
        }
        return rect;
    }
}
