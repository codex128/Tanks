/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import codex.tanks.es.AbstractFloatComponent;

/**
 *
 * @author codex
 */
public class Brightness extends AbstractFloatComponent {
    
    public static final float MINIMUM = 0.001f;
    
    public Brightness(float value) {
        this(value, value);
    }
    public Brightness(float value, float max) {
        setValue(value);
        setRange(MINIMUM, max);
    }

    @Override
    public Brightness setPercent(float percent) {
        return new Brightness(interpolate(percent), range.y);
    }
    
}
