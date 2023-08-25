/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class Physics implements EntityComponent {
    
    private float mass = -1;
    
    public Physics() {}
    public Physics(float mass) {
        this.mass = mass;
    }

    public float getMass() {
        return mass;
    }
    @Override
    public String toString() {
        return "Physics{" + "mass=" + mass + '}';
    }
    
}
