/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class Visual implements EntityComponent {
    
    private final String model;
    private final String scene;
    
    public Visual() {
        this(null, null);
    }
    public Visual(String model) {
        this(model, null);
    }
    public Visual(String model, String scene) {
        this.model = model;
        this.scene = scene;
    }
    
    public String getModel() {
        return model;
    }
    public String getScene() {
        return scene;
    }    
    public boolean isCustom() {
        return model == null;
    }
    @Override
    public String toString() {
        return "Visual{" + "model=" + model + ", scene=" + scene + '}';
    }
    
}
