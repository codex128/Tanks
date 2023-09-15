/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.collision;

import codex.tanks.components.CollisionShape;
import codex.tanks.es.EntityAccess;
import com.simsilica.es.EntityId;

/**
 * Implementation of {@link ShapeFilter} which ignores a given entity id.
 * 
 * <p>This implementation differs from {@code ShapeFilter.byId()} because {@link LaserRaytest}
 * uses it to specifically filter out the entity in which a single raytest begins.
 * 
 * <p>This filter has no unique effects if it is not the origin filter of a given raytest.
 * 
 * @author codex
 */
public class OriginFilter implements ShapeFilter {
    
    private EntityId origin;
    private final ShapeFilter child;
    
    public OriginFilter(EntityId id, ShapeFilter child) {
        this.origin = id;
        this.child = child;
    }
    
    @Override
    public boolean filter(EntityAccess access, CollisionShape shape) {
        return !access.id.equals(origin) && (child == null || child.filter(access, shape));
    }
    
    public void setOrigin(EntityId id) {
        this.origin = id;
    }
    public EntityId getOrigin() {
        return origin;
    }
    public ShapeFilter getChildFilter() {
        return child;
    }
    
}
