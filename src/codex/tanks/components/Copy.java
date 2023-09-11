/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import codex.tanks.es.FunctionFilter;
import com.simsilica.es.ComponentFilter;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public class Copy implements EntityComponent {
    
    public static final String
        LIFE = "copy:life", 
        TRANSFORM = "copy:transform";
    
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
    public boolean supportsAny(String... m) {
        for (var mode : modes) {
            for (var check : m) {
                if (mode.equals(check)) return true;
            }
        }
        return false;
    }
    @Override
    public String toString() {
        return "Copy{" + "copy=" + copy + ", mode=" + modes + '}';
    }
    
    public static ComponentFilter<Copy> filter(String mode) {
        return new FunctionFilter<>(Copy.class, c -> c.supports(mode));
    }
    public static ComponentFilter<Copy> filter(String... mode) {
        return new FunctionFilter<>(Copy.class, c -> c.supportsAny(mode));
    }
    
}
