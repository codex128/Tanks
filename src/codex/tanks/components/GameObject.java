/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import codex.tanks.es.FunctionFilter;
import com.simsilica.es.ComponentFilter;
import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class GameObject implements EntityComponent {
    
    public final String type;

    public GameObject(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    @Override
    public String toString() {
        return "GameObject{" + "type=" + type + '}';
    }
    
    public static ComponentFilter<GameObject> filter(String type) {
        return new FunctionFilter<>(GameObject.class, c -> c.getType().equals(type));
    }
    public static ComponentFilter<GameObject> filter(String type, boolean equals) {
        return new FunctionFilter<>(GameObject.class, c -> c.getType().equals(type) == equals);
    }
    
}
