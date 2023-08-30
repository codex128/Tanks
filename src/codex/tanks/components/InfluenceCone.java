/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.jme3.light.SpotLight;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class InfluenceCone implements EntityComponent {
    
    private final Vector2f angles = new Vector2f(FastMath.PI/10, FastMath.PI/6);
    
    public InfluenceCone() {}
    public InfluenceCone(float inner, float outer) {
        angles.set(inner, outer);
        if (inner >= outer) {
            throw new IllegalArgumentException("The outer angle ("+outer+") must be greater than the inner angle ("+inner+")!");
        }
    }
    
    public float getInnerAngle() {
        return angles.x;
    }
    public float getOuterAngle() {
        return angles.y;
    }
    @Override
    public String toString() {
        return "InfluenceCone{inner"+angles.x+", outer="+angles.y+"}";
    }
    
    public void applyToSpotLight(SpotLight light) {
        light.setSpotInnerAngle(angles.x);
        light.setSpotOuterAngle(angles.y);
    }
    
}
