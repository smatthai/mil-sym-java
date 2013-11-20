/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ArmyC2.C2SD.Utilities;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author michael.spinelli
 */
public class TacticalGraphicLookup {
    
    private ArrayList<String> alSymbolID = new ArrayList<String>();
    private ArrayList<Integer> alMapping = new ArrayList<Integer>();
    
    private Map<String, Integer> lookup = new HashMap<String, Integer>();

    private static TacticalGraphicLookup _instance = null;
    
    private TacticalGraphicLookup() 
    {
        init();
    }
    
    public static synchronized TacticalGraphicLookup getInstance()
    {
        if(_instance == null)
        {
        _instance = new TacticalGraphicLookup();
        }
        return _instance;
    }
    
    /**
    * @name init
    *
    * @desc Simply calls xmlLoaded
    *
    * @return None
    */
    private void init()
    {
        xmlLoaded();
    }
    
      /**
   * @name xmlLoaded
   *
   * @desc
   *
   * @param event - IN - Not used
   * @return None
   */
  private void xmlLoaded()
  {
    InputStream xmlStream = this.getClass().getClassLoader().getResourceAsStream("XML/TacticalGraphics.xml");
    String lookupXml = FileHandler.InputStreamToString(xmlStream);
    populateLookup(lookupXml);
  }
  
    /**
   * @name populateLookup
   *
   * @desc
   *
   * @param xml - IN -
   * @return None
   */
  private void populateLookup(String xml)
  {
    ArrayList al = XMLUtil.getItemList(xml, "<SYMBOL>", "</SYMBOL>");
    for(int i = 0; i < al.size(); i++)
    {
      String data = (String)al.get(i);

      String basicID = XMLUtil.parseTagValue(data, "<SYMBOLID>", "</SYMBOLID>");
      //String description = XMLUtil.parseTagValue(data, "<DESCRIPTION>", "</DESCRIPTION>");
      String mapping = XMLUtil.parseTagValue(data, "<MAPPING>", "</MAPPING>");

      //System.out.println(basicID + ": " + mapping);
      lookup.put(basicID, Integer.parseInt(mapping));

    }
  }
  
    /**
   * given the milstd symbol code, find the font index for the symbol.
   * @param symbolCode
   * @return
   */
  public int getCharCodeFromSymbol(String symbolCode)
  {

      try
      {

          String basicID = symbolCode;
          
          if(SymbolUtilities.isWeather(symbolCode)==false)
          {
              basicID = SymbolUtilities.getBasicSymbolID(symbolCode);
          }
          if(lookup.containsKey(basicID))
          {
              return lookup.get(basicID);
          }
          else
          {
              return -1;
          }
      }
      catch(Exception exc)
      {
          ErrorLogger.LogException("TacticalGraphicLookup", "getCharCodeFromSymbol", exc, Level.WARNING);
      }
    return -1;

  } 
    
}
