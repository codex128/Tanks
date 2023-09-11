/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.es;

import codex.tanks.es.FloatComponent;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

/**
 *
 * @author codex
 */
public abstract class AbstractFloatComponent implements FloatComponent {
    
    protected float value;
    protected final Vector2f range = new Vector2f(0f, 1f);
    
    protected void setValue(float value) {
        this.value = value;
    }
    protected void setRange(Vector2f range) {
        this.range.set(range);
    }
    protected void setRange(float min, float max) {
        range.set(min, max);
    }

    public float getValue() {
        return value;
    }
    public Vector2f getRange() {
        return range;
    }
    @Override
    public float getPercent() {
        return (value-range.x)/(range.y-range.x);
    }
    
    protected float interpolate(float percent) {
        return FastMath.interpolateLinear(FastMath.clamp(percent, 0f, 1f), range.x, range.y);
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName()+"{" + "value=" + value + ", range=" + range + '}';
    }
    
}
