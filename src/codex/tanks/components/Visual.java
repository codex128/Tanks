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
public class Visual implements EntityComponent {
    
    private final String model;
    private EntityId parent;
    
    public Visual() {
        this(null);
    }
    public Visual(String model) {
        this.model = model;
    }
    public Visual(EntityId parent, String model) {
        this.parent = parent;
        this.model = model;
    }
    
    public String getModel() {
        return model;
    }
    public EntityId getParent() {
        return parent;
    }
    public boolean isCustom() {
        return model == null;
    }
    @Override
    public String toString() {
        return "Visual{" + "model=" + model + '}';
    }
    
}
