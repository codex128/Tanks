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
