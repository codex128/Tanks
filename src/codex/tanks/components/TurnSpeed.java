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
public class TurnSpeed implements EntityComponent {
    
    private final float speed;

    public TurnSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }
    @Override
    public String toString() {
        return "TurnSpeed{" + "speed=" + speed + '}';
    }
    
}
