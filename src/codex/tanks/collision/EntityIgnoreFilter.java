/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.collision;

import codex.tanks.collision.ShapeFilter;
import codex.tanks.components.CollisionShape;
import codex.tanks.util.EntityAccess;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public class EntityIgnoreFilter implements ShapeFilter {
    
    private EntityId root;
    private final ShapeFilter child;
    
    public EntityIgnoreFilter(EntityId id, ShapeFilter child) {
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
    
}
