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
public class EmitterComponent implements EntityComponent {
    
    private final String model;
    
    public EmitterComponent(String model) {
        this.model = model;
    }
    
    public String getModel() {
        return model;
    }
    @Override
    public String toString() {
        return "EmitterComponent{" + "model=" + model + '}';
    }
    
}
