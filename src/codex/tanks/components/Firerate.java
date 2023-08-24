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
public class Firerate implements EntityComponent {
    
    private final float rate;
    
    public Firerate(float rps) {
        rate = 1/rps;
    }

    public float getRate() {
        return rate;
    }
    @Override
    public String toString() {
        return "Firerate{" + "rate=" + rate + '}';
    }
    
}
