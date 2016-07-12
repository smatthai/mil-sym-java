/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ArmyC2.C2SD.Utilities;

/**
 *
 * @author michael.spinelli
 */
public class SymbolDef {

    String _strBasicSymbolId = "";
    String _strDescription = "";
    String _strSymbolType = "";
    String _strGeometry = "";
    String _strDrawCategory = "";
    int _intDrawCategory = 99;
    int _intMinPoints = 0;
    int _intMaxPoints = 0;
    String _strModifiers = "";
    String _strHierarchy = "";
    String _strFullPath = "";
    



    /**
     * Just a category in the milstd hierarchy.
     * Not something we draw.
     * WILL NOT RENDER
     */
    static public final int DRAW_CATEGORY_DONOTDRAW = 0;

    /**
     * A polyline, a line with n number of points.
     * 0 control points
     */
    static public final int DRAW_CATEGORY_LINE = 1;

    /**
     * An animated shape, uses the animate function to draw.
     * 0 control points (every point shapes symbol)
     */
    static public final int DRAW_CATEGORY_AUTOSHAPE = 2;

    /**
     * An enclosed polygon with n points
     * 0 control points
     */
    static public final int DRAW_CATEGORY_POLYGON = 3;
    /**
     * A polyline with n points (entered in reverse order)
     * 0 control points
     */
    static public final int DRAW_CATEGORY_ARROW = 4;
    /**
     * A graphic with n points whose last point defines the width of the graphic.
     * 1 control point
     */
    static public final int DRAW_CATEGORY_ROUTE = 5;
    /**
     * A line defined only by 2 points, and cannot have more.
     * 0 control points
     */
    static public final int DRAW_CATEGORY_TWOPOINTLINE = 6;
    /**
     * Shape is defined by a single point
     * 0 control points
     */
    static public final int DRAW_CATEGORY_POINT = 8;
    /**
     * A polyline with 2 points (entered in reverse order).
     * 0 control points
     */
    static public final int DRAW_CATEGORY_TWOPOINTARROW = 9;
    /**
     * An animated shape, uses the animate function to draw. Super Autoshape draw
     * in 2 phases, usually one to define length, and one to define width.
     * 0 control points (every point shapes symbol)
     *
     */
    static public final int DRAW_CATEGORY_SUPERAUTOSHAPE = 15;
     /**
     * Circle that requires 1 AM modifier value.
     * See ModifiersTG.java for modifier descriptions and constant key strings.
     */
    static public final int DRAW_CATEGORY_CIRCULAR_PARAMETERED_AUTOSHAPE = 16;
    /**
     * Rectangle that requires 2 AM modifier values and 1 AN value.";
     * See ModifiersTG.java for modifier descriptions and constant key strings.
     */
    static public final int DRAW_CATEGORY_RECTANGULAR_PARAMETERED_AUTOSHAPE = 17;
    /**
     * Requires 2 AM values and 2 AN values per sector.  
     * The first sector can have just one AM value although it is recommended 
     * to always use 2 values for each sector.  X values are not required
     * as our rendering is only 2D for the Sector Range Fan symbol.
     * See ModifiersTG.java for modifier descriptions and constant key strings.
     */
    static public final int DRAW_CATEGORY_SECTOR_PARAMETERED_AUTOSHAPE = 18;
    /**
     *  Requires at least 1 distance/AM value"
     *  See ModifiersTG.java for modifier descriptions and constant key strings.
     */
    static public final int DRAW_CATEGORY_CIRCULAR_RANGEFAN_AUTOSHAPE = 19;
    /**
     * Requires 1 AM value.
     * See ModifiersTG.java for modifier descriptions and constant key strings.
     */
    static public final int DRAW_CATEGORY_TWO_POINT_RECT_PARAMETERED_AUTOSHAPE = 20;
    
    /**
     * 3D airspace, not a milstd graphic.
     */
    static public final int DRAW_CATEGORY_3D_AIRSPACE = 40;
    
    /**
     * UNKNOWN.
     */
    static public final int DRAW_CATEGORY_UNKNOWN = 99;

    public void SymbolDef()
    {

    }



    /**
     * The basic 15 character basic symbol Id.
     */

    public String getBasicSymbolId()
    {
            return _strBasicSymbolId;
    }

    /**
     * 
     */
    public void setBasicSymbolId(String value)
    {
            _strBasicSymbolId = value;
    }

    /**
     * The description of this tactical graphic.  Typically the name of the tactical graphic in MIL-STD-2525B.
     */
    public String getDescription()
    {
            return _strDescription;
    }

    /**
     * 
     */
    public void setDescription(String value)
    {
            _strDescription = value;
    }

    /**
     * What kind of symbol is it? (Bridge, Critical Point, Check Point, Road, Route, etc)
     * @deprecated 
     */
    public String getSymbolType()
    {
            return _strSymbolType;
    }

    /**
     * What kind of symbol is it? (Bridge, Critical Point, Check Point, Road, Route, etc)
     * @deprecated 
     */
    public String setSymbolType(String value)
    {
            return _strSymbolType = value;
    }

    /**
     * 
     */
    public String getGeometry()
    {
            return _strGeometry;
    }

    /**
     * 
     */
    public void setGeometry(String value)
    {
            _strGeometry = value;
    }

    /**
     * How does this draw? (autoshape, superautoshape, polygon)
     * 
     */
    public int getDrawCategory()
    {
            return _intDrawCategory;
    }

    /**
     * 
     */
    public void setDrawCategory(int value)
    {
            _intDrawCategory = value;
    }
    
    /**
     * Defines the minimum points fields.
     */
    public int getMinPoints()
    {
            return _intMinPoints;
    }

    /**
     * 
     */
    public void setMinPoints(int value)
    {
            _intMinPoints = value;
    }

    /**
     * Defines the maximum points fields.
     */
    public int getMaxPoints()
    {
            return _intMaxPoints;
    }

    public Boolean isMultiPoint()
    {
        char codingScheme = _strBasicSymbolId.charAt(0);
        Boolean returnVal = false;
        if (codingScheme == 'G' || codingScheme == 'W') 
        {

            if(_intMaxPoints > 1)
            {
                returnVal = true;
            }
            else
            {
                switch(_intDrawCategory)
                {
                    case SymbolDef.DRAW_CATEGORY_RECTANGULAR_PARAMETERED_AUTOSHAPE:
                    case SymbolDef.DRAW_CATEGORY_SECTOR_PARAMETERED_AUTOSHAPE:
                    case SymbolDef.DRAW_CATEGORY_TWO_POINT_RECT_PARAMETERED_AUTOSHAPE: 
                    case SymbolDef.DRAW_CATEGORY_CIRCULAR_PARAMETERED_AUTOSHAPE:
                    case SymbolDef.DRAW_CATEGORY_CIRCULAR_RANGEFAN_AUTOSHAPE:
                    case SymbolDef.DRAW_CATEGORY_ROUTE:
                        returnVal = true;
                        break;
                    default:
                        returnVal = false;
                }
            }
            return returnVal;
        } 
        else if(_strBasicSymbolId.startsWith("BS_") || _strBasicSymbolId.startsWith("BBS_") || _strBasicSymbolId.startsWith("PBS_"))
        {
            return true;
        }
        else 
        {
            return false;
        }
    }
    
    /**
     * 
     */
    public void setMaxPoints(int value)
    {
            _intMaxPoints = value;
    }

    /**
     * Checks if the symbol has required modifiers that influence the shape of the symbol.
     * @deprecated  Use the draw category to determine if the symbol has a 
     * channel width point or required modifiers (AM or AN) that affect how
     * the symbol is rendered.
     * @return 
     */
    public Boolean HasWidth()
    {
        Boolean returnVal = false;
        switch(_intDrawCategory)
        {
            case SymbolDef.DRAW_CATEGORY_RECTANGULAR_PARAMETERED_AUTOSHAPE:
            case SymbolDef.DRAW_CATEGORY_SECTOR_PARAMETERED_AUTOSHAPE:
            case SymbolDef.DRAW_CATEGORY_TWO_POINT_RECT_PARAMETERED_AUTOSHAPE: 
            case SymbolDef.DRAW_CATEGORY_CIRCULAR_PARAMETERED_AUTOSHAPE:
            case SymbolDef.DRAW_CATEGORY_CIRCULAR_RANGEFAN_AUTOSHAPE:
            case SymbolDef.DRAW_CATEGORY_ROUTE:
                returnVal = true;
                break;
            case SymbolDef.DRAW_CATEGORY_LINE:
                if(_strModifiers.indexOf(ModifiersTG.AM_DISTANCE + ".")>-1)//air corridor
                    returnVal = true;
                break;
            default:
                returnVal = false;
        }
        return returnVal;
    }
    

    /**
     *
     */
    public String getModifiers()
    {
            return _strModifiers;
    }

    /**
     * 
     */
    public void setModifiers(String value)
    {
            _strModifiers = value;
    }

    /**
     * Defines where the symbol goes in the ms2525 hierarchy.
     * 2.X.etc...
     */
    public String getHierarchy()
    {
            return _strHierarchy;
    }

    /**
     *
     */
    public void setHierarchy(String value)
    {
            _strHierarchy = value;
    }

     /**
     * Defines where the symbol goes in the ms2525 hierarchy.
      * TacticalGraphics/Areas/stuff...
     */
    public String getFullPath()
    {
            return _strFullPath;
    }

    /**
     *
     */
    public void setFullPath(String value)
    {
            _strFullPath = value;
    }

}
