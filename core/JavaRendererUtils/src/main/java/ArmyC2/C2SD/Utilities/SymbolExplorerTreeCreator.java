/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ArmyC2.C2SD.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

/**
 *
 * @author michael.spinelli
 */
public class SymbolExplorerTreeCreator {
    
    private static String endl = System.getProperty("line.separator");
    private static String delimiter = "\\.";
    

    
    private ArrayList<MilStdDef> getAllMilStdDefs(int symStd)
    {
        Map<String,SymbolDef> SDs = SymbolDefTable.getInstance().GetAllSymbolDefs(symStd);
        Map<String,UnitDef> UDs = UnitDefTable.getInstance().GetAllUnitDefs(symStd);
        
        ArrayList<SymbolDef> SDDups = SymbolDefTable.getInstance().GetAllSymbolDefDups(symStd);
        ArrayList<UnitDef> UDDups = UnitDefTable.getInstance().GetUnitDefDups(symStd);
        
        ArrayList<MilStdDef> msds = new ArrayList<MilStdDef>();
        
        UnitDef ud = null;
        MilStdDef msd = null;
        //add unit defs
        for(Map.Entry<String, UnitDef> entry : UDs.entrySet())
        {
            ud = entry.getValue();
            if(SymbolUtilities.isMCSSpecificForceElement(ud)==false && ud.getHierarchy().charAt(0) != '0')
            {
                msd = new MilStdDef(ud.getHierarchy(), ud.getBasicSymbolId(), ud.getDescription(),ud.getDrawCategory());
                msds.add(msd);
            }
        }
        //add unit defs with duplicate symbolID
        for (UnitDef udd : UDDups)
        {
                if(SymbolUtilities.isMCSSpecificForceElement(udd)==false && udd.getHierarchy().charAt(0) != '0')
                {
                    msd = new MilStdDef(udd.getHierarchy(), udd.getBasicSymbolId(), udd.getDescription(),udd.getDrawCategory());
                    msds.add(msd);
                }
                //trace(ud.hierarchy);
        }
        //add SymbolDefs
        SymbolDef sd = null;
        for(Map.Entry<String, SymbolDef> entry : SDs.entrySet())
        {
            sd = entry.getValue();
            if(SymbolUtilities.isMCSSpecificTacticalGraphic(sd)==false && sd.getHierarchy().charAt(0) != '0')
            {
                msd = new MilStdDef(sd.getHierarchy(), sd.getBasicSymbolId(), sd.getDescription(),sd.getDrawCategory());
                msds.add(msd);
            }
        }
        //add unit defs with duplicate symbolID
        for (SymbolDef sdd : SDDups)
        {
                if(SymbolUtilities.isMCSSpecificTacticalGraphic(sdd)==false && sdd.getHierarchy().charAt(0) != '0')
                {
                    msd = new MilStdDef(sdd.getHierarchy(), sdd.getBasicSymbolId(), sdd.getDescription(),sdd.getDrawCategory());
                    msds.add(msd);
                }
                //trace(ud.hierarchy);
        }
        // </editor-fold>
        
        Collections.sort(msds);
        
        return msds;
    }
    
    public String buildXMLString(int symStd)
    {
        Document xml = buildXML(symStd);
        DOMImplementationLS domImplLS = (DOMImplementationLS)xml.getImplementation();
        LSSerializer serializer = domImplLS.createLSSerializer();
        String strXML = serializer.writeToString(xml);
        return strXML;
    }
    
    /**
     * 
     * @param filePath
     * @param symStd
     * @param withFormatting if false, you get the whole xml on one line.
     */
    public void buildXMLFile(String filePath, int symStd, Boolean withFormatting)
    {
        try
        {
            Document xml = buildXML(symStd);
            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer t = tff.newTransformer();
            if(withFormatting)
            {
                t.setOutputProperty(OutputKeys.INDENT, "yes");
                t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            }
            DOMSource source = new DOMSource(xml);
            StreamResult result = new StreamResult(new File(filePath));
            t.transform(source, result);
        }
        catch(Exception exc)
        {
            System.out.println(exc.getMessage());
        }
    }
    
    public Document buildXML(int symStd)
    {
                
        ArrayList<MilStdDef> arr = getAllMilStdDefs(symStd);
        
        Document doc = null;     
        Element rootElement = null;
        
        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            
            //root elements
            doc = docBuilder.newDocument();
            rootElement = doc.createElement("SYMBOLEXPLORER");
            doc.appendChild(rootElement);
            
            
            MilStdDef msd1 = arr.get(0);
            MilStdDef msd2 = arr.get(1);
            Element node1 = msd1.toXMLElement(doc);
            Element node2 = null;
            int count = arr.size();
            int lcv = 1;
            
            rootElement.appendChild(node1);
            while(lcv<count)
            {
                if(msd1.isChild(arr.get(lcv)))
                {
                        lcv = AddChildNode(msd1,node1,lcv,arr,doc);
                        lcv = lcv;
                }
                else if(msd1.isSibling(arr.get(lcv)))
                {
                        msd2 = arr.get(lcv);
                        node2 = msd2.toXMLElement(doc);
                        rootElement.appendChild(node2);
                        msd1 = msd2;
                        node1 = node2;
                        lcv++;
                }
            }
                       
        }
        catch(Exception exc)
        {
            System.out.println(exc.getMessage());
        }
        //System.out.println(doc.getTextContent());
        return doc;
    }
    
    public static String buildJSON()
    {
        StringBuilder json = new StringBuilder("");
        
        try
        {
            
        }
        catch(Exception exc)
        {
            System.out.println(exc.getMessage());
        }
        
        return json.toString();
    }
    
   
    private static int AddChildNode(MilStdDef prevMSD, Element prevNode, int lcv, ArrayList<MilStdDef> msds, Document doc)
    {
            MilStdDef currentMSD = null;
            currentMSD = msds.get(lcv);
            Element currentNode = null;
            int count = msds.size();
            SymbolDef sd = null;


            while (lcv < count && prevMSD.isChild(currentMSD))
            {
                    currentMSD = msds.get(lcv);
                    currentNode = currentMSD.toXMLElement(doc);

                    if (prevMSD.isChild(currentMSD))
                    {


                            prevNode.appendChild(currentNode);

                            lcv++;//enumerator.MoveNext();

                            if (lcv < count)
                            {
                                    if (currentMSD.isChild(msds.get(lcv)))//(enumerator.Current))
                                    {
                                            lcv = AddChildNode(currentMSD, currentNode, lcv, msds, doc);
                                    }
                            }
                    }


            }//end while

            return lcv;
    }
    
    public class MilStdDef implements Comparable<MilStdDef>{

        @Override
        public int compareTo(MilStdDef o) {
            int returnVal = -99;
			
            try
            {
                    if (o == null)
                    {
                            throw new Error("Passed object is null");
                    }

                    MilStdDef a = this;
                    MilStdDef b = o;
                    String str1 = a.getHierarchy();
                    String str2 = b.getHierarchy();
                    int xLocation = 0;

                    xLocation = str1.indexOf("X");
                    if (xLocation > 0)
                            str1 = str1.replace(".X", "");//get rid of the ".X"

                    xLocation = str2.indexOf("X");
                    if (str2.indexOf("X") > 0)
                            str2 = str2.replace(".X", "");//get rid of the ".X"

                    String[] arr1 = null;
                    String[] arr2 = null;

                    // can't just use "."
                    // '.' considered to be a special character
                    String delimiter = "\\.";
                    arr1 = str1.split(delimiter); 
                    arr2 = str2.split(delimiter);

                    int code1 = 0;
                    int code2 = 0;

                    int count1 = arr1.length;
                    int count2 = arr2.length;

                    int lcv = 0;
                    while (returnVal == -99)
                    {
                            code1 = -99;
                            code2 = -99;

                            code1 = Integer.valueOf(arr1[lcv]);

                            code2 = Integer.valueOf(arr2[lcv]);



                            if (str1 == null || str1.equals("") && str2 == null || str2.equals(""))
                            {
                                    returnVal = 0;//should never happen
                                    System.out.println("One of these has a bad hierarchy");
                                    System.out.println(a.getBasicSymbolID() + " - " + b.getBasicSymbolID());
                                    return 0;
                            }
                            else if (code1 < code2)
                                    returnVal = -1;
                            else if (code1 > code2)
                                    returnVal = 1;
                            else if (code1 == code2 && count1 == lcv + 1 && count2 == lcv + 1)
                                    returnVal = 0;//should never happen if hierarchies are correct
                            else if (code1 == code2 && count1 == lcv + 1 && count2 >= lcv)//equal but hierarchy1 has ended
                                    returnVal = -1;
                            else if (code1 == code2 && count1 >= lcv && lcv == count2 - 1)//equal but hierarchy2 has ended
                                    returnVal = 1;
                            lcv++;

                            if(lcv > 30)
                                    System.out.println(str1 + " vs " + str2);
                    }
            }
            catch (Exception exc)
            {
                    System.out.println(exc.getMessage());
                    System.out.println(exc.getStackTrace());
            }

            return returnVal;
        }
        
        private String _hierarchy = "";
        private String _basicSymbolID = "";
        private String _description = "";
        private int _drawCategory = 0;
        
        public MilStdDef(String hierarchy, String basicSymbolID, String description, int drawCategory)
        {
            _hierarchy = hierarchy;
            _basicSymbolID = basicSymbolID;
            _description = description;
            _drawCategory = drawCategory;
        }
        
        
        /**
         * Passed node is the parent
         * @param msd
         * @return 
         */
        public boolean isParent(MilStdDef msd)
        {
            String str1 = this.getHierarchy();
            String str2 = msd.getHierarchy().substring(0, msd.getHierarchy().length() - 2);
            if (str1.contentEquals(str2))
                    return true;
            else
                    return false;
        }
        
        /**
         * 
         * @param msd
         * @return 
         */
        public boolean isSibling(MilStdDef msd)
        {
            int index = -1;
            String str1 = this.getHierarchy();
            index = str1.lastIndexOf(".");
            str1 = str1.substring(0, index);

            String str2 = msd.getHierarchy();
            index = str2.lastIndexOf(".");
            str2 = str2.substring(0, index);

            //parent strings equal or they're both root nodes
            if (str1.equals(str2) || (str1.indexOf(".") == -1 && str2.indexOf(".") == -1))
                    return true;
            else
                    return false;
        }
        
        /**
         * passed node is a child node
         * @param msd
         * @return 
         */
        public boolean isChild(MilStdDef msd)
        {
            String str1 = this.getHierarchy();
            String str2 = msd.getHierarchy();
            int xLocation = 0;

            xLocation = str1.indexOf("X");
            if (xLocation > 0)
                    str1 = str1.replace(".X","");//get rid of the ".X"

            xLocation = str2.indexOf("X");
            if (xLocation > 0)
                    str2 = str2.replace(".X","");//get rid of the ".X"

            String[] arr1 = null;
            String[] arr2 = null;

            arr1 = str1.split(delimiter);
            arr2 = str2.split(delimiter);


            int count1 = arr1.length;
            int count2 = arr2.length;

            if (count2 == count1 + 1)
            {
                    int index = str2.lastIndexOf(".");
                    str2 = str2.substring(0, index);
                    if (str1.equals(str2))
                            return true;
                    else
                            return false;
            }
            else
                    return false;
        }
        
        public String getHierarchy()
        {
            return _hierarchy;
        }
        
        public String getBasicSymbolID()
        {
            return _basicSymbolID;
        }
        
        public String getDescription()
        {
            return _description;
        }
        
        public Element toXMLElement(Document doc)
        {
            Element e = doc.createElement("SYMBOL");
            e.setAttribute("HIERARCHY", _hierarchy);
            e.setAttribute("SYMBOLID", _basicSymbolID);
            e.setAttribute("DESCRIPTION", _description);
            e.setAttribute("DRAWCATEGORY", String.valueOf(_drawCategory));
            return e;
        }
        
        public String toXML()
        {
            
                String temp = "<SYMBOL HIERARCHY=\"" + _hierarchy + "\" SYMBOLID=\"" + _basicSymbolID + "\" DESCRIPTION=\"" + _description + "\"/>";
                //return new XML(temp);
                return temp;
        }
        
        public String toJSON()
        {
            //{"type":"symbol","basicID:S*F*-----------","hierarchy":"1.X.#","description":"unit"}
                String temp = "{\"type\":\"symbol\",\"hierarchy\":\"" + _hierarchy + "\",\"basicID\":\"" + _basicSymbolID + "\",\"description\":\"" + _description + "\"}";
                //return new XML(temp);
                return temp;
        }
    }
    
}
