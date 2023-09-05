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
public class Damage implements EntityComponent {
    
    private final float damage;

    public Damage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }
    @Override
    public String toString() {
        return "Damage{" + "damage=" + damage + '}';
    }
    
}
