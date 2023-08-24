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
    
    private final Vector3f velocity = new Vector3f();
    
    public Velocity(Vector3f velocity) {
        this.velocity.set(velocity);
    }

    public Vector3f getVelocity() {
        return velocity;
    }
    public Vector3f getDirection() {
        return velocity.normalize();
    }
    public float getSpeed() {
        return velocity.length();
    }
    @Override
    public String toString() {
        return "Velocity{" + "velocity=" + velocity + '}';
    }
    
}
