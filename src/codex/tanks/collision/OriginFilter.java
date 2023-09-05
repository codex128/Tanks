/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.collision;

import codex.tanks.components.CollisionShape;
import codex.tanks.util.EntityAccess;
import com.simsilica.es.EntityId;

/**
 * Implementation of {@link ShapeFilter} which ignores a given entity id.
 * 
 * <p>This implementation differs from {@code ShapeFilter.byId()} because {@link LaserRaytest}
 * uses it to specifically filter out the entity in which a single raytest begins.
 * 
 * <p>This filter has no unique effects if it is not the root filter of a given raytest.
 * 
 * @author codex
 */
public class OriginFilter implements ShapeFilter {
    
    private EntityId root;
    private final ShapeFilter child;
    
    public OriginFilter(EntityId id, ShapeFilter child) {
        this.root = id;
        this.child = child;
    }
    
    @Override
    public boolean filter(EntityAccess access, CollisionShape shape) {
        return !access.id.equals(root) && (child == null || child.filter(access, shape));
    }
    
    public void setRoot(EntityId id) {
        this.root = id;
    }
    public EntityId getRoot() {
        return root;
    }
    public ShapeFilter getChildFilter() {
        return child;
    }
    
}
