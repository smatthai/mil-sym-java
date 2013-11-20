/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ArmyC2.C2SD.Utilities;

/**
 *
 * @author michael.spinelli
 */
public class UnitDef {




            String basicSymbolId;
            String description;
            public void UnitDef()
            {
                    //Set fields to their default values.
                    basicSymbolId = "";
                    description = "";
            }

            /**
             * @name Clone
             *
             * @desc Gets around the fact that Flex passes non-primitive data types (e.g. int, string) as by ref. So
             * clone creates a new object of this instance of UnitDef
             *
             * @param none
             * @return The newly cloned UnitDef
             */
            public UnitDef Clone()
            {
                    UnitDef defReturn = new UnitDef();
                    defReturn.setBasicSymbolId(this.getBasicSymbolId());
                    defReturn.setDescription(this.getDescription());
                    defReturn.setDrawCategory(this.getDrawCategory());
                    defReturn.setHierarchy(this.getHierarchy());

                    return defReturn;
            }

            /**
             * The basic 15 character basic symbol Id.
             */
            private String _strBasicSymbolId;
            public String getBasicSymbolId()
            {
                    return _strBasicSymbolId;
            }

            /**
             * @private
             */
            public void setBasicSymbolId(String value)
            {
                    _strBasicSymbolId = value;
            }

            /**
             * The description of this tactical graphic.  Typically the name of the tactical graphic in MIL-STD-2525B.
             */
            private String _strDescription;
            public String getDescription()
            {
                    return _strDescription;
            }

            /**
             * @private
             */
            public void setDescription(String value)
            {
                    _strDescription = value;
            }

            private int _intDrawCategory = 0;
            /**
             * 8 is singlepoint unit, 0 is category
             * (do not draw because it's just a category node in the tree)
             * @return
             */
            public int getDrawCategory()
            {
                    return _intDrawCategory;
            }

            /**
             *
             */
            public void setDrawCategory(int value)
            {
                    _intDrawCategory = value;
            }

            /**
             * Defines where the symbol goes in the ms2525 hierarchy.
             * 2.X.whatever
             */
            private String _strHierarchy;
            public String getHierarchy()
            {
                    return _strHierarchy;
            }

            /**
             * @private
             */
            public void setHierarchy(String value)
            {
                    _strHierarchy = value;
            }

                        /**
             * Defines where the symbol goes in the ms2525 hierarchy.
             * STBOPS.INDIV.WHATEVER
             */
            private String _strAlphaHierarchy;
            public String getAlphaHierarchy()
            {
                    return _strAlphaHierarchy;
            }

            /**
             * @private
             */
            public void setAlphaHierarchy(String value)
            {
                    _strAlphaHierarchy = value;
            }

            private String _strFullPath = "";
             /**
     * Defines where the symbol goes in the ms2525 hierarchy.
      * Warfighting/something/something
     */
    public String getFullPath()
    {
            return _strFullPath;
    }

    /**
     *
     */
    public void setFullPath(String value)
    {
            _strFullPath = value;
    }

}
