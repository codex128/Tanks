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
public class CreateOnDeath implements EntityComponent {
    
    private final String model;
    
    public CreateOnDeath(String model) {
        this.model = model;
    }
    
    public String getModel() {
        return model;
    }
    @Override
    public String toString() {
        return "CreateOnDeath{" + "model=" + model + '}';
    }
    
}
