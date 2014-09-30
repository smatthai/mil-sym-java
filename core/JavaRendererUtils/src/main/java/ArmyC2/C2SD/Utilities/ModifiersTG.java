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
 * Tactical Graphics:
 * P = points, L = lines, A = areas, BL = boundary lines, N = nuclear, B/C = bio/chem.
 * G = graphic modifier so there is no length.  Most of these we feed off of the symbol
 * code so they aren't actually used.
 * @author michael.spinelli
 */
public class ModifiersTG {

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
     * SIDC positions 3, 5-104
     * TG: P,L,A,BL,N,B/C
     * Length: G
     */
    public static final String A_SYMBOL_ICON = "A";
    /**
     * The basic graphic (see 5.5.1).
     * We feed off of the symbol code so this isn't used
     * SIDC positions 11 and 12
     * TG: L,A,BL
     * Length: G
     */
    public static final String B_ECHELON = "B";
    /**
     * A graphic modifier in a boundary graphic that
     * identifies command level (see 5.5.2.2, table V, and
     * figures 10 and 12).
     * TG: N
     * Length: 6
     */
    public static final String C_QUANTITY = "C";
    /**
     * A text modifier for tactical graphics; content is
     * implementation specific.
     * TG: P,L,A,N,B/C
     * Length: 20
     */
    public static final String H_ADDITIONAL_INFO_1 = "H";
    /**
     * A text modifier for tactical graphics; content is
     * implementation specific.
     * TG: P,L,A,N,B/C
     * Length: 20
     */
    public static final String H1_ADDITIONAL_INFO_2 = "H1";
    /**
     * A text modifier for tactical graphics; content is
     * implementation specific.
     * TG: P,L,A,N,B/C
     * Length: 20
     */
    public static final String H2_ADDITIONAL_INFO_3 = "H2";
    /**
     * A text modifier for tactical graphics; letters "ENY" denote hostile symbols.
     * TG: P,L,A,BL,N,B/C
     * Length: 3
     */
    public static final String N_HOSTILE = "N";
    /**
     * A graphic modifier for CBRN events that
     * identifies the direction of movement (see 5.5.2.1
     * and figure 11).
     * TG: N,B/C
     * Length: G
     */
    public static final String Q_DIRECTION_OF_MOVEMENT = "Q";
    /**
     * A graphic modifier for points and CBRN events
     * used when placing an object away from its actual
     * location (see 5.5.2.3 and figures 10, 11, and 12).
     * TG: P,N,B/C
     * Length: G
     */
    public static final String S_OFFSET_INDICATOR = "S";
    /**
     * A text modifier that uniquely identifies a particular
     * tactical graphic; track number.
     * Nuclear: delivery unit (missile, aircraft, satellite,
     * etc.)
     * TG:P,L,A,BL,N,B/C
     * Length: 15 (35 for BL)
     */
    public static final String T_UNIQUE_DESIGNATION_1 = "T";
    /**
     * A text modifier that uniquely identifies a particular
     * tactical graphic; track number.
     * Nuclear: delivery unit (missile, aircraft, satellite,
     * etc.)
     * TG:P,L,A,BL,N,B/C
     * Length: 15 (35 for BL)
     */
    public static final String T1_UNIQUE_DESIGNATION_2 = "T1";
    /**
     * A text modifier that indicates nuclear weapon type.
     * TG: N
     * Length: 20
     */
    public static final String V_EQUIP_TYPE = "V";
    /**
     * A text modifier for units, equipment, and installations that displays DTG format:
     * DDHHMMSSZMONYYYY or “O/O” for on order (see 5.5.2.6).
     * TG:P,L,A,N,B/C
     * Length: 16
     */
    public static final String W_DTG_1 = "W";
    /**
     * A text modifier for units, equipment, and installations that displays DTG format:
     * DDHHMMSSZMONYYYY or “O/O” for on order (see 5.5.2.6).
     * TG:P,L,A,N,B/C
     * Length: 16
     */
    public static final String W1_DTG_2 = "W1";
    /**
     * A text modifier that displays the minimum,
     * maximum, and/or specific altitude (in feet or
     * meters in relation to a reference datum), flight
     * level, or depth (for submerged objects in feet
     * below sea level). See 5.5.2.5 for content.
     * TG:P,L,A,N,B/C
     * Length: 14
     */
    public static final String X_ALTITUDE_DEPTH = "X";
    /**
     * A text modifier that displays a graphic’s location
     * in degrees, minutes, and seconds (or in UTM or
     * other applicable display format).
     *  Conforms to decimal
     *  degrees format:
     *  xx.dddddhyyy.dddddh
     *  where
     *  xx = degrees latitude
     *  yyy = degrees longitude
     *  .ddddd = decimal degrees
     *  h = direction (N, E, S, W)
     * TG:P,L,A,BL,N,B/C
     * Length: 19
     */
    public static final String Y_LOCATION = "Y";

    /**
     * For Tactical Graphics
     * A numeric modifier that displays a minimum,
     * maximum, or a specific distance (range, radius,
     * width, length, etc.), in meters.
     * 0 - 999,999 meters
     * TG: P.L.A
     * Length: 6
     */
    public static final String AM_DISTANCE = "AM";
    /**
     * For Tactical Graphics
     * A numeric modifier that displays an angle
     * measured from true north to any other line in
     * degrees.
     * 0 - 359 degrees
     * TG: P.L.A
     * Length: 3
     */
    public static final String AN_AZIMUTH = "AN";




    public static final String LENGTH = "Length";
    public static final String WIDTH = "Width";
    public static final String RADIUS = "Radius";
    public static final String ANGLE = "Angle";
    //public static final String SEGMENT_DATA = "Segment Data";

    /**
     * Returns an Arraylist of the modifer names for tactical graphics
     * @return
     */
    public synchronized static ArrayList<String> GetModifierList()
    {
        ArrayList<String> list = new ArrayList<String>();

        //list.add(ModifierType.A_SYMBOL_ICON);//graphical, feeds off of symbol code
        //list.add(ModifierType.B_ECHELON);//graphical, feeds off of symbol code
        list.add(C_QUANTITY);
        list.add(H_ADDITIONAL_INFO_1);
        list.add(H1_ADDITIONAL_INFO_2);
        list.add(H2_ADDITIONAL_INFO_3);
        list.add(N_HOSTILE);
        list.add(Q_DIRECTION_OF_MOVEMENT);
        list.add(T_UNIQUE_DESIGNATION_1);
        list.add(T1_UNIQUE_DESIGNATION_2);
        list.add(V_EQUIP_TYPE);
        list.add(W_DTG_1);
        list.add(W1_DTG_2);
        list.add(X_ALTITUDE_DEPTH);
        list.add(Y_LOCATION);

        list.add(AM_DISTANCE);//2525C
        //list.add(AM1_DISTANCE);//2525C
        list.add(AN_AZIMUTH);//2525C
        //list.add(AN1_AZIMUTH);//2525C

        //back compat
        list.add(LENGTH);
        list.add(WIDTH);
        list.add(RADIUS);
        list.add(ANGLE);



        return list;
    }


}
