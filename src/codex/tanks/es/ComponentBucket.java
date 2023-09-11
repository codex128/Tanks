/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.es;

import codex.tanks.components.OrphanBucket;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public class ComponentBucket {
    
    private EntityComponent[] bucket;
    
    public ComponentBucket() {}
    public ComponentBucket(EntityComponent... bucket) {
        this.bucket = bucket;
    }

    public void apply(EntityData ed, EntityId id) {
        ed.setComponents(id, bucket);
    }
    public OrphanBucket apply(EntityId id) {
        return new OrphanBucket(id, bucket);
    }
    
    public void setComponents(EntityComponent... bucket) {
        this.bucket = bucket;
    }
    
    public <T extends EntityComponent> T get(Class<T> type) {
        for (var c : bucket) {
            if (c.getClass().equals(type)) {
                return (T)c;
            }
        }
        return null;
    }
    public EntityComponent[] getComponents() {
        return bucket;
    }
    
    public boolean hasComponents() {
        return bucket != null && bucket.length > 0;
    }
    
}
