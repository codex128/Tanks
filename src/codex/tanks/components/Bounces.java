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
public class Bounces implements EntityComponent {
    
    private final int remaining;

    public Bounces(int remaining) {
        this.remaining = remaining;
    }

    public int getRemaining() {
        return remaining;
    }
    public boolean isExhausted() {
        return remaining <= 0;
    }
    public Bounces increment() {
        return new Bounces(remaining-1);
    }

    @Override
    public String toString() {
        return "Bounces{" + "remaining=" + remaining + '}';
    }
    
}
