/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaLineArray;
import java.awt.geom.GeneralPath;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.geom.AffineTransform;
import ArmyC2.C2SD.Utilities.ShapeInfo;
/**
 * A class to extend ShapeInfo suitable for point calculations.
 * @author Michael Deutch. 
 */
public class Shape2 extends ShapeInfo
{

    public Shape2(int value)
    {
        setShapeType(value);
        _Shape=new GeneralPath();
        AffineTransform tx=new AffineTransform();
        tx.setToIdentity();
        Stroke stroke=new BasicStroke();
        this.setStroke(stroke);
        this.setAffineTransform(tx);
    }
    private int style=0;  //e.g. 26 for enemy flots
    public void set_Style(int value)
    {
        style = value;
    }
    private int fillStyle;
    public void set_Fillstyle(int value)
    {
        fillStyle=value;
    }
    public int get_FillStyle()
    {
        return fillStyle;
    }
    public int get_Style()  //used by TacticalRenderer but not client
    {
        return style;
    }
    public void lineTo(POINT2 pt)
    {
        ((GeneralPath)_Shape).lineTo(pt.x, pt.y);
    }
    public void moveTo(POINT2 pt)
    {
        ((GeneralPath)_Shape).moveTo(pt.x, pt.y);
    }
}
