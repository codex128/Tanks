/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.factories;

import codex.tanks.es.ComponentBucket;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public class EntityBlueprint {
    
    private final EntityData ed;
    private ComponentBucket components = new ComponentBucket();
    
    public EntityBlueprint(EntityData ed) {
        this.ed = ed;
    }
    public EntityBlueprint(EntityData ed, EntityComponent... components) {
        this.ed = ed;
        this.components.setComponents(components);
    }
    
    public EntityBlueprint setComponents(EntityComponent... components) {
        this.components.setComponents(components);
        return this;
    }
    
    public ComponentBucket getComponents() {
        return components;
    }
    
    public EntityId create(EntityComponent... variables) {
        var id = ed.createEntity();
        components.apply(ed, id);
        ed.setComponents(id, variables);
        return id;
    }
    public void apply(EntityId id) {
        components.apply(ed, id);
    }
    
    public EntityBlueprint copy() {
        var b = new EntityBlueprint(ed);
        b.setComponents(components.getComponents());
        return b;
    }
    
}
