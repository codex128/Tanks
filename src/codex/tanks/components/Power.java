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
public class Power implements EntityComponent {
    
    private final float power;

    public Power(float power) {
        this.power = power;
    }

    public float getPower() {
        return power;
    }
    @Override
    public String toString() {
        return "Power{" + "power=" + power + '}';
    }
    
}
