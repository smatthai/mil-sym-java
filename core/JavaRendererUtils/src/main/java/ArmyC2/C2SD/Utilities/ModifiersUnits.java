/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ArmyC2.C2SD.Utilities;

import java.util.ArrayList;

/**
 * This class contains keys to the modifiers.  A number of these we feed off of
 * the symbol code instead of using the property.  But those modifiers remain
 * for completeness.
 *
 * U = units, E = equipment, I= installations, SI = signals intelligence (SIGINT), SO = stability operations, EU = EMS units, EEI = EMS equipment and incidents, EI =
 * EMS installations.
 * @author michael.spinelli
 */
public class ModifiersUnits {


//public static final String SYMBOL_ID = "Symbol ID";
    //public static final String SOURCE = "Source";
    //public static final String EDITOR_CLASS_TYPE = "Editor Class Type";
    //public static final String URN = "URN";
    //public static final String UIC = "UIC";
    //public static final String ANGLE_OF_ROTATION = "Angle of Rotation";
    /**
     * The innermost part of a symbol that represents a warfighting object
     * Here for completeness, not actually used as this comes from the
     * symbol code.
     * Type: U,E,I,SI,SO,EU,EEI,EI
     * Length: G
     */
    public static final String A_SYMBOL_ICON = "A";
    /**
     * A graphic modifier in a unit symbol that identifies command level
     * We feed off of the symbol code so this isn't used
     * Type: U,SO
     * Length: G
     */
    public static final String B_ECHELON = "B";
    /**
     * A text modifier in an equipment symbol that identifies the number of items present.
     * Type: E,EEI
     * Length: 9
     */
    public static final String C_QUANTITY = "C";
    /**
     * A graphic modifier that identifies a unit or SO symbol as a task force (see 5.3.4.6
     * and figures 2 and 3).
     * Type: U,SO
     * Length: G
     */
    public static final String D_TASK_FORCE_INDICATOR = "D";
    /**
     * A graphic modifier that displays standard identity, battle dimension, or exercise
     * amplifying descriptors of an object (see 5.3.1 and table II).
     * Type: U,E,I,SO,EU,EEI,EI
     * Length: G
     */
    public static final String E_FRAME_SHAPE_MODIFIER = "E";
    /**
     * A text modifier in a unit symbol that displays (+) for reinforced, (-) for reduced,(+) reinforced and reduced.
     * R = reinforced,D = reduced,RD = reinforced and reduced
     * Type: U,SO
     * Length: 23
     */
    public static final String F_REINFORCED_REDUCED = "F";
    /**
     * A text modifier for units, equipment and installations; content is implementation specific.
     * Type: U,E,I,SI,SO
     * Length: 20
     */
    public static final String G_STAFF_COMMENTS = "G";
    /**
     * Text modifier for amplifying free text
     * Type: U,E,I,SI,SO,EU,EEI,EI
     * Length: 20
     */
    public static final String H_ADDITIONAL_INFO_1 = "H";
    /**
     * Text modifier for amplifying free text
     * Type: U,E,I,SI,SO,EU,EEI,EI
     * Length: 20
     */
    public static final String H1_ADDITIONAL_INFO_2 = "H1";
    /**
     * Text modifier for amplifying free text
     * Type: U,E,I,SI,SO,EU,EEI,EI
     * Length: 20
     */
    public static final String H2_ADDITIONAL_INFO_3 = "H2";
    /**
     * A text modifier for units, equipment, and installations that consists of 
     * a one letter reliability rating and a one-number credibility rating.
        Reliability Ratings: A-completely reliable, B-usually reliable, 
        C-fairly reliable, D-not usually reliable, E-unreliable, 
        F-reliability cannot be judged.
        Credibility Ratings: 1-confirmed by other sources,
        2-probably true, 3-possibly true, 4-doubtfully true,
        5-improbable, 6-truth cannot be judged.
        Type: U,E,I,SI,SO,EU,EEI,EI
        Length: 2
     */
    public static final String J_EVALUATION_RATING = "J";
    /**
     * A text modifier for units and installations that indicates unit effectiveness or
     * installation capability.
     * Type: U,I,SO
     * Length: 5,5,3
     */
    public static final String K_COMBAT_EFFECTIVENESS = "K";
    /**
     * A text modifier for hostile equipment; “!” indicates detectable electronic
     * signatures.
     * Type: E,SI
     * Length: 1
     */
    public static final String L_SIGNATURE_EQUIP = "L";
    /**
     * A text modifier for units that indicates number or title of higher echelon
     * command (corps are designated by Roman numerals).
     * Type: U,SI
     * Length: 21
     */
    public static final String M_HIGHER_FORMATION = "M";
    /**
     * A text modifier for equipment; letters "ENY" denote hostile symbols.
     * Type: E
     * Length: 3
     */
    public static final String N_HOSTILE = "N";
    /**
     * A text modifier displaying IFF/SIF Identification modes and codes.
     * Type: U,E,SO
     * Length: 5
     */
    public static final String P_IFF_SIF = "P";
    /**
     * A graphic modifier for units and equipment that identifies the direction of
     * movement or intended movement of an object (see 5.3.4.1 and figures 2 and 3).
     * Type: U,E,SO,EU,EEI
     * Length: G
     */
    public static final String Q_DIRECTION_OF_MOVEMENT = "Q";
    /**
     * A graphic modifier for equipment that depicts the mobility of an object (see
     *   5.3.4.3, figures 2 and 3, and table VI).
     * We feed off of the symbol code for mobility so this isn't used
     * Type: E,EEI
     * Length: G
     */
    public static final String R_MOBILITY_INDICATOR = "R";
    /**
     * M = Mobile, S = Static, or U = Uncertain.
     * Type: SI
     * Length: 1
     */
    public static final String R2_SIGNIT_MOBILITY_INDICATOR = "R2";
    /**
     * Headquarters staff indicator: A graphic modifier for units, equipment, and
     * installations that identifies a unit as a headquarters (see 5.3.4.8 and figures 2 and
     * 3).
     * Offset location indicator: A graphic modifier for units, equipment, and
     * installations used when placing an object away from its actual location (see
     * 5.3.4.9 and figures 2 and 3).
     * Type: U,E,I,SO,EU,EEI,EI
     * Length: G
     */
    public static final String S_HQ_STAFF_OR_OFFSET_INDICATOR = "S";
    /**
     * A text modifier for units, equipment, and installations that uniquely identifies a
     * particular symbol or track number. Identifies acquisitions number when used
     * with SIGINT symbology.
     * Type: U,E,I,SI,SO,EU,EEI,EI
     * Length: 21
     */
    public static final String T_UNIQUE_DESIGNATION_1 = "T";
    /**
     * A text modifier for units, equipment, and installations that uniquely identifies a
     * particular symbol or track number. Identifies acquisitions number when used
     * with SIGINT symbology.
     * Type: U,E,I,SI,SO,EU,EEI,EI
     * Length: 21
     */
    public static final String T1_UNIQUE_DESIGNATION_2 = "T1";
    /**
     * A text modifier for equipment that indicates types of equipment.
     * For Tactical Graphics:
     * A text modifier that indicates nuclear weapon type.
     * Type: E,SI,EEI
     * Length: 24
     */
    public static final String V_EQUIP_TYPE = "V";
    /**
     * A text modifier for units, equipment, and installations that displays DTG format:
     * DDHHMMSSZMONYYYY or “O/O” for on order (see 5.5.2.6).
     * Type: U,E,I,SI,SO,EU,EEI,EI
     * Length: 16
     */
    public static final String W_DTG_1 = "W";
    /**
     * A text modifier for units, equipment, and installations that displays DTG format:
     * DDHHMMSSZMONYYYY or “O/O” for on order (see 5.5.2.6).
     * Type: U,E,I,SI,SO,EU,EEI,EI
     * Length: 16
     */
    public static final String W1_DTG_2 = "W1";
    /**
     * A text modifier for units, equipment, and installations, that displays either
     * altitude flight level, depth for submerged objects; or height of equipment or
     * structures on the ground. See 5.5.2.5 for content.
     * Type: U,E,I,SO,EU,EEI,EI
     * Length: 14
     */
    public static final String X_ALTITUDE_DEPTH = "X";
    /**
     * A text modifier for units, equipment, and installations that displays a symbol’s
     * location in degrees, minutes, and seconds (or in UTM or other applicable display
     * format).
     * Conforms to decimal
     *  degrees format:
     *  xx.dddddhyyy.dddddh
     *  where
     *  xx = degrees latitude
     *  yyy = degrees longitude
     *  .ddddd = decimal degrees
     *  h = direction (N, E, S, W)
     * Type: U,E,I,SI,SO,EU,EEI,EI
     * Length: 19
     */
    public static final String Y_LOCATION = "Y";
    /**
     * A text modifier for units and equipment that displays velocity as set forth in
     * MIL-STD-6040.
     * Type: U,E,SO,EU,EEI
     * Length: 8
     */
    public static final String Z_SPEED = "Z";
    /**
     * A text modifier for units; indicator is contained inside the frame (see figures 2
     * and 3); contains the name of the special C2 Headquarters.
     * Type: U,SO
     * Length: 9
     */
    public static final String AA_SPECIAL_C2_HQ = "AA";
    /**
     * Feint or dummy indicator: A graphic modifier for units, equipment, and
     * installations that identifies an offensive or defensive unit intended to draw the
     * enemy’s attention away from the area of the main attack (see 5.3.4.7 and figures
     * 2 and 3).
     * Type: U,E,I,SO
     * Length: G
     */
    public static final String AB_FEINT_DUMMY_INDICATOR = "AB";
    /**
     * Installation: A graphic modifier for units, equipment, and installations used to
     * show that a particular symbol denotes an installation (see 5.3.4.5 and figures 2
     * and 3).
     * Not used, we feed off of symbol code for this
     * Type: U,E,I,SO,EU,EEI,EI
     * Length: G
     */
    public static final String AC_INSTALLATION = "AC";
    /**
     * ELNOT or CENOT
     * Type: SI
     * Length: 6
     */
    public static final String AD_PLATFORM_TYPE = "AD";
    /**
     * Equipment teardown time in minutes.
     * Type: SI
     * Length: 3
     */
    public static final String AE_EQUIPMENT_TEARDOWN_TIME = "AE";
    /**
     * Example: “Hawk” for Hawk SAM system.
     * Type: SI
     * Length: 12
     */
    public static final String AF_COMMON_IDENTIFIER = "AF";
    /**
     * Towed sonar array indicator: A graphic modifier for equipment that indicates the
     * presence of a towed sonar array (see 5.3.4.4, figures 2 and 3, and table VII).
     * Type: E
     * Length: G
     */
    public static final String AG_AUX_EQUIP_INDICATOR = "AG";
    /**
     * A graphic modifier for units and equipment that indicates the area where an
     * object is most likely to be, based on the object’s last report and the reporting
     * accuracy of the sensor that detected the object (see 5.3.4.11.1 and figure 4).
     * Type: E,U,SO,EU,EEI
     * Length: G
     */
    public static final String AH_AREA_OF_UNCERTAINTY = "AH";
    /**
     * A graphic modifier for units and equipment that identifies where an object
     * should be located at present, given its last reported course and speed (see
     * 5.3.4.11.2 and figure 4).
     * Type: E,U,SO,EU,EEI
     * Length: G
     */
    public static final String AI_DEAD_RECKONING_TRAILER = "AI";
    /**
     * A graphic modifier for units and equipment that depicts the speed and direction
     * of movement of an object (see 5.3.4.11.3 and figure 4).
     * Type: E,U,SO,EU,EEI
     * Length: G
     */
    public static final String AJ_SPEED_LEADER = "AJ";
    /**
     * A graphic modifier for units and equipment that connects two objects and is
     * updated dynamically as the positions of the objects change (see 5.3.4.11.4 and
     * figure 4).
     * Type: U,E,SO
     * Length: G
     */
    public static final String AK_PAIRING_LINE = "AK";
    /**
     * An optional graphic modifier for equipment or installations that indicates
     * operational condition or capacity.
     * Type: E,I,SI,SO,EU,EEI,EI
     * Length: G
     */
    public static final String AL_OPERATIONAL_CONDITION = "AL";

    /**
     * A graphic amplifier placed immediately atop the symbol. May denote, 1)
     * local/remote status; 2) engagement status; and 3) weapon type.
     *
     * Type: U,E,I
     * Length: G/8
     */
    public static final String AO_ENGAGEMENT_BAR = "AO";

    /**
     * Used internally by the renderer.  This value is set via the 13th & 14th
     * characters in the symbol id.  There is no formal definition of how
     * this should be indicated on the symbol in the MilStd or USAS.  
     * The renderer will place it to the right of the 'H' label.
     */
    public static final String CC_COUNTRY_CODE = "CC";
    
    /**
     * A generic name label that goes to the right of the symbol and
     * any existing labels.  If there are no existing labels, it goes right
     * next to the right side of the symbol.  This is a CPOF label that applies
     * to all force elements.  This IS NOT a MilStd or USAS Label.  
     */
    public static final String CN_CPOF_NAME_LABEL = "CN";
    
    /**
     * Sonar Classification Confidence level. valid values are 1-5.
     * Only applies to the 4 subsurface MILCO sea mines
     */
    public static final String SCC_SONAR_CLASSIFICATION_CONFIDENCE = "SCC";



    //public static final String LENGTH = "Length";
    //public static final String WIDTH = "Width";
    //public static final String RADIUS = "Radius";
    //public static final String SEGMENT_DATA = "Segment Data";

    /**
     * Returns an Arraylist of the modifer names for units
     * @return
     */
    public static ArrayList<String> GetModifierList()
    {
        ArrayList<String> list = new ArrayList<String>();

        //list.add(ModifierType.A_SYMBOL_ICON);//graphical, feeds off of symbol code, SIDC positions 3, 5-10
        //list.add(ModifierType.B_ECHELON);//graphical, feeds off of symbol code, SIDC positions 11-12
        list.add(C_QUANTITY);
        //list.add(D_TASK_FORCE_INDICATOR);//graphical, feeds off of symbol code, SIDC positions 11-12
        //list.add(E_FRAME_SHAPE_MODIFIER);//symbol frame, feeds off of symbol code, SIDC positions 3-4
        list.add(F_REINFORCED_REDUCED);//R = reinforced, D = reduced, RD = reinforced and reduced
        list.add(G_STAFF_COMMENTS);
        list.add(H_ADDITIONAL_INFO_1);
        list.add(H1_ADDITIONAL_INFO_2);
        list.add(H2_ADDITIONAL_INFO_3);
        list.add(J_EVALUATION_RATING);
        list.add(K_COMBAT_EFFECTIVENESS);
        list.add(L_SIGNATURE_EQUIP);
        list.add(M_HIGHER_FORMATION);
        list.add(N_HOSTILE);
        list.add(P_IFF_SIF);
        list.add(Q_DIRECTION_OF_MOVEMENT);//number in mils
        //list.add(R_MOBILITY_INDICATOR);//graphical, feeds off of symbol code, SIDC positions 11-12
        list.add(R2_SIGNIT_MOBILITY_INDICATOR);
        //list.add(S_HQ_STAFF_OR_OFFSET_INDICATOR);//graphical, feeds off of symbol code, SIDC positions 11-12
        list.add(T_UNIQUE_DESIGNATION_1);
        list.add(T1_UNIQUE_DESIGNATION_2);
        list.add(V_EQUIP_TYPE);
        list.add(W_DTG_1);
        list.add(W1_DTG_2);
        list.add(X_ALTITUDE_DEPTH);
        list.add(Y_LOCATION);
        list.add(Z_SPEED);

        list.add(AA_SPECIAL_C2_HQ);
        //list.add(AB_FEINT_DUMMY_INDICATOR);//graphical, feeds off of symbol code, SIDC positions 11-12
        //list.add(AC_INSTALLATION);//graphical, feeds off of symbol code, SIDC positions 11-12
        list.add(AD_PLATFORM_TYPE);
        list.add(AE_EQUIPMENT_TEARDOWN_TIME);
        list.add(AF_COMMON_IDENTIFIER);
        list.add(AG_AUX_EQUIP_INDICATOR);
        list.add(AH_AREA_OF_UNCERTAINTY);
        list.add(AI_DEAD_RECKONING_TRAILER);
        list.add(AJ_SPEED_LEADER);
        list.add(AK_PAIRING_LINE);
        //list.add(AL_OPERATIONAL_CONDITION);//2525C ////graphical, feeds off of symbol code, SIDC positions 4
        list.add(AO_ENGAGEMENT_BAR);//2525C



        return list;
    }

}
