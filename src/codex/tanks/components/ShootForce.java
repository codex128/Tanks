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
public class ShootForce implements EntityComponent {
    
    private final float force;

    public ShootForce(float force) {
        this.force = force;
    }

    public float getForce() {
        return force;
    }
    @Override
    public String toString() {
        return "ShootForce{" + "force=" + force + '}';
    }
    
}
