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
public class Activated implements EntityComponent {
    
    private final boolean activated;

    public Activated(boolean activated) {
        this.activated = activated;
    }
    
    public boolean isActivated() {
        return activated;
    }
    @Override
    public String toString() {
        return "Activated{" + "activated=" + activated + '}';
    }
    
}
