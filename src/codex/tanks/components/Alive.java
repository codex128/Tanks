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
public class Alive implements EntityComponent {
    
    private final boolean alive;
    
    public Alive() {
        this(true);
    }
    public Alive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }
    public Alive kill() {
        return new Alive(false);
    }
    @Override
    public String toString() {
        return "Alive{" + "alive=" + alive + '}';
    }
    
}
