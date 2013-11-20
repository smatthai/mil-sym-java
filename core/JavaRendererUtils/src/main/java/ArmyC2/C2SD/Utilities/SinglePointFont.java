/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ArmyC2.C2SD.Utilities;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Responsible for loading the single point & unit fonts into memory
 * @author michael.spinelli
 */
public class SinglePointFont {


    static SinglePointFont _instance = null;
    //static InputStream _unitFontStream = null;
    //static InputStream _spFontStream = null;

    private SinglePointFont()
    {
        //Init();

    }

    public static synchronized SinglePointFont getInstance()
    {
        if(_instance == null)
        {
            _instance = new SinglePointFont();
           // _unitFontStream = _instance.getClass().getClassLoader().getResourceAsStream("FONTS/UnitFont.ttf");
           // _spFontStream = _instance.getClass().getClassLoader().getResourceAsStream("FONTS/SinglePoint.ttf");
        }


        return _instance;
    }

    /**
     * Font used to render force elements (units).
     * @param size
     * @return 
     */
    public Font getUnitFont(float size)
    {
        //load font from resource
        InputStream fontStream = _instance.getClass().getClassLoader().getResourceAsStream("FONTS/UnitFont.ttf");
        //InputStream fontStream = _unitFontStream;

        Font newFont = null;
        try
        {
            //create font
            newFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
        }
        catch(FontFormatException ffe)
        {
            ErrorLogger.LogException(this.getClass().getName() ,"getUnitFont()",
                    new RendererException("UnitFont failed to load.", ffe));
        }
        catch(IOException ioe)
        {
            ErrorLogger.LogException(this.getClass().getName() ,"getUnitFont()",
                    new RendererException("UnitFont failed to load.", ioe));
        }
        catch(Exception exc)
        {
            ErrorLogger.LogException(this.getClass().getName() ,"getUnitFont()",
                    new RendererException("UnitFont failed to load.", exc));
        }

        //resize font
        newFont = newFont.deriveFont(Font.TRUETYPE_FONT, size);
        //return font
        return newFont;

    }

    /**
     * Font used to render single point tactical graphics
     * @param size
     * @return 
     */
    public Font getSPFont(float size)
    {
        //load font from resource
        InputStream fontStream = _instance.getClass().getClassLoader().getResourceAsStream("FONTS/SinglePoint.ttf");
        //InputStream fontStream = _spFontStream;

        Font newFont = null;
        try
        {
            //create font
            newFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
        }
        catch(FontFormatException ffe)
        {
            ErrorLogger.LogException(this.getClass().getName() ,"getSPFont()",
                    new RendererException("SPFont failed to load.", ffe));
        }
        catch(IOException ioe)
        {
            ErrorLogger.LogException(this.getClass().getName() ,"getSPFont()",
                    new RendererException("SPFont failed to load.", ioe));
        }
        catch(Exception exc)
        {
            ErrorLogger.LogException(this.getClass().getName() ,"getSPFont()",
                    new RendererException("SPFont failed to load.", exc));
        }

        //resize font
        newFont = newFont.deriveFont(Font.TRUETYPE_FONT, size);
        //return font
        return newFont;

    }
    
    /**
     * Font used to make icons of multipoint tactical graphics
     * @param size
     * @return 
     */
    public Font getTGFont(float size)
    {
        //load font from resource
        InputStream fontStream = _instance.getClass().getClassLoader().getResourceAsStream("FONTS/TacticalGraphics.ttf");
        //InputStream fontStream = _spFontStream;

        Font newFont = null;
        try
        {
            //create font
            newFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
        }
        catch(FontFormatException ffe)
        {
            ErrorLogger.LogException(this.getClass().getName() ,"getTGFont()",
                    new RendererException("TGFont failed to load.", ffe));
        }
        catch(IOException ioe)
        {
            ErrorLogger.LogException(this.getClass().getName() ,"getTGFont()",
                    new RendererException("TGFont failed to load.", ioe));
        }
        catch(Exception exc)
        {
            ErrorLogger.LogException(this.getClass().getName() ,"getTGFont()",
                    new RendererException("TGFont failed to load.", exc));
        }

        //resize font
        newFont = newFont.deriveFont(Font.TRUETYPE_FONT, size);
        //return font
        return newFont;

    }

}
