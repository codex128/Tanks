/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import com.simsilica.es.Entity;

/**
 *
 * @author codex
 * @param <T>
 */
public abstract class AbstractAlgorithm <T extends AIComponent> implements Algorithm {
    
    protected final Entity entity;
    protected final Class<T> componentType;
    
    public AbstractAlgorithm(Entity entity, Class<T> componentType) {
        if (entity.get(componentType) == null) {
            throw new IllegalArgumentException("Entity does not contain necessary AI component!");
        }
        this.entity = entity;
        this.componentType = componentType;
    }
    
    public Entity getEntity() {
        return entity;
    }
    public Class<T> getComponentType() {
        return componentType;
    }
    public T getComponent() {
        return entity.get(componentType);
    }
    
}
