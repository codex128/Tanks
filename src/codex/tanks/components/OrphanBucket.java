/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 * Contains components which are added to the entity when
 * another entity is removed.
 * 
 * @author codex
 */
public class OrphanBucket implements EntityComponent {
    
    private final EntityId parent;
    private final EntityComponent[] components;
    
    public OrphanBucket(EntityId parent, EntityComponent... components) {
        this.parent = parent;
        this.components = components;
    }
    
    public EntityId getParent() {
        return parent;
    }
    public EntityComponent[] getComponents() {
        return components;
    }
    @Override
    public String toString() {
        return "OrphanBucket{" + "parent=" + parent + ", components=" + components + '}';
    }
    
    public void apply(EntityData ed, EntityId entity) {
        ed.setComponents(entity, components);
    }
    
}
