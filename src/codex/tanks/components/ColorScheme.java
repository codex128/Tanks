/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.jme3.math.ColorRGBA;
import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class ColorScheme implements EntityComponent {
    
    private final ColorRGBA[] pallete;
    
    public ColorScheme(ColorRGBA... pallete) {
        this.pallete = pallete;
    }
    
    public ColorRGBA[] getPallete() {
        return pallete;
    }
    public void verifySize(int size) {
        if (pallete.length < size) {
            throw new NullPointerException("Not enough colors in pallete!");
        }
    }
    @Override
    public String toString() {
        return "ColorScheme{" + "pallete=" + pallete + '}';
    }
    
}
