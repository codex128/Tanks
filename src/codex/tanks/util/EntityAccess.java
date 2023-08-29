/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.util;

import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public class EntityAccess {
    
    public final EntityData ed;
    public EntityId id;
    
    public EntityAccess(EntityData ed, EntityId id) {
        this.ed = ed;
        this.id = id;
    }

    public EntityData getEntityData() {
        return ed;
    }
    public EntityId getEntityId() {
        return id;
    }
    
    public void setEntityId(EntityId id) {
        this.id = id;
    }
    
}
