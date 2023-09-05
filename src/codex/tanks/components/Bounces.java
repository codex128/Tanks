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
    private final int bounces;

    public Bounces(int remaining) {
        this(remaining, 0);
    }
    public Bounces(Bounces b) {
        remaining = b.remaining;
        bounces = b.bounces;
    }
    private Bounces(int remaining, int bounces) {
        this.remaining = remaining;
        this.bounces = bounces;
    }

    public int getRemaining() {
        return remaining;
    }
    public int getBouncesMade() {
        return bounces;
    }
    public boolean isExhausted() {
        return remaining <= 0;
    }
    public Bounces increment() {
        return new Bounces(remaining-1, bounces+1);
    }

    @Override
    public String toString() {
        return "Bounces{" + "remaining=" + remaining + '}';
    }
    
}
