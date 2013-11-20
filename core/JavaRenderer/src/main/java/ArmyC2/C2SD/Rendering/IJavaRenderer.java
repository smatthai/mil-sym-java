/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ArmyC2.C2SD.Rendering;

import ArmyC2.C2SD.Utilities.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.TextAttribute;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author michael.spinelli
 */
public interface IJavaRenderer {

    /**
 * The Interface to the Render
 *
 * usage:
 * IJavaRenderer foo = (IJavaRenderer)JavaRenderer.getInstance();
 * @author michael.spinelli
 */


    /**
     * determines size of units (force elements)
     * @param size JavaRenderer.SymbolSizeMedium or positive any integer
     */
    public void setUnitSymbolSize(int size);

    /**
     * Returns size currently being used to draw units
     */
    public int getUnitSymbolSize();

    /**
     * determines size of single  point tactical graphics
     * @param size like JavaRenderer.SymbolSizeMedium or any positive integer
     */
    public void setSinglePointTGSymbolSize(int size);

    /**
     * Returns size currently being used to draw single point Tactical Graphics
     */
    public int getSinglePointTGSymbolSize();
    
    /**
     * Set the label font to be used in the renderer
     * Default tracking to TextAttribute.TRACKING_LOOSE
     * and kerning to off.
     * @param name like "arial"
     * @param type like Font.BOLD
     * @param size like 12
     */
    public void setModifierFont(String name, int type, int size);
    
    /**
     * Set the label font to be used in the renderer
     * @param name like "arial"
     * @param type like Font.BOLD
     * @param size like 12
     * @param tracking like TextAttribute.TRACKING_LOOSE (0.04f)
     * @param kerning default false.
     */
    public void setModifierFont(String name, int type, int size, float tracking, Boolean kerning);
    

    /**
     * Get a Map of the supported Unit or Force Element symbols
     * @param symStd RendererSettings.Symbology_2525C
     * @return a Map of UnitDefs keyed by symbol code.
     */
    public Map<String, UnitDef> getSupportedFETypes(int symStd);
    
     /**
     * Get a Map of the supported Tactical Graphic symbols.
     * @param symStd RendererSettings.Symbology_2525C
     * @return a Map of SymbolDefs keyed by symbol code.
     */
    public Map<String, SymbolDef> getSupportedTGTypes(int symStd);

    /**
     * Checks if the Renderer can build the symbol with the given information
     * @param symbol
     * @return true if symbol can be rendered based on provided information
     */
    public Boolean CanRender(MilStdSymbol symbol);
    
    /**
     * Checks if the Renderer can build the symbol with the given information
     * @param symbolCode
     * @param coords
     * @return true if symbol can be rendered based on provided information
     */
    public Boolean CanRender(String symbolCode, ArrayList<Point2D.Double> coords);



    /**
     * Checks if the Renderer can build the symbol with the given information
     * @param symbolCode
     * @param coords
     * @param symStd Like RendererSettings.Symbology_2525C
     * @return true if symbol can be rendered based on provided information
     */
    public Boolean CanRender(String symbolCode, ArrayList<Point2D.Double> coords, int symStd);

    /**
     * Takes a MilStdSymbol populated with the symbol code, coordinates,
     * and modifier and build & assigns the shape objects
     * @param symbol
     * @param converter does point conversion between pixels & lat/lon coordinates.
     * @return drawable symbol populated with shape data
     * @throws TBCRendererException
     */
    public MilStdSymbol Render(MilStdSymbol symbol, IPointConversion converter, Rectangle2D clipBounds) throws RendererException;

    /**
     * Takes a List of MilStdSymbol's populated with the symbol code, coordinates,
     * and modifier and build & assigns the shape objects
     * @param symbols
     * @param converter does point conversion between pixels & lat/lon coordinates.
     * @param clipBounds Dimensions of the drawing surface.  Will not do
     * clipping if NULL is passed.
     * @return drawable symbols populated with shape data
     * @throws TBCRendererException
     */
    public ArrayList<MilStdSymbol> Render(ArrayList<MilStdSymbol> symbols, IPointConversion converter, Rectangle2D clipBounds) throws RendererException;

    /**
     * Takes info needed to draw a symbol and returns a full populated MilStdSymbol.
     * @param symbolCode
     * @param UUID unique ID for the symbol.  For client use.
     * @param coords
     * @param Modifiers keyed by ModifiersUnits or ModifiersTG (ModifiersTG.C_QUANTITY)
     * @param converter does point conversion between pixels & lat/lon coordinates.
     * @param clipBounds Dimensions of the drawing surface.  Will not do
     * clipping if NULL is passed.
     * @return drawable symbol populated with shape data
     * @throws TBCRendererException
     */
    public MilStdSymbol Render(String symbolCode, String UUID, ArrayList<Point2D.Double> coords, Map<String,String> Modifiers, IPointConversion converter, Rectangle2D clipBounds) throws RendererException;

    /**
     * Returns an icon representing a milstd symbol.  Useful for adding icons
     * in tree controls.
     * Does not work for multipoints currently.
     * @param symbolID 15 character symbol ID
     * @param iconSize if 15, returned icon will be sized for 15x15 pixels
     * @param showDisplayModifiers Things like echelon, mobility, or affiliation modifiers
     * @return 
     */
    public BufferedImage RenderMilStdSymbolAsIcon(String symbolID, int iconSize, Boolean showDisplayModifiers);
    
    /**
     * Requires less information than RenderMilStdSymbolAsImageInfo.
     * Can only be used for singlepoint graphics.
     * @param symbolCode
     * @param Modifiers keyed on values from ModifiersUnits, ModifiersTG, and 
     * MilStdAttributes.
     * @param unitSize 35 would make a an image where the core symbol is 35x35.
     * label modifiers and display modifiers may fall outside of that area and
     * final image may be bigger than 35x35.
     * use getSinglePointTGSymbolSize or getUnitSymbolSize. 
     * @param keepUnitRatio Recommend setting to true when drawing on a map.
     * Only applies to force elements (units).  If KeepUnitRatio is set,
     * Symbols will be drawn with respect to each other.  Unknown unit
     * is the all around biggest, neutral unit is the smallest. if size is
     * 35, neutral would be (35/1.5)*1.1=25.7
     * @return ImageInfo, which has the image and all the information needed to 
     * position it properly.
     */
    public ImageInfo RenderSinglePointAsImageInfo(String symbolCode, Map<String, String> Modifiers, int unitSize, boolean keepUnitRatio);

    /**
     * Does the actual drawing of the Symbol.  MilstdSymbol need to be properly 
     * populated via the Render call first. Draws to an offscreen image that blits the result
     * to the desination Graphics2D object.
     * @param symbol
     * @param destination
     * @param clip Cannot be null. This function does not apply it to the destination object.
     * It makes use of the dimensions for the back buffer
     */
    public void DrawDB(MilStdSymbol symbol, Graphics2D destination, Rectangle clip) throws RendererException;

    /**
     * Does the actual drawing of the Symbol.  MilstdSymbol need to be properly 
     * populated via the Render call first. Draws to an offscreen image that blits the result
     * to the desination Graphics2D object.
     * @param symbols
     * @param destination
     * @param clip Cannot be null. This function does not apply it to the destination object.
     * It makes use of the dimensions for the back buffer
     */
    public void DrawDB(ArrayList<MilStdSymbol> symbols, Graphics2D destination, Rectangle clip) throws RendererException;

    /**
     * Does the actual drawing of the Symbol.  MilstdSymbol need to be properly 
     * populated via the Render call first.
     * @param symbol
     * @param destination
     */
    public void Draw(MilStdSymbol symbol, Graphics2D destination) throws RendererException;

    /**
     * Does the actual drawing of the Symbol.  MilstdSymbol need to be properly
     * populated via the Render call first.
     * @param symbols
     * @param destination
     */
    public void Draw(ArrayList<MilStdSymbol> symbols, Graphics2D destination) throws RendererException;

}
