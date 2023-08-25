/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public class Copy implements EntityComponent {
    
    public static final String
            LIFE = "life", 
            TRANSFORM = "transform";
    
    private final EntityId copy;
    private final String[] modes;
    
    public Copy(EntityId copy, String... mode) {
        this.copy = copy;
        this.modes = mode;
    }

    public EntityId getCopy() {
        return copy;
    }
    public String[] getModes() {
        return modes;
    }
    public boolean supports(String mode) {
        for (var m : modes) {
            if (m.equals(mode)) return true;
        }
        return false;
    }
    @Override
    public String toString() {
        return "Copy{" + "copy=" + copy + ", mode=" + modes + '}';
    }
    
}
