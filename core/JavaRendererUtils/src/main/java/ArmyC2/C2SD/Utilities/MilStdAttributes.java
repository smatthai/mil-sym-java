/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ArmyC2.C2SD.Utilities;

import java.util.ArrayList;

/**
 * Symbol attributes for use as url parameters
 * @author michael.spinelli
 */
public class MilStdAttributes {
     
    /*
     * Line color of the symbol. hex value.
     */
    public static final String LineColor = "LINECOLOR";
    
    /*
     * Fill color of the symbol. hex value
     */
    public static final String FillColor = "FILLCOLOR";
    
    /**
     * Used to change the icon from its normally black color.
     */
    public static final String IconColor = "ICONCOLOR";
    
    /*
     * Fill color of the symbol. hex value
     */
    public static final String TextColor = "TEXTCOLOR";
    
    /*
     * Fill color of the symbol. hex value
     */
    public static final String TextBackgroundColor = "TEXTBACKGROUNDCOLOR";
    
    /*
     * size of the single point image
     */
    public static final String PixelSize = "SIZE";
    
    /*
     * scale value to grow or shrink single point tactical graphics.
     */
    public static final String Scale = "SCALE";
    
    /**
     * defaults to true
     */
    public static final String KeepUnitRatio = "KEEPUNITRATIO";
    
    /*
     * transparency value of the symbol. values from 0-255
     */
    public static final String Alpha = "ALPHA";
    
    /*
     * outline the symbol, true/false
     */
    public static final String OutlineSymbol = "OUTLINESYMBOL";
    
    /*
     * specify and outline color rather than letting renderer picking 
     * the best contrast color. hex value
     */
    public static final String OutlineColor = "OUTLINECOLOR";
    
    /*
     * 2525B vs 2525C. 
     * like:
     * RendererSettings.Symbology_2525Bch2_USAS_13_14
     * OR
     * RendererSettings.Symbology_2525C
     */
    public static final String SymbologyStandard = "SYMSTD";
    
    public static final String Renderer = "RENDERER";
    
    public static final String LookAtTag = "LOOKAT";
    
    /**
     * AGL or MSL typically
     */
    public static final String AltitudeMode = "ALTMODE";
    
    /**
     * If false, the renderer will create a bunch of little lines to create
     * the "dash" effect (expensive but necessary for KML).  
     * If true, it will be on the user to create the dash effect using the
     * DashArray from the Stroke object from the ShapeInfo object.
     */
    public static final String UseDashArray = "USEDASHARRAY";
    
    public static final String UsePatternFill = "USEPATTERNFILL";
    
    public static final String PatternFillType = "PATTERNFILLTYPE";
    
    
    /**
     * for singlepoints, if set to "true", no labels will be drawn and you
     * will just get the core symbol.
     */
    public static final String DrawAsIcon = "ICON";
    
    public static final String HideOptionalLabels = "HIDEOPTIONALLABELS";
    
    public static ArrayList<String> GetModifierList()
    {
        ArrayList<String> list = new ArrayList<String>();
        
        list.add(LineColor);
        list.add(FillColor);
        list.add(PixelSize);
        list.add(Scale);
        list.add(KeepUnitRatio);
        list.add(Alpha);
        list.add(OutlineSymbol);
        list.add(OutlineColor);
        list.add(SymbologyStandard);
        
        return list;
    }
}
