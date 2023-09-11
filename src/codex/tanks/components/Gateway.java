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
public class Gateway implements EntityComponent {
    
    private final boolean ready;
    
    public Gateway() {
        this(true);
    }
    public Gateway(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }
    @Override
    public String toString() {
        return "Gateway{" + "ready=" + ready + '}';
    }
    
}
