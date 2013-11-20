/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ArmyC2.C2SD.Rendering;

import ArmyC2.C2SD.Utilities.IMultiPointRenderer;
import ArmyC2.C2SD.Utilities.IPointConversion;
import ArmyC2.C2SD.Utilities.MilStdSymbol;
import ArmyC2.C2SD.Utilities.ModifiersTG;
import ArmyC2.C2SD.Utilities.RendererSettings;
import ArmyC2.C2SD.Utilities.ShapeInfo;
import ArmyC2.C2SD.Utilities.SymbolDraw;
import ArmyC2.C2SD.Utilities.SymbolUtilities;
import RenderMultipoints.clsRenderer;
import java.util.ArrayList;

/**
 *
 * @author michael.spinelli
 */
public class MultiPointRenderer implements IMultiPointRenderer {

    private static MultiPointRenderer _instance = null;
    
    public static synchronized MultiPointRenderer getInstance()
    {
        if(_instance == null)
            _instance = new MultiPointRenderer();

        return _instance;
    }
    
    @Override
    public MilStdSymbol render(MilStdSymbol symbol, IPointConversion converter, Object clipBounds) {
        
        try
        {

            //RenderMultipoints.clsRenderer.render(symbol, converter);
            //TGLight tgl = new TGLight();
            
            //sector range fan, make sure there is a minimum distance value.
            if(SymbolUtilities.getBasicSymbolID(symbol.getSymbolID()).equals("G*F*AXS---****X"))
            {
                if(symbol.getModifiers_AM_AN_X(ModifiersTG.AN_AZIMUTH)!=null &&
                symbol.getModifiers_AM_AN_X(ModifiersTG.AM_DISTANCE)!=null)
                {
                    int anCount = symbol.getModifiers_AM_AN_X(ModifiersTG.AN_AZIMUTH).size();
                    int amCount = symbol.getModifiers_AM_AN_X(ModifiersTG.AM_DISTANCE).size();
                    ArrayList<Double> am = null;
                    if(amCount < ((anCount/2) + 1))
                    {
                        am = symbol.getModifiers_AM_AN_X(ModifiersTG.AM_DISTANCE);
                        if(am.get(0)!=0.0)
                        {
                            am.add(0, 0.0);
                        }
                    }
                }
            }

            //call that supports clipping
            

            
            ArrayList<ShapeInfo> modifiers = null;
            RenderMultipoints.clsRenderer.render(symbol, converter, clipBounds);
            modifiers = symbol.getModifierShapes();

            if(RendererSettings.getInstance().getTextBackgroundMethod()
                    != RendererSettings.TextBackgroundMethod_NONE)
            {
                modifiers = SymbolDraw.ProcessModifierBackgrounds(modifiers);
                symbol.setModifierShapes(modifiers);
            }

        }
        catch(Exception exc)
        {
            String message = "Failed to build multipoint TG";
            if(symbol != null)
                message = message + ": " + symbol.getSymbolID();
            //ErrorLogger.LogException(this.getClass().getName() ,"ProcessTGSymbol()",
            //        new RendererException(message, exc));
            System.err.println(exc.getMessage());
        }
        catch(Throwable t)
        {
            String message2 = "Failed to build multipoint TG";
            if(symbol != null)
                message2 = message2 + ": " + symbol.getSymbolID();
            //ErrorLogger.LogException(this.getClass().getName() ,"ProcessTGSymbol()",
            //        new RendererException(message2, t));
            System.err.println(t.getMessage());
        }
        
        return symbol;
    }

    @Override
    public MilStdSymbol renderWithPolylines(MilStdSymbol symbol, IPointConversion converter, Object clipBounds) {
        clsRenderer.renderWithPolylines(symbol, converter, clipBounds);
        return symbol;
    }
    
}
