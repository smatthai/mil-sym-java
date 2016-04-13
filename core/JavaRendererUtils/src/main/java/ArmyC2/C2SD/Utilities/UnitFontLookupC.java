/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ArmyC2.C2SD.Utilities;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 *
 * @author michael.spinelli
 * @deprecated 
 */
public class UnitFontLookupC {

     private static UnitFontLookupC _instance;
  private boolean _ready = false;
  HashMap<String, UnitFontLookupInfo> hashMap = new HashMap<String, UnitFontLookupInfo>();

  //UNKNOWN FILL Indexes
  private static final int FillIndexUZ = 800;
  private static final int FillIndexUP = 849;
  private static final int FillIndexUA = 825;
  private static final int FillIndexUG = 800;
  private static final int FillIndexUGE = 800;
  private static final int FillIndexUS = 800;
  private static final int FillIndexUU = 837;
  private static final int FillIndexUF = 800;
   //FRIENDLY FILL Indexes
  private static final int FillIndexFZ = 812;
  private static final int FillIndexFP = 843;
  private static final int FillIndexFA = 819;
  private static final int FillIndexFG = 803;
  private static final int FillIndexFGE = 812;
  private static final int FillIndexFS = 812;
  private static final int FillIndexFU = 831;
  private static final int FillIndexFF = 803;
   //NEUTRAL FILL Indexes
  private static final int FillIndexNZ = 809;
  private static final int FillIndexNP = 846;
  private static final int FillIndexNA = 822;
  private static final int FillIndexNG = 809;
  private static final int FillIndexNGE = 809;
  private static final int FillIndexNS = 809;
  private static final int FillIndexNU = 834;
  private static final int FillIndexNF = 809;
   //HOSTILE FILL Indexes
  private static final int FillIndexHZ = 806;
  private static final int FillIndexHP = 840;
  private static final int FillIndexHA = 816;
  private static final int FillIndexHG = 806;
  private static final int FillIndexHGE = 806;
  private static final int FillIndexHS = 806;
  private static final int FillIndexHU = 828;
  private static final int FillIndexHF = 806;

  //Font positions for new layout
  //770-799: small fills, inside the frame
  //800-900: regular fills with solid & dashed frame
  //1000 - 3000?: Warfighting - A -
  //3000 - 3200?: SigInt - D - 80 only need fill/frame/symbol
  //3200? - 3400?: Stability Operations Symbology - E - 60 w/ 6 that need 4 symbols & 6 with secondary symbol / fill
  //4000+: Emergency Management Symbols - G -

  //
  public boolean getReady()
  {
    return this._ready;
  }

  private UnitFontLookupC()
  {
    init();
  }

  public static synchronized UnitFontLookupC getInstance()
  {
    if(_instance == null)
    {
      _instance = new UnitFontLookupC();
    }
    return _instance;
  }

  private void init()
  {
    xmlLoaded();
  }

  private void xmlLoaded()
  {
      String xmlPath = null;
      if(RendererSettings.getInstance().getSymbologyStandard() == 
              RendererSettings.Symbology_2525C)
      {
        xmlPath = "XML/UnitFontMappingsC.xml";
      }
      else
      {
        xmlPath = "XML/UnitFontMappingsB.xml";
      }
        
      
    InputStream xmlStream = this.getClass().getClassLoader().getResourceAsStream(xmlPath);
    String lookupXml = FileHandler.InputStreamToString(xmlStream);
    //String lookupXml = FileHandler.fileToString("C:\\UnitFontMappings.xml");
    populateLookup(lookupXml);
  }

  private void populateLookup(String xml)
  {

    ArrayList al = XMLUtil.getItemList(xml, "<SYMBOL>", "</SYMBOL>");
    for(int i = 0; i < al.size(); i++)
    {
      String data = (String)al.get(i);
      String ID = XMLUtil.parseTagValue(data, "<SYMBOLID>", "</SYMBOLID>");
      String description = XMLUtil.parseTagValue(data, "<DESCRIPTION>", "</DESCRIPTION>");
      String m1u = XMLUtil.parseTagValue(data, "<MAPPING1U>", "</MAPPING1U>");
      String m1f = XMLUtil.parseTagValue(data, "<MAPPING1F>", "</MAPPING1F>");
      String m1n = XMLUtil.parseTagValue(data, "<MAPPING1N>", "</MAPPING1N>");
      String m1h = XMLUtil.parseTagValue(data, "<MAPPING1H>", "</MAPPING1H>");
      String m2 = XMLUtil.parseTagValue(data, "<MAPPING2>", "</MAPPING2>");
      String c1 = XMLUtil.parseTagValue(data, "<MAPPING1COLOR>", "</MAPPING1COLOR>");
      String c2 = XMLUtil.parseTagValue(data, "<MAPPING2COLOR>", "</MAPPING2COLOR>");

      UnitFontLookupInfo uflTemp = null;
      

      uflTemp =  new UnitFontLookupInfo(ID, description, m1u, m1f, m1n, m1h, c1, m2, c2);
      
      
      if(uflTemp != null)
        hashMap.put(ID, uflTemp);
    }

  }

  /**
   * we only have font lookups for F,H,N,U.  But the shapes match one of these
   * four for the remaining affiliations.  So we convert the string to a base
   * affiliation before we do the lookup.
   * @param symbolID
   * @return
   */
  private String resolveAffiliation(String symbolID)
  {
      String code = symbolID.substring(0);
      String affiliation = symbolID.substring(1,2);

      if(affiliation.equals("F") ||//friendly
              affiliation.equals("H") ||//hostile
              affiliation.equals("U") ||//unknown
              affiliation.equals("N") )//neutral
          return code;
      else if(affiliation.equals("S"))//suspect
          code = code.substring(0, 1) + "H" + code.substring(2, 15);
      else if(affiliation.equals("L"))//exercise neutral
          code = code.substring(0, 1) + "N" + code.substring(2, 15);
      else if(affiliation.equals("A") ||//assumed friend
              affiliation.equals("D") ||//exercise friend
              affiliation.equals("M") ||//exercise assumed friend
              affiliation.equals("K") ||//faker
              affiliation.equals("J"))//joker
          code = code.substring(0, 1) + "F" + code.substring(2, 15);
      else if(affiliation.equals("P") ||//pending
              affiliation.equals("G") ||//exercise pending
              affiliation.equals("O") ||//? brought it over from mitch's code
              affiliation.equals("W"))//exercise unknown
          code = code.substring(0, 1) + "U" + code.substring(2, 15);
      else
          code = code.substring(0, 1) + "U" + code.substring(2, 15);

      return code;
  }

 
  public static UnitFontLookupInfo adjustSubSurfaceIfNot2525C(UnitFontLookupInfo original)
  {
      UnitFontLookupInfo result = null;
      
      return result;
  }

  /**
   * 2525C
   * returns the character index for the fill frame based on the symbol code.
   * @param SymbolID 15 character symbol ID
   * @return fill character index
   */
  public static int getFillCode(String SymbolID)
  {
      int returnVal = -1;

      char scheme = 0;
      char battleDimension = 0;
      char status = 0;
      char affiliation = 0;
      char grdtrkSubset = 0;
      //char foo = 'a';


      try
      {
          //to upper
          if(SymbolID != null && SymbolID.length() == 15)
          {
              scheme = SymbolID.charAt(0);//S,O,E,I,etc...
              affiliation = SymbolID.charAt(1);//F,H,N,U,etc...
              battleDimension = SymbolID.charAt(2);//P,A,G,S,U,F,X,Z
              status = SymbolID.charAt(3);//A,P,C,D,X,F
              grdtrkSubset = SymbolID.charAt(4);

              if(scheme == 'S')//Warfighting symbols
              {
                  if(affiliation == 'F' ||
                          affiliation == 'A' ||
                          affiliation == 'D' ||
                          affiliation == 'M' ||
                          affiliation == 'J' ||
                          affiliation == 'K')
                  {
                      if(battleDimension=='Z')//unknown
                      {
                          returnVal = 812;//index in font file
                      }
                      else if(battleDimension=='F' || battleDimension=='G' || battleDimension=='S')//ground & SOF & sea surface
                      {
                          if(battleDimension=='F' ||
                                  (battleDimension=='G' &&
                                    (grdtrkSubset=='U' || grdtrkSubset=='I' || grdtrkSubset=='0'|| grdtrkSubset=='-')))
                          {
                              returnVal = 803;
                          }
                          else if(battleDimension=='S' ||
                                  (battleDimension=='G' && grdtrkSubset=='E'))
                          {
                              returnVal = 812;
                          }
                          else
                              returnVal = 803;
                      }
                      else if(battleDimension=='A')//Air
                      {
                          returnVal = 819;
                      }
                      else if(battleDimension=='U')//Subsurface
                      {
                          returnVal = getSubSurfaceFill(SymbolID);
                      }
                      else if(battleDimension=='P')//space
                      {
                          returnVal = 843;
                      }
                  }
                  else if(affiliation == 'H' || affiliation == 'S')//hostile,suspect
                  {
                      if(battleDimension=='Z')//unknown
                      {
                          returnVal = 806;//index in font file
                      }
                      else if(battleDimension=='F' || battleDimension=='G' || battleDimension=='S')//ground & SOF & sea surface
                      {
                          returnVal = 806;
                      }
                      else if(battleDimension=='A')//Air
                      {
                          returnVal = 816;
                      }
                      else if(battleDimension=='U')//Subsurface
                      {
                          returnVal = getSubSurfaceFill(SymbolID);
                      }
                      else if(battleDimension=='P')//space
                      {
                          returnVal = 840;
                      }
                      else
                          returnVal = 806;
                  }
                  else if(affiliation == 'N' || affiliation == 'L')//neutral,exercise neutral
                  {
                      if(battleDimension=='Z')//unknown
                      {
                          returnVal = 809;//index in font file
                      }
                      else if(battleDimension=='F' || battleDimension=='G' || battleDimension=='S')//ground & SOF & sea surface
                      {
                          returnVal = 809;
                      }
                      else if(battleDimension=='A')//Air
                      {
                          returnVal = 822;
                      }
                      else if(battleDimension=='U')//Subsurface
                      {
                          returnVal = getSubSurfaceFill(SymbolID);
                      }
                      else if(battleDimension=='P')//space
                      {
                          returnVal = 846;
                      }
                      else
                          returnVal = 809;
                  }
                  else if(affiliation == 'P' ||
                     affiliation == 'U' ||
                     affiliation == 'G' ||
                     affiliation == 'W')
                  {

                      if(battleDimension=='Z' ||//unknown
                            battleDimension=='G' ||//ground
                            battleDimension=='S' ||//sea surface
                            battleDimension=='F')//SOF
                      {
                          returnVal = 800;//index in font file
                      }
                      else if(battleDimension=='A')//Air
                      {
                          returnVal = 825;
                      }
                      else if(battleDimension=='U')//Subsurface
                      {
                          returnVal = getSubSurfaceFill(SymbolID);
                      }
                      else if(battleDimension=='P')//space
                      {
                          returnVal = 849;
                      }
                      else
                          returnVal = 800;
                  }
              }//end if scheme == 's'
              else if(scheme == 'E')//Emergency Management Symbols
              {
                  if(battleDimension != 'N')//if not EMS natural event
                  {
                      if(affiliation == 'F' ||
                              affiliation == 'A' ||
                              affiliation == 'D' ||
                              affiliation == 'M' ||
                              affiliation == 'J' ||
                              affiliation == 'K')
                      {

                          //EMS symbols break some rules about symbol codes
                          if(SymbolUtilities.isEMSEquipment(SymbolID))
                              returnVal = 812;
                          else
                            returnVal = 803;
                      }
                      else if(affiliation == 'H' || affiliation == 'S')//hostile,suspect
                      {
                          returnVal = 806;//index in font file

                      }
                      else if(affiliation == 'N' || affiliation == 'L')//neutral,exercise neutral
                      {
                          returnVal = 809;
                      }
                      else /*if(affiliation == 'P' ||
                         affiliation == 'U' ||
                         affiliation == 'G' ||
                         affiliation == 'W')*/
                      {
                          returnVal = 800;//index in font file
                      }
                  }
                  else //natural events do not have a fill/frame
                  {
                      returnVal = -1;
                  }
              }//end if scheme == 'E'
              else if(scheme == 'I')//Also default behavior
              {
                  if(affiliation == 'F' ||
                          affiliation == 'A' ||
                          affiliation == 'D' ||
                          affiliation == 'M' ||
                          affiliation == 'J' ||
                          affiliation == 'K')
                  {
                      if(battleDimension=='Z')//unknown
                      {
                          returnVal = 812;//index in font file
                      }
                      else if(battleDimension=='F' || battleDimension=='G' || battleDimension=='S')//ground & SOF & sea surface
                      {
                          if(scheme=='I')
                            returnVal = 812;
                          else
                            returnVal = 803;
                      }
                      else if(battleDimension=='A')//Air
                      {
                          returnVal = 819;
                      }
                      else if(battleDimension=='U')//Subsurface
                      {
                          returnVal = 831;
                      }
                      else if(battleDimension=='P')//space
                      {
                          returnVal = 843;
                      }
                      else
                      {
                          if(scheme=='I')
                            returnVal = 812;
                          else
                            returnVal = 803;
                      }
                  }
                  if(affiliation == 'H' || affiliation == 'S')//hostile,suspect
                  {
                      if(battleDimension=='Z')//unknown
                      {
                          returnVal = 806;//index in font file
                      }
                      else if(battleDimension=='F' || battleDimension=='G' || battleDimension=='S')//ground & SOF & sea surface
                      {
                          returnVal = 806;
                      }
                      else if(battleDimension=='A')//Air
                      {
                          returnVal = 816;
                      }
                      else if(battleDimension=='U')//Subsurface
                      {
                          returnVal = 828;
                      }
                      else if(battleDimension=='P')//space
                      {
                          returnVal = 840;
                      }
                      else
                      {
                          returnVal = 806;
                      }
                  }
                  if(affiliation == 'N' || affiliation == 'L')//neutral,exercise neutral
                  {
                      if(battleDimension=='Z')//unknown
                      {
                          returnVal = 809;//index in font file
                      }
                      else if(battleDimension=='F' || battleDimension=='G' || battleDimension=='S')//ground & SOF & sea surface
                      {
                          returnVal = 809;
                      }
                      else if(battleDimension=='A')//Air
                      {
                          returnVal = 822;
                      }
                      else if(battleDimension=='U')//Subsurface
                      {
                          returnVal = 834;
                      }
                      else if(battleDimension=='P')//space
                      {
                          returnVal = 846;
                      }
                      else
                      {
                          returnVal = 809;
                      }
                  }
                  else if(affiliation == 'P' ||
                     affiliation == 'U' ||
                     affiliation == 'G' ||
                     affiliation == 'W')
                  {

                      if(battleDimension=='Z' ||//unknown
                            battleDimension=='G' ||//ground
                            battleDimension=='S' ||//sea surface
                            battleDimension=='F')//SOF
                      {
                          returnVal = 800;//index in font file
                      }
                      else if(battleDimension=='A')//Air
                      {
                          returnVal = 825;
                      }
                      else if(battleDimension=='U')//Subsurface
                      {
                          returnVal = 837;
                      }
                      else if(battleDimension=='P')//Subsurface
                      {
                          returnVal = 849;
                      }
                      else
                      {
                          returnVal = 800;
                      }
                  }
              }//end if scheme == 'I'
              else//scheme = 'O' and anything else
              {
                  if(affiliation == 'F' ||
                          affiliation == 'A' ||
                          affiliation == 'D' ||
                          affiliation == 'M' ||
                          affiliation == 'J' ||
                          affiliation == 'K')
                  {
                        returnVal = 803;
                  }
                  else if(affiliation == 'H' || affiliation == 'S')//hostile,suspect
                  {
                      returnVal = 806;//index in font file
                  }
                  else if(affiliation == 'N' || affiliation == 'L')//neutral,exercise neutral
                  {
                      returnVal = 809;
                  }
                  else /*if(affiliation == 'P' ||
                     affiliation == 'U' ||
                     affiliation == 'G' ||
                     affiliation == 'W')*/
                  {
                      returnVal = 800;//index in font file
                  }
              }//end default

          }

      }
      catch(Exception exc)
      {
          ErrorLogger.LogException("UnitFontLookup", "getFillCode", exc, Level.SEVERE);
      }

      return returnVal;
  }

  public static int getFrameCode(String SymbolID, int FillCode)
  {
      int returnVal = 0;
      char status = SymbolID.charAt(3);

      if(status == 'A')
          returnVal = FillCode + 2;
      else//P, C, D, X, F
          returnVal = FillCode + 1;

      if(SymbolUtilities.isSubSurface(SymbolID))
      {
          returnVal = getSubSurfaceFrame(SymbolID, FillCode);
      }

      return returnVal;

  }

  private static int getSubSurfaceFill(String SymbolID)
  {
      char affiliation = 0;
      char status = 0;
      int returnVal = 0;

      returnVal = 831;

      try
      {
          affiliation = SymbolID.charAt(1);//F,H,N,U,etc...
          status = SymbolID.charAt(3);//A,P,C,D,X,F

          if(affiliation == 'F' ||
                  affiliation == 'A' ||
                  affiliation == 'D' ||
                  affiliation == 'M' ||
                  affiliation == 'J' ||
                  affiliation == 'K')
          {
                returnVal = 831;//
          }
         else if(affiliation == 'H' || affiliation == 'S')//hostile,suspect
          {
              returnVal = 828;//index in font file

          }
         else if(affiliation == 'N' || affiliation == 'L')//neutral,exercise neutral
          {
              returnVal = 834;
          }
          else if(affiliation == 'P' ||
             affiliation == 'U' ||
             affiliation == 'G' ||
             affiliation == 'W')
          {
              returnVal = 837;//index in font file
          }
          
          if(SymbolUtilities.getBasicSymbolIDStrict(SymbolID).equalsIgnoreCase("S*U*X-----*****"))
          {
              if(status=='A')
                  returnVal = returnVal+2;
              else
                  returnVal++;
          }

          //Special check for sea mine graphics
          //2525C///////////////////////////////////////////////////////////////
          if(RendererSettings.getInstance().getSymbologyStandard() == 
                  RendererSettings.Symbology_2525C)
          {
              if(SymbolID.indexOf("WM")==4 || //Sea Mine
                      SymbolID.indexOf("WDM")==4 ||//Sea Mine Decoy
                      SymbolUtilities.getBasicSymbolIDStrict(SymbolID).equalsIgnoreCase("S*U*E-----*****") ||
                      SymbolUtilities.getBasicSymbolIDStrict(SymbolID).equalsIgnoreCase("S*U*V-----*****") ||
                      SymbolUtilities.getBasicSymbolIDStrict(SymbolID).equalsIgnoreCase("S*U*X-----*****"))
              {
                  returnVal++;




                  if(status == 'A')
                      returnVal++;

              }
              else if(SymbolUtilities.getBasicSymbolIDStrict(SymbolID).equalsIgnoreCase("S*U*ND----*****"))
              {
                  returnVal = 2121;
              }
          }
          else//2525Bch2////////////////////////////////////////////////////////
          {
              if(SymbolID.indexOf("WM")==4)//Sea Mine
              {
                  if(SymbolID.indexOf("----", 6)==6 || SymbolID.indexOf("D---", 6)==6)
                      returnVal = 2059;//
                  else if(SymbolID.indexOf("G---", 6)==6)
                      returnVal = 2062;
                  else if(SymbolID.indexOf("GD--", 6)==6)
                      returnVal = 2064;
                  else if(SymbolID.indexOf("M---", 6)==6)
                      returnVal = 2073;
                  else if(SymbolID.indexOf("MD--", 6)==6)
                      returnVal = 2075;
                  else if(SymbolID.indexOf("F---", 6)==6)
                      returnVal = 2084;
                  else if(SymbolID.indexOf("FD--", 6)==6)
                      returnVal = 2086;
                  else if(SymbolID.indexOf("O---", 6)==6 ||
                          SymbolID.indexOf("OD--", 6)==6)
                      returnVal = 2094;

              }
              else if(SymbolID.indexOf("WDM")==4)//Sea Mine Decoy
              {
                    returnVal = 2115;
              }
              else if(SymbolUtilities.getBasicSymbolIDStrict(SymbolID).equalsIgnoreCase("S*U*ND----*****"))
              {
                    returnVal = 2121;
              }//
          }
      }
      catch(Exception exc)
      {
          ErrorLogger.LogException("UnitFontLookupC", "getSubSurfaceFill", exc);
          return 831;
      }

      return returnVal;
  }

  private static int getSubSurfaceFrame(String SymbolID, int fillCode)
  {
      int returnVal = 0;

      returnVal = 831;

      try
      {
          //Special check for sea mine graphics
          //2525C///////////////////////////////////////////////////////////////
          if(RendererSettings.getInstance().getSymbologyStandard() == 
                  RendererSettings.Symbology_2525C)
          {
              if(SymbolID.indexOf("WM")==4 || //Sea Mine
                      SymbolID.indexOf("WDM")==4 ||//Sea Mine Decoy
                      SymbolUtilities.getBasicSymbolIDStrict(SymbolID).equalsIgnoreCase("S*U*E-----*****") ||
                      SymbolUtilities.getBasicSymbolIDStrict(SymbolID).equalsIgnoreCase("S*U*V-----*****") ||
                      SymbolUtilities.getBasicSymbolIDStrict(SymbolID).equalsIgnoreCase("S*U*X-----*****"))
              {
                  returnVal = -1;
              }
              else if(SymbolUtilities.getBasicSymbolIDStrict(SymbolID).equalsIgnoreCase("S*U*ND----*****"))
              {
                  returnVal = -1;
              }
              else
              {
                  if(SymbolID.charAt(3)=='A' || SymbolID.charAt(3)=='a')
                      return fillCode + 2;
                  else
                      return fillCode + 1;
              }//
          }
          else//2525Bch2////////////////////////////////////////////////////////
          {
              if(SymbolID.indexOf("WM")==4)//Sea Mine
              {
                  returnVal = -1;

              }
              else if(SymbolID.indexOf("WDM")==4)//Sea Mine Decoy
              {
                    returnVal = -1;
              }
              else if(SymbolUtilities.getBasicSymbolIDStrict(SymbolID).equalsIgnoreCase("S*U*ND----*****"))
              {
                    returnVal = -1;
              }//
              else if(SymbolUtilities.getBasicSymbolIDStrict(SymbolID).equalsIgnoreCase("S*U*X-----*****"))
              {
                  returnVal = -1;
              }
              else
              {
                  if(SymbolID.charAt(3)=='A' || SymbolID.charAt(3)=='a')
                      return fillCode + 2;
                  else
                      return fillCode + 1;
              }
          }
      }
      catch(Exception exc)
      {
          ErrorLogger.LogException("UnitFontLookupC", "getSubSurfaceFrame", exc);
          return fillCode;
      }

      return returnVal;
  }

  public UnitFontLookupInfo getLookupInfo(String SymbolID)
  {
    try
    {
        String code = SymbolUtilities.getBasicSymbolIDStrict(SymbolID);

        UnitFontLookupInfo data = hashMap.get(code);

        return data;
    }
    catch(Exception exc)
    {
        ErrorLogger.LogException("UnitFontLookupC", "getLookupInfo()", exc, Level.WARNING);
        return null;
    }
  }

  /**
   *
   * @param characterIndex - Fill Character Index
   * @return
   */
  public static double getUnitRatioHeight(int characterIndex)
  {
      if(characterIndex == FillIndexHP ||
              characterIndex == FillIndexHA ||
              characterIndex == FillIndexHU ||
              characterIndex == (FillIndexHU+1) ||
              characterIndex == (FillIndexHU+2) ||
              characterIndex == FillIndexUP ||
              characterIndex == FillIndexUA ||
              characterIndex == FillIndexUU ||
              characterIndex == (FillIndexUU+1) ||
              characterIndex == (FillIndexUU+2))
      {
          return 1.3;
      }
      else if(characterIndex == FillIndexHZ ||
              characterIndex == FillIndexHG ||
              characterIndex == FillIndexHGE ||
              characterIndex == FillIndexHS ||
              characterIndex == FillIndexHF ||
              characterIndex == FillIndexUZ ||
              characterIndex == FillIndexUG ||
              characterIndex == FillIndexUGE ||
              characterIndex == FillIndexUS ||
              characterIndex == FillIndexUF)
      {
          return 1.44;
      }
      else if(characterIndex == FillIndexFGE ||
              characterIndex == FillIndexFP ||
              characterIndex == FillIndexFA ||
              characterIndex == FillIndexFU ||
              characterIndex == (FillIndexFU+1) ||
              characterIndex == (FillIndexFU+2) ||
              characterIndex == FillIndexFZ ||
              characterIndex == FillIndexFS ||
              characterIndex == FillIndexNP ||
              characterIndex == FillIndexNA ||
              characterIndex == FillIndexNU ||
              characterIndex == (FillIndexNU+1) ||
              characterIndex == (FillIndexNU+2))
      {
          return 1.2;
      }
      else if(characterIndex == FillIndexNZ ||
              characterIndex == FillIndexNG ||
              characterIndex == FillIndexNGE ||
              characterIndex == FillIndexNS ||
              characterIndex == FillIndexNF)
      {
          return 1.1;
      }
      else if(characterIndex == FillIndexFG ||
              characterIndex == FillIndexFGE)
      {
          return 1.0;
      }
      else
      {
          return 1.2;
      }
  }

  /**
   *
   * @param characterIndex - Fill Character Index
   * @return
   */
  public static double getUnitRatioWidth(int characterIndex)
  {
      if(characterIndex == FillIndexUP ||
              characterIndex == FillIndexUA ||
              characterIndex == FillIndexUU ||
              characterIndex == FillIndexUU+1 ||
              characterIndex == FillIndexUU+2 ||
              characterIndex == FillIndexFG ||
              characterIndex == FillIndexFF)
      {
          return 1.5;
      }
      else if(characterIndex == FillIndexHZ ||
              characterIndex == FillIndexHG ||
              characterIndex == FillIndexHGE ||
              characterIndex == FillIndexHS ||
              characterIndex == FillIndexHF ||
              characterIndex == FillIndexUZ ||
              characterIndex == FillIndexUG ||
              characterIndex == FillIndexUGE ||
              characterIndex == FillIndexUS ||
              characterIndex == FillIndexUF)
      {
          return 1.44;
      }
      else if(characterIndex == FillIndexFZ ||
              characterIndex == FillIndexFGE ||
              characterIndex == FillIndexFS)
      {
          return 1.2;
      }
      else
      {
          return 1.1;
      }
  }

  /*
  public static void main(String args[])
  {
    int mapping = UnitFontLookup.instance().getCharCodeFromSymbol("G*FPPTN---****X");
    String junk = "";
  }*/
}
