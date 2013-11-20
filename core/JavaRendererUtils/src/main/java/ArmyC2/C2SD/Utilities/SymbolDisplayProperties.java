/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ArmyC2.C2SD.Utilities;

import java.awt.Color;


/**
 *
 * @author michael.spinelli
 * @deprecated 
 */
public class SymbolDisplayProperties {

     private Color _FillColor = null;
    private Color _LineColor = null;
    private String _FillStyle = null;
    private String _LineType = null;
    private int _LineWidth = -1;

    public SymbolDisplayProperties() {
    }

    public Color getFillColor() {
        return _FillColor;
    }

    public String getFillStyle() {
        return _FillStyle;
    }

    public Color getLineColor() {
        return _LineColor;
    }

    public int getLineWidth() {
        return _LineWidth;
    }

    public void setFillColor(Color fillColor) {
        _FillColor = fillColor;
    }

    public void setFillStyle(String style) {
        _FillStyle = style;
    }

    public void setLineColor(Color lineColor) {
        _LineColor = lineColor;
    }

    public void setLineWidth(int width) {
        _LineWidth = width;
    }

}
