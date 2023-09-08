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
    
    public static final int NORMAL = 0, UNAFFECTED = 1;
    
    private final boolean alive;
    private final int level;
    
    public Alive() {
        this(true, 0);
    }
    public Alive(boolean alive) {
        this(alive, 0);
    }
    public Alive(int level) {
        this(true, level);
    }
    public Alive(boolean alive, int level) {
        this.alive = alive;
        this.level = level;
    }

    public boolean isAlive() {
        return alive;
    }
    public Alive kill() {
        return new Alive(false);
    }
    public Alive kill(int level) {
        if (this.level > level) return this;
        else return new Alive(false, this.level);
    }
    @Override
    public String toString() {
        return "Alive{" + "alive=" + alive + '}';
    }
    
}
