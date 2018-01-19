/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ArmyC2.C2SD.Utilities;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author michael.spinelli
 */
public class UnitDefTable {


    private static UnitDefTable _instance = null;
    //private static SymbolTableThingy
    private static Map<String, UnitDef> _UnitDefinitionsB = null;
    private static ArrayList<UnitDef> _UnitDefDupsB = null;
    
    private static Map<String, UnitDef> _UnitDefinitionsC = null;
    private static ArrayList<UnitDef> _UnitDefDupsC = null;

    private static String propSymbolID = "SYMBOLID";
    private static String propDrawCategory = "DRAWCATEGORY";
    private static String propModifiers = "MODIFIERS";
    private static String propDescription = "DESCRIPTION";
    private static String propHierarchy = "HIERARCHY";
    private static String propAlphaHierarchy = "ALPHAHIERARCHY";
    private static String propPath = "PATH";


    private UnitDefTable()
    {
        Init();
    }

    public static synchronized UnitDefTable getInstance()
    {
      if(_instance == null)
          _instance = new UnitDefTable();

      return _instance;
    }

   /* public String[] searchByHierarchy(String hierarchy)
    {
        for(UnitDef foo : _UnitDefinitions.values() )
        {
            if(foo.getHierarchy().equalsIgnoreCase(hierarchy))
            {
                return
            }
        }
    }*/

  private void Init()
  {
        _UnitDefinitionsB = new HashMap<String, UnitDef>();
        _UnitDefDupsB = new ArrayList<UnitDef>();

        _UnitDefinitionsC = new HashMap<String, UnitDef>();
        _UnitDefDupsC = new ArrayList<UnitDef>();
          
        String xmlPathB = "XML/UnitConstantsB.xml";
      
        String xmlPathC = "XML/UnitConstantsC.xml";
      
      
    InputStream xmlStreamB = this.getClass().getClassLoader().getResourceAsStream(xmlPathB);
    InputStream xmlStreamC = this.getClass().getClassLoader().getResourceAsStream(xmlPathC);
    
    String lookupXmlB = FileHandler.InputStreamToString(xmlStreamB);
    String lookupXmlC = FileHandler.InputStreamToString(xmlStreamC);
    //String lookupXml = FileHandler.fileToString("C:\\UnitFontMappings.xml");
    populateLookup(lookupXmlB, RendererSettings.Symbology_2525B);
    populateLookup(lookupXmlC, RendererSettings.Symbology_2525C);
  }

  private void populateLookup(String xml, int symStd)
  {
     UnitDef ud = null;
    ArrayList al = XMLUtil.getItemList(xml, "<SYMBOL>", "</SYMBOL>");
    for(int i = 0; i < al.size(); i++)
    {
      String data = (String)al.get(i);
      String symbolID = XMLUtil.parseTagValue(data, "<SYMBOLID>", "</SYMBOLID>");
      String description = XMLUtil.parseTagValue(data, "<DESCRIPTION>", "</DESCRIPTION>");
      description = description.replaceAll("&amp;", "&");
      String drawCategory = XMLUtil.parseTagValue(data, "<DRAWCATEGORY>", "</DRAWCATEGORY>");
      String hierarchy = XMLUtil.parseTagValue(data, "<HIERARCHY>", "</HIERARCHY>");
      String alphaHierarchy = XMLUtil.parseTagValue(data, "<ALPHAHIERARCHY>", "</ALPHAHIERARCHY>");
      String path = XMLUtil.parseTagValue(data, "<PATH>", "</PATH>");

      ud = new UnitDef();


      if(SymbolUtilities.isInstallation(symbolID))
            symbolID = symbolID.substring(0, 10) + "H****";

      ud.setBasicSymbolId(symbolID);
      ud.setDescription(description);
      if(drawCategory == null || drawCategory.equals(""))
          ud.setDrawCategory(Integer.valueOf(0));
      else
          ud.setDrawCategory(Integer.valueOf(drawCategory));
      ud.setHierarchy(hierarchy);
      ud.setAlphaHierarchy(alphaHierarchy);
      ud.setFullPath(path);


      boolean isMCSSpecificFE = SymbolUtilities.isMCSSpecificForceElement(ud);
      
      if(symStd == RendererSettings.Symbology_2525B)
      {
        if(_UnitDefinitionsB.containsKey(symbolID)==false && isMCSSpecificFE==false)
            _UnitDefinitionsB.put(symbolID, ud);//EMS have dupe symbols with same code
        else if(isMCSSpecificFE==false)
            _UnitDefDupsB.add(ud);
      }
      else
      {
          if(_UnitDefinitionsC.containsKey(symbolID)==false && isMCSSpecificFE==false)
            _UnitDefinitionsC.put(symbolID, ud);//EMS have dupe symbols with same code
        else if(isMCSSpecificFE==false)
            _UnitDefDupsC.add(ud);
      }
      
    }//end for
  
  }//end populateLookup

    /**
     * @name getSymbolDef
     *
     * @desc Returns a SymbolDef from the SymbolDefTable that matches the passed in Symbol Id
     *
     * @param basicSymbolID - IN - A 15 character MilStd code
     * @param symStd 0 for 2525Bch2, 1 for 2525C
     * @return SymbolDef whose Symbol Id matches what is passed in
     */
    public UnitDef getUnitDef(String basicSymbolID, int symStd)
    {
        UnitDef returnVal = null;
        if(symStd==RendererSettings.Symbology_2525B)
        {
            returnVal = (UnitDef)_UnitDefinitionsB.get(basicSymbolID);
            if(returnVal == null)
            {
                basicSymbolID = basicSymbolID.replace("*****","H****");
                returnVal = _UnitDefinitionsB.get(basicSymbolID);
            }
        }
        else if(symStd==RendererSettings.Symbology_2525C)
        {
            returnVal = (UnitDef)_UnitDefinitionsC.get(basicSymbolID);
            if(returnVal == null)
            {
                basicSymbolID = basicSymbolID.replace("*****","H****");
                returnVal = _UnitDefinitionsC.get(basicSymbolID);
            }
        }
        return returnVal;
    }



    /**
     *
     * @return
     */
    public Map<String, UnitDef> GetAllUnitDefs(int symStd)
    {
        if(symStd==RendererSettings.Symbology_2525B)
            return _UnitDefinitionsB;
        else
            return _UnitDefinitionsC;
    }
    
    /**
     * SymbolIDs are no longer unique thanks to 2525C and some EMS symbols.
     * Here are the EMS symbols that reused symbol IDs.
     * Like how EMS.INCDNT.CVDIS.DISPOP uses the same symbol code as STBOPS.ITM.RFG (O*I*R-----*****)
     * @param symStd
     * @return 
     */
    public ArrayList<UnitDef> GetUnitDefDups(int symStd)
    {
        if(symStd==RendererSettings.Symbology_2525B)
            return _UnitDefDupsB;
        else
            return _UnitDefDupsC;
    }
    
    

    /**
     * 
     * @param basicSymbolID
     * @return
     */
    public Boolean HasUnitDef(String basicSymbolID, int symStd)
    {
        if(basicSymbolID != null && basicSymbolID.length() == 15)
        {
            if(symStd==RendererSettings.Symbology_2525B)
                return _UnitDefinitionsB.containsKey(basicSymbolID);
            else if(symStd==RendererSettings.Symbology_2525C)
                return _UnitDefinitionsC.containsKey(basicSymbolID);
            else
                return false;
        }
        else
            return false;
    }

}
