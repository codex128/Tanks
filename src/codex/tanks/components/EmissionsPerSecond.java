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
public class EmissionsPerSecond implements EntityComponent {
    
    private final float eps;

    public EmissionsPerSecond(float eps) {
        this.eps = eps;
    }

    public float getEps() {
        return eps;
    }
    @Override
    public String toString() {
        return "EmissionsPerSecond{" + "eps=" + eps + '}';
    }
    
}
