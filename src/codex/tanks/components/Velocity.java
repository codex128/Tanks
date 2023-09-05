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
public class Velocity implements EntityComponent {
    
    private final Vector3f direction = new Vector3f();
    private final float speed;
    
    public Velocity(Vector3f velocity) {
        direction.set(velocity).normalizeLocal();
        speed = velocity.length();
    }
    public Velocity(Vector3f direction, float speed) {
        this.direction.set(direction).normalizeLocal();
        this.speed = speed;
    }

    public Vector3f getVelocity() {
        return direction.mult(speed);
    }
    public Vector3f getDirection() {
        return direction.clone();
    }
    public float getSpeed() {
        return speed;
    }
    public boolean isSpeedConstrained() {
        return speed >= 0;
    }
    @Override
    public String toString() {
        return "Velocity{" + "velocity=" + getVelocity() + '}';
    }
    
}
