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
public class Explosive implements EntityComponent {
    
    private final float radius;
    private final float lifetime;

    public Explosive(float radius, float lifetime) {
        this.radius = radius;
        this.lifetime = lifetime;
    }

    public float getRadius() {
        return radius;
    }
    public float getLifetime() {
        return lifetime;
    }
    @Override
    public String toString() {
        return "Explosive{" + "radius=" + radius + ", lifetime=" + lifetime + '}';
    }
    
}
