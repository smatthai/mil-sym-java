/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ArmyC2.C2SD.Utilities;

/*import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;*/
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for loading tactical graphic symbol definitions into a hash table.
 *
 * @author michael.spinelli
 */
@SuppressWarnings("unused")
public class SymbolDefTable {



    private static SymbolDefTable _instance = null;
    //private static SymbolTableThingy
    private static Map<String, SymbolDef> _SymbolDefinitionsB = null;
    private static ArrayList<SymbolDef> _SymbolDefDupsB = null;
    
    private static Map<String, SymbolDef> _SymbolDefinitionsC = null;
    private static ArrayList<SymbolDef> _SymbolDefDupsC = null;
    
	private static String propSymbolID = "SYMBOLID";
    private static String propGeometry = "GEOMETRY";
    private static String propDrawCategory = "DRAWCATEGORY";
    private static String propMaxPoint = "MAXPOINTS";
    private static String propMinPoints = "MINPOINTS";
    private static String propHasWidth = "HASWIDTH";
    private static String propModifiers = "MODIFIERS";
    private static String propDescription = "DESCRIPTION";
    private static String propHierarchy = "HIERARCHY";

    /*
     * Holds SymbolDefs for all symbols.  (basicSymbolID, Description,
     * MinPoint, MaxPoints, etc...)
     * Call getInstance().
     *
     * */
    private SymbolDefTable()
    {
        Init();

    }

    public static synchronized SymbolDefTable getInstance()
    {
        if(_instance == null)
            _instance = new SymbolDefTable();

        return _instance;
    }

    private void Init()
    {
        _SymbolDefinitionsB = new HashMap<String, SymbolDef>();
        _SymbolDefDupsB = new ArrayList<SymbolDef>();
        
        _SymbolDefinitionsC = new HashMap<String, SymbolDef>();
        _SymbolDefDupsC = new ArrayList<SymbolDef>();
        
        String xmlPathB = "XML/SymbolConstantsB.xml";
        String xmlPathC = "XML/SymbolConstantsC.xml";

        InputStream xmlStreamB = this.getClass().getClassLoader().getResourceAsStream(xmlPathB);
        String lookupXmlB = FileHandler.InputStreamToString(xmlStreamB);
        
        InputStream xmlStreamC = this.getClass().getClassLoader().getResourceAsStream(xmlPathC);
        String lookupXmlC = FileHandler.InputStreamToString(xmlStreamC);
        
        populateLookup(lookupXmlB, RendererSettings.Symbology_2525Bch2_USAS_13_14);
        populateLookup(lookupXmlC, RendererSettings.Symbology_2525C);
    }

  private void populateLookup(String xml, int symStd)
  {
     SymbolDef sd = null;
    ArrayList<String> al = XMLUtil.getItemList(xml, "<SYMBOL>", "</SYMBOL>");
    for(int i = 0; i < al.size(); i++)
    {
      String data = (String)al.get(i);
      String symbolID = XMLUtil.parseTagValue(data, "<SYMBOLID>", "</SYMBOLID>");
      String geometry = XMLUtil.parseTagValue(data, "<GEOMETRY>", "</GEOMETRY>");
      String drawCategory = XMLUtil.parseTagValue(data, "<DRAWCATEGORY>", "</DRAWCATEGORY>");
      String maxpoints = XMLUtil.parseTagValue(data, "<MAXPOINTS>", "</MAXPOINTS>");
      String minpoints = XMLUtil.parseTagValue(data, "<MINPOINTS>", "</MINPOINTS>");
      String haswidth = XMLUtil.parseTagValue(data, "<HASWIDTH>", "</HASWIDTH>");
      String modifiers = XMLUtil.parseTagValue(data, "<MODIFIERS>", "</MODIFIERS>");
      String description = XMLUtil.parseTagValue(data, "<DESCRIPTION>", "</DESCRIPTION>");
      description = description.replaceAll("&amp;", "&");
      String hierarchy = XMLUtil.parseTagValue(data, "<HIERARCHY>", "</HIERARCHY>");
      //String alphaHierarchy = XMLUtil.parseTagValue(data, "<ALPHAHIERARCHY>", "</ALPHAHIERARCHY>");
      String path = XMLUtil.parseTagValue(data, "<PATH>", "</PATH>");

      sd = new SymbolDef();
      sd.setBasicSymbolId(symbolID);
      sd.setDescription(description);
      sd.setDrawCategory(Integer.valueOf(drawCategory));
      sd.setHierarchy(hierarchy);
      sd.setGeometry(geometry);
      sd.setMinPoints(Integer.valueOf(minpoints));
      sd.setMaxPoints(Integer.valueOf(maxpoints));
      sd.setModifiers(modifiers);
      sd.setFullPath(path);
      if(haswidth.equalsIgnoreCase("yes"))
        sd.HasWidth(Boolean.TRUE);

      boolean isMCSSpecific = SymbolUtilities.isMCSSpecificTacticalGraphic(sd);
      if(symStd==RendererSettings.Symbology_2525Bch2_USAS_13_14)
      {
        if(_SymbolDefinitionsB.containsKey(symbolID)==false && isMCSSpecific==false)
            _SymbolDefinitionsB.put(symbolID, sd);
        else if(isMCSSpecific==false)
            _SymbolDefDupsB.add(sd);
      }
      else if(symStd==RendererSettings.Symbology_2525C)
      {
        if(_SymbolDefinitionsC.containsKey(symbolID)==false && isMCSSpecific==false)
            _SymbolDefinitionsC.put(symbolID, sd);
        else if(isMCSSpecific==false)
            _SymbolDefDupsC.add(sd);
      }
    }

  }
/*
    private void Init()
    {
        try
        {

            InputStream xmlStream = this.getClass().getClassLoader().getResourceAsStream("XML/SymbolConstants.xml");

            _SymbolDefinitions = new HashMap<String, SymbolDef>();
            // Create a DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //File xmlFile = new File("D:/xml/symbolconstants.xml");//("xml/SymbolConstants.xml");
            // Create a DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Parse an XML document
            Document document = null;
            //document = builder.parse(xmlFile);

            if(xmlStream != null)
                document = builder.parse(xmlStream);

            //build map containing symbol definitions
            ProcessXML(document);

            //System.out.println("success! " + String.valueOf(_SymbolDefinitions.size()));

        } catch (SAXException e) {
                System.out.println(e.getMessage());

        } catch (ParserConfigurationException e) {
                System.out.println(e.getMessage());

        } catch (IOException e) {
                System.out.println(e.getMessage());
        }
    }

    private static void ProcessXML(Document doc)
    {
        doc.normalizeDocument();
        //Node SymbolConstants = doc.getFirstChild();

        //System.out.println(SymbolConstants.getNodeName());//SYMBOLCONSTANTS

        //NodeList symbols = SymbolConstants.getChildNodes();
        NodeList symbols = doc.getElementsByTagName("SYMBOL");
        NodeList properties = null;

        Node temp = null;
        Node symbol = null;

        SymbolDef newSymbolDef = null;
        int lcv = symbols.getLength();

        String name = null;
        String value = "";

        //loop through symbols
        for (int i = 0; i < lcv; i++)
        {
            newSymbolDef = new SymbolDef();
            symbol = symbols.item(i);
            properties = symbol.getChildNodes();

            Element eSymbol = null;
            Element property = null;
            //NodeList properties = null;
            if(symbol.getNodeType() == Node.ELEMENT_NODE)
            {
               //symbol element
               eSymbol = (Element)symbol;

               //get properties
               //SymbolID
               properties = eSymbol.getElementsByTagName(propSymbolID);
               property = (Element)properties.item(0);
               properties = property.getChildNodes();
               temp = properties.item(0);
               if(temp != null)
                    newSymbolDef.setBasicSymbolId(temp.getNodeValue());
               else
                    newSymbolDef.setBasicSymbolId("");

               //geometry
               properties = eSymbol.getElementsByTagName(propGeometry);
               property = (Element)properties.item(0);
               properties = property.getChildNodes();
               temp = properties.item(0);
               if(temp != null)
                    newSymbolDef.setGeometry(temp.getNodeValue());
               else
                    newSymbolDef.setGeometry("");

               //draw category
               properties = eSymbol.getElementsByTagName(propDrawCategory);
               property = (Element)properties.item(0);
               properties = property.getChildNodes();
               temp = properties.item(0);
               if(temp != null)
                    newSymbolDef.setDrawCategory(temp.getNodeValue());
               else
                   newSymbolDef.setDrawCategory("");

               //max points
               properties = eSymbol.getElementsByTagName(propMaxPoint);
               property = (Element)properties.item(0);
               properties = property.getChildNodes();
               temp = properties.item(0);
               if(temp != null)
               {
                   value = temp.getNodeValue();
                   newSymbolDef.setMaxPoints(Integer.parseInt(value));
               }
               else
                   newSymbolDef.setMaxPoints(0);


               //min points
               properties = eSymbol.getElementsByTagName(propMinPoints);
               property = (Element)properties.item(0);
               properties = property.getChildNodes();
               temp = properties.item(0);
               if(temp != null)
               {
                    value = temp.getNodeValue();
                    newSymbolDef.setMinPoints(Integer.parseInt(value));
               }
               else
                    newSymbolDef.setMinPoints(0);

               //modifiers
               properties = eSymbol.getElementsByTagName(propModifiers);

               property = (Element)properties.item(0);
               properties = property.getChildNodes();
               temp = properties.item(0);
               if(temp != null)
               {
                   newSymbolDef.setModifiers(temp.getNodeValue());
               }
               else
                   newSymbolDef.setModifiers("");

               //description
               properties = eSymbol.getElementsByTagName(propDescription);
               property = (Element)properties.item(0);
               properties = property.getChildNodes();
               temp = properties.item(0);
               if(temp != null)
                   newSymbolDef.setDescription(temp.getNodeValue());
               else
                   newSymbolDef.setDescription("");

               //hierarchy
               properties = eSymbol.getElementsByTagName(propHierarchy);
               property = (Element)properties.item(0);
               properties = property.getChildNodes();
               temp = properties.item(0);
               if(temp != null)
                    newSymbolDef.setHierarchy(temp.getNodeValue());
               else
                   newSymbolDef.setHierarchy("");

               /*
               properties = eSymbol.getElementsByTagName(propHasWidth);
               property = (Element)properties.item(0);
               properties = property.getChildNodes();
               temp = properties.item(0);
               newSymbolDef.setHasWidth(temp.getNodeValue());*/
/*            }

            name = newSymbolDef.getBasicSymbolId();
            if(!(name.matches("")))
                _SymbolDefinitions.put(newSymbolDef.getBasicSymbolId(), newSymbolDef);

        }
    }
*/
    /**
     * @name getSymbolDef
     *
     * @desc Returns a SymbolDef from the SymbolDefTable that matches the passed in Symbol Id
     *
     * @param strBasicSymbolID - IN - A 15 character MilStd code
     * @return SymbolDef whose Symbol Id matches what is passed in
     */
    public SymbolDef getSymbolDef(String basicSymbolID, int symStd)
    {
        SymbolDef returnVal = null;
        if(symStd==RendererSettings.Symbology_2525Bch2_USAS_13_14)
            returnVal = (SymbolDef)_SymbolDefinitionsB.get(basicSymbolID);
        else if(symStd==RendererSettings.Symbology_2525C)
            returnVal = (SymbolDef)_SymbolDefinitionsC.get(basicSymbolID);
        return returnVal;
    }

    /**
     * Returns a Map of all the symbol definitions, keyed on basic symbol code.
     * @return
     */
    public Map<String, SymbolDef> GetAllSymbolDefs(int symStd)
    {
        if(symStd==RendererSettings.Symbology_2525Bch2_USAS_13_14)
            return _SymbolDefinitionsB;
        else if(symStd==RendererSettings.Symbology_2525C)
            return _SymbolDefinitionsC;
        else
            return null;
    }
    
    /**
     * SymbolIDs are no longer unique.  
     * @param symStd
     * @return 
     */
    public ArrayList GetAllSymbolDefDups(int symStd)
    {
        if(symStd==RendererSettings.Symbology_2525Bch2_USAS_13_14)
            return _SymbolDefDupsB;
        else if(symStd==RendererSettings.Symbology_2525C)
            return _SymbolDefDupsC;
        else
            return null;
    }

    /**
     * 
     * @param basicSymbolID
     * @return
     */
    public Boolean HasSymbolDef(String basicSymbolID, int symStd)
    {
        if(basicSymbolID != null && basicSymbolID.length() == 15)
        {
            if(symStd==RendererSettings.Symbology_2525Bch2_USAS_13_14)
                return _SymbolDefinitionsB.containsKey(basicSymbolID);
            else if(symStd==RendererSettings.Symbology_2525C)
                return _SymbolDefinitionsC.containsKey(basicSymbolID);
            else
                return false;
        }
        else
            return false;
    }

}
