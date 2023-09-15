/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.jme3.math.Vector3f;
import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class PositionalFade implements EntityComponent {
    
    private final Vector3f origin = new Vector3f();
    private final Vector3f axis = new Vector3f(0, 0, 1);
    private final float start;
    private final float limit;
    private final float length;
    private final float speed;
    
    public PositionalFade(Vector3f origin, Vector3f axis, float start, float limit, float length, float speed) {
        this.origin.set(origin);
        this.axis.set(axis);
        this.start = start;
        this.limit = limit;
        this.length = length;
        this.speed = speed;
    }

    public Vector3f getOrigin() {
        return origin;
    }
    public Vector3f getAxis() {
        return axis;
    }
    public float getStart() {
        return start;
    }
    public float getLimit() {
        return limit;
    }
    public float getLength() {
        return length;
    }
    public float getSpeed() {
        return speed;
    }    
    public boolean isComplete() {
        return start >= limit;
    }
    
    public PositionalFade increment(float tpf) {
        return new PositionalFade(origin, axis, start+speed*tpf, limit, length, speed);
    }
    
}
