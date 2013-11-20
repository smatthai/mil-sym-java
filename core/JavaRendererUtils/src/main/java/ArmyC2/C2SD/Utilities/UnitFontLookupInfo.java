/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ArmyC2.C2SD.Utilities;

import java.awt.Color;

/*
<SYMBOL>
  <SYMBOLID>S*G*UCRX--*****</SYMBOLID>
  <DESCRIPTION>Reconnaissance Long Range Surveillance (LRS)</DESCRIPTION>
  <MAPPING1U>1457</MAPPING1U>
  <MAPPING1F>1458</MAPPING1F>
  <MAPPING1N>1459</MAPPING1N>
  <MAPPING1H>1460</MAPPING1H>
  <MAPPING1COLOR/>
  <MAPPING2/>
  <MAPPING2COLOR/>
</SYMBOL>
*/
/**
 *
 * @author Michael.Spinelli
 */
public class UnitFontLookupInfo {

    public String _SymbolID = "";
    public String _Description = "";
    public int _mapping1 = 0;
    public int _mapping1U = 0;
    public int _mapping1F = 0;
    public int _mapping1N = 0;
    public int _mapping1H = 0;
    public int _mapping2 = 0;
    public Color _color1 = Color.BLACK;
    public Color _color2 = Color.BLACK;

    public UnitFontLookupInfo(String SymbolID, String Description, 
            int M1U, int M1F, int M1N, int M1H, Color Color1, int M2, Color Color2 )
    {
        _SymbolID = SymbolID;
        _Description = Description;

        _mapping1U = M1U;
        _mapping1F = M1F;
        _mapping1N = M1N;
        _mapping1H = M1H;
        _mapping2 = M2;

        _color1 = Color1;
        _color2 = Color2;
    }

        public UnitFontLookupInfo(String SymbolID, String Description, 
            String M1U, String M1F, String M1N, String M1H, String Color1, 
            String M2, String Color2)
    {
        _SymbolID = SymbolID;
        _Description = Description;

        if(M1U != null && !M1U.equals(""))
            _mapping1U = Integer.valueOf(M1U);
        if(M1F != null && !M1F.equals(""))
            _mapping1F = Integer.valueOf(M1F);
        if(M1N != null && !M1N.equals(""))
            _mapping1N = Integer.valueOf(M1N);
        if(M1H != null && !M1H.equals(""))
            _mapping1H = Integer.valueOf(M1H);
        if(M2 != null && !M2.equals(""))
            _mapping2 = Integer.valueOf(M2);

        Color temp = null;
        if(Color1 != null && !Color1.equals(""))
        temp = SymbolUtilities.getColorFromHexString(Color1);
        if(temp != null)
            _color1 = temp;
        if(Color2 != null && !Color2.equals(""))
        _color2 = SymbolUtilities.getColorFromHexString(Color2);
    }

    public String getBasicSymbolID()
    {
        return _SymbolID;
    }

    public String getDescription()
    {
        return _Description;
    }

    /**
     * gives correct mapping given the full
     * @param SymbolID
     * @return
     */
    public int getMapping1(String SymbolID)
    {
        char affiliation = SymbolID.charAt(1);
        if(affiliation == 'F' ||
                          affiliation == 'A' ||
                          affiliation == 'D' ||
                          affiliation == 'M' ||
                          affiliation == 'J' ||
                          affiliation == 'K')
            return _mapping1F;
        else if(affiliation == 'H' || affiliation == 'S')
            return _mapping1H;
        if(affiliation == 'N' || affiliation == 'L')
            return _mapping1N;
        else /*(affiliation == 'P' ||
                     affiliation == 'U' ||
                     affiliation == 'G' ||
                     affiliation == 'W')*/
            return _mapping1U;
    }

    public int getMapping2()
    {
        return _mapping2;
    }

    public Color getColor1()
    {
        return _color1;
    }

    public Color getColor2()
    {
        return _color2;
    }

}
