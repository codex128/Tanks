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
public class Visual implements EntityComponent {
    
    private final String model;
    private boolean independent = false;
    
    public Visual() {
        this(null);
    }
    public Visual(String model) {
        this.model = model;
    }
    
    /**
     * Set this as independent.
     * <p>If true, then the spatial associated with this component
     * will not be detached from the scene graph by {@link VisualState}
     * when the entity is removed. It is up to other systems to handle
     * the spatial after entity death.
     * @param i
     * @return this visual instance
     */
    public Visual setIndependent(boolean i) {
        this.independent = i;
        return this;
    }
    
    public String getModel() {
        return model;
    }
    public boolean isCustom() {
        return model == null;
    }
    public boolean isIndependent() {
        return independent;
    }
    @Override
    public String toString() {
        return "Visual{" + "model=" + model + '}';
    }
    
}
