/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.util.debug;

import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class ComponentWatcher implements EntityComponent {
    
    private final Class[] types;

    public ComponentWatcher(Class... types) {
        this.types = types;
    }

    public Class[] getTypes() {
        return types;
    }
    
}
