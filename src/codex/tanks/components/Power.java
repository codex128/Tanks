/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import codex.tanks.util.AbstractFloatComponent;
import com.jme3.math.Vector2f;

/**
 *
 * @author codex
 */
public class Power extends AbstractFloatComponent {

    public Power(float value) {
        setValue(value);
        setRange(0.001f, value);
    }
    public Power(float min, float value) {
        setValue(value);
        setRange(min, value);
    }
    public Power(float value, Vector2f range) {
        setValue(value);
        setRange(range);
    }
    
    @Override
    public Power setPercent(float percent) {
        return new Power(interpolate(percent), range);
    }
    
}
