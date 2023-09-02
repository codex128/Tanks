/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import codex.tanks.effects.MatChange;
import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class MaterialUpdate implements EntityComponent {
    
    private final String rootSpatial;
    private final MatChange[] updates;

    public MaterialUpdate(MatChange... updates) {
        this(null, updates);
    }
    public MaterialUpdate(String rootSpatial, MatChange... updates) {
        this.rootSpatial = rootSpatial;
        this.updates = updates;
    }

    public String getRootSpatial() {
        return rootSpatial;
    }
    public MatChange[] getUpdates() {
        return updates;
    }
    @Override
    public String toString() {
        return "MaterialUpdate{" + "updates=" + updates.length + '}';
    }
    
}
