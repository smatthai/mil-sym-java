/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ArmyC2.C2SD.Utilities;

/**
 *
 * @author michael.spinelli
 */
public class SinglePointLookupInfo {
    
    private String _SymbolID = "";
    private String _Description = "";
    private int _mappingP = 0;
    private int _mappingA = 0;
    private int _height = 0;
    private int _width = 0;
    
    public SinglePointLookupInfo(String basicSymbolID, String description, 
                                    String mappingP, String mappingA,String width,String height)
    {
            _SymbolID = basicSymbolID;
            _Description = description;
            if(mappingP != null && mappingP.equals("") == false)
                    _mappingP = Integer.valueOf(mappingP);
            if(mappingA != null && mappingA.equals("") == false)
                    _mappingA = Integer.valueOf(mappingA);
            if(height != null && height.equals("") == false)
                    _height = Integer.valueOf(height);
            if(width != null && width.equals("") == false)
                    _width = Integer.valueOf(width);
    }
    
    public String getBasicSymbolID()
    {
            return _SymbolID;
    }

    public String getDescription()
    {
            return _Description;
    }

    public int getMappingP()
    {
            return _mappingP;
    }

    public int getMappingA()
    {
            return _mappingA;
    }

    public int getHeight()
    {
            return _height;
    }

    public int getWidth()
    {
            return _width;
    }
    
      /**
   * 
   * @return The newly cloned SPSymbolDef
   */
  @Override
  public SinglePointLookupInfo clone()
  {
    SinglePointLookupInfo defReturn;
    defReturn = new SinglePointLookupInfo(_SymbolID, _Description, 
            String.valueOf(getMappingP()), 
            String.valueOf(getMappingA()),
            String.valueOf(getWidth()), 
            String.valueOf(getHeight()));
    return defReturn;
  }

  public String toXML()
  {
    String symbolId = "<SYMBOLID>" +  getBasicSymbolID() + "</SYMBOLID>";
    String mappingP = "<MAPPINGP>" + String.valueOf(getMappingP()) + "</MAPPINGP>";
    String mappingA = "<MAPPINGA>" + String.valueOf(getMappingA()) + "</MAPPINGA>";
    String description = "<DESCRIPTION>" + getDescription() + "</DESCRIPTION>";
    String width = "<WIDTH>" + String.valueOf(getWidth()) + "</WIDTH>";
    String height = "<HEIGHT>" + String.valueOf(getHeight()) + "</HEIGHT>";

    String xml = symbolId + mappingP + mappingA + description + width + height;
    return xml;
  }
    
}
