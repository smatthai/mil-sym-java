/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.web.renderer.utilities;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author michael.spinelli
 */
public class LineInfo {
    
    private Color lineColor = null;
    private Color fillColor = null;
    private int lineWidth = 2;
    private Stroke stroke = null;
    
    private ArrayList<ArrayList<Point2D>> _Polylines = null;
    
    public LineInfo()
    {
        
        
    }
    
    public void setLineColor(Color value)
    {
        lineColor=value;
    }
    public Color getLineColor()
    {
        return lineColor;
    }
    
    public void setFillColor(Color value)
    {
        fillColor=value;
    }
    public Color getFillColor()
    {
        return fillColor;
    }
    
    public Stroke getStroke()
    {
        return stroke;
    }
    //client will use this to do fills (if it is not null)
/*
    public TexturePaint getTexturePaint()
    {
        return texturePaint;
    }
    public void setTexturePaint(TexturePaint value)
    {
        texturePaint=value;
    }*/

     public void setStroke(Stroke s)
    {
        stroke=s;
    }
    
    public ArrayList<ArrayList<Point2D>> getPolylines()
    {
        return _Polylines;
    }

    public void setPolylines(ArrayList<ArrayList<Point2D>> value)
    {
        _Polylines = value;
    }
    
}
