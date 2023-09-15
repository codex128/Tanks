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
public class EntityLight implements EntityComponent {
    
    public static final int DIRECTIONAL = 0, POINT = 1, SPOT = 2, AMBIENT = 3;
    
    private final int type;
    public final boolean added;
    
    public EntityLight(int type) {
        this(type, false);
    }
    private EntityLight(int type, boolean added) {
        this.type = type;
        this.added = added;
    }

    public int getType() {
        return type;
    }
    public boolean isAdded() {
        return added;
    }
    @Override
    public String toString() {
        return "EntityLight{" + "type=" + type + ", added=" + added + '}';
    }
    
    public EntityLight set(boolean added) {
        return new EntityLight(type, added);
    }
    
    public static ComponentFilter<EntityLight> filter(int type) {
        return new FunctionFilter<>(EntityLight.class, c -> c.getType() == type);
    }
    
}
