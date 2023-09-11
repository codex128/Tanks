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
    private final EntityId scene;
    
    public Visual() {
        this.model = null;
        this.scene = null;
    }
    public Visual(String model) {
        this.model = model;
        this.scene = null;
    }
    public Visual(EntityId scene) {
        this.model = null;
        this.scene = scene;
    }
    public Visual(String model, EntityId scene) {
        this.model = model;
        this.scene = scene;
    }
    
    public String getModel() {
        return model;
    }
    public EntityId getScene() {
        return scene;
    }
    @Override
    public String toString() {
        return "Visual{" + "model=" + model + " scene=" + scene + '}';
    }
    
    public Visual setScene(EntityId scene) {
        return new Visual(model, scene);
    }
    
}
