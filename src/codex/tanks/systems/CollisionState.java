/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.collision.ContactEvent;
import codex.tanks.collision.ShapeFilter;
import codex.tanks.components.ContactReaction;
import codex.tanks.components.CollisionShape;
import codex.tanks.components.Visual;
import codex.tanks.util.ESAppState;
import codex.tanks.util.EntityAccess;
import codex.tanks.util.GameUtils;
import com.jme3.app.Application;
import com.jme3.collision.CollisionResult;
import com.jme3.scene.Spatial;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.HashMap;
import java.util.Iterator;
import codex.tanks.collision.ContactEventPipeline;
import codex.tanks.components.Alive;
import codex.tanks.components.Bounces;
import codex.tanks.components.Velocity;

/**
 *
 * @author codex
 */
public class CollisionState extends ESAppState implements Iterable<Spatial> {
    
    private EntitySet shapes;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        shapes = ed.getEntities(Visual.class, CollisionShape.class);
    }
    @Override
    protected void cleanup(Application app) {
        shapes.release();
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        shapes.applyChanges();
    }
    
    @Override
    public Iterator<Spatial> iterator() {
        return new ShapeIterator();
    }
    public Iterator<Spatial> iterator(ShapeFilter filter) {
        return new ShapeIterator(filter);
    }
    
    public void bulletContact(EntityId target, Entity bullet, CollisionResult collision) {
        // todo: fill in this method
    }
    
    private class ShapeIterator implements Iterator<Spatial> {
        
        ShapeFilter filter;
        Iterator<Entity> delegate = shapes.iterator();
        EntityAccess access = new EntityAccess(ed, null);
        Spatial next;
        
        ShapeIterator() {}
        ShapeIterator(ShapeFilter filter) {
            this.filter = filter;
        }
        
        @Override
        public boolean hasNext() {
            if (next != null) return true;
            while (delegate.hasNext()) {
                var e = delegate.next();
                access.setEntityId(e.getId());
                var shape = e.get(CollisionShape.class);
                if (filter != null && !filter.filter(access, shape)) {
                    continue;
                }
                if (shape.getShape() == null) {
                    next = visuals.getSpatial(e.getId());
                }
                else {
                    next = GameUtils.getChild(visuals.getSpatial(e.getId()), shape.getShape());
                }
                if (next != null) {
                    return true;
                }
            }
            return false;
        }
        @Override
        public Spatial next() {
            Spatial n = next;
            next = null;
            return n;
        }
        
    }
        
}