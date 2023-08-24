/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.util;

import codex.j3map.processors.J3mapPropertyProcessor;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author codex
 */
public class ColorProcessor implements J3mapPropertyProcessor<ColorRGBA> {

    @Override
    public Class<ColorRGBA> type() {
        return ColorRGBA.class;
    }
    @Override
    public ColorRGBA process(String str) {
        if (!str.startsWith(getPropertyIdentifier()+"(") || !str.endsWith(")")) {
            return null;
        }
        String[] args = str.substring(getPropertyIdentifier().length()+1, str.length()-1).split(",");
        if (args.length != 4) {
            return null;
        }
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].trim();
        }
        ColorRGBA color = new ColorRGBA();
        color.r = Float.parseFloat(args[0]);
        color.g = Float.parseFloat(args[1]);
        color.b = Float.parseFloat(args[2]);
        color.a = Float.parseFloat(args[3]);
        return color;
    }
    @Override
    public String[] export(ColorRGBA property) {
        return new String[] {getPropertyIdentifier()+"("+property.r+", "+property.g+", "+property.b+", "+property.a+")"};
    }
    @Override
    public String getPropertyIdentifier() {
        return type().getSimpleName();
    }
    @Override
    public ColorRGBA[] createArray(int length) {
        return new ColorRGBA[length];
    }
    
}
