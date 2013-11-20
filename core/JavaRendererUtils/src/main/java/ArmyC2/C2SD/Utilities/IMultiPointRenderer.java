/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ArmyC2.C2SD.Utilities;

/**
 *
 * 
 */
public interface IMultiPointRenderer {


    /**
     * Takes a MilStdSymbol populated with the symbol code, coordinates,
     * and modifiers and builds & assigns the shape objects: 
     * symbolShapes & modifierShapes
     * @param symbol
     * @param converter
     * @param clipBounds Rectangle2D, Rectangle2D.Double, or ArrayList<Point2D>
     * @return 
     */
    public MilStdSymbol render(MilStdSymbol symbol, IPointConversion converter, Object clipBounds);

    /**
     * Takes a MilStdSymbol populated with the symbol code, coordinates,
     * and modifiers and builds & assigns the shape objects: 
     * symbolShapes & modifierShapes.  It additionally populates the 
     * Polylines (ArrayList<ArrayList<Point2D.Double>>)in each shapeInfo object 
     * in the SymbolShapes arraylist of ShapeInfo objects.  Useful for when not
     * rendering on a java form.  I.E., generating kml or json.
     * @param symbol
     * @param converter
     * @param clipBounds Rectangle2D, Rectangle2D.Double, or ArrayList<Point2D>
     * @return 
     */
    public MilStdSymbol renderWithPolylines(MilStdSymbol symbol, IPointConversion converter, Object clipBounds);



}
