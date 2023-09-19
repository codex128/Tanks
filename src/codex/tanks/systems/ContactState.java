/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.collision.ContactEvent;
import codex.tanks.collision.RaytestSystem;
import codex.tanks.collision.ShapeFilter;
import codex.tanks.components.Bounces;
import codex.tanks.components.CollisionShape;
import codex.tanks.components.ContactResponse;
import codex.tanks.components.Owner;
import codex.tanks.components.Visual;
import codex.tanks.es.ESAppState;
import codex.tanks.es.EntityAccess;
import codex.tanks.util.GameUtils;
import com.jme3.app.Application;
import com.jme3.scene.Spatial;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;
import java.util.Iterator;

/**
 *
 * @author codex
 */
public class ContactState extends ESAppState implements Iterable<Spatial> {
    
    private EntitySet shapes;
    private RaytestSystem raytester;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        shapes = ed.getEntities(Visual.class, CollisionShape.class);
        raytester = new RaytestSystem(this);
        raytester.start();
    }
    @Override
    protected void cleanup(Application app) {
        shapes.release();
        raytester.stopThread();
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
    
    public RaytestSystem getRaytester() {
        return raytester;
    }
    
    public void triggerContactEvent(ContactEvent event) {
        if (authorizeContactEvent(event)) {
            var reaction = ed.getComponent(event.target, ContactResponse.class);
            if (reaction != null) {
                factory.getContactMethods().respond(reaction, event);
            }
        }
    }
    private boolean authorizeContactEvent(ContactEvent event) {
        var owner = ed.getComponent(event.projectile.getId(), Owner.class);
        return owner == null || !owner.isOwner(event.target) || event.projectile.get(Bounces.class).getBouncesMade() > 0;
    }
    
    private class ShapeIterator implements Iterator<Spatial> {
        
        ShapeFilter filter;
        Iterator<Entity> delegate;
        EntityAccess access = new EntityAccess(ed, null);
        Spatial next;
        
        ShapeIterator() {
            shapes.applyChanges();
            delegate = shapes.iterator();
        }
        ShapeIterator(ShapeFilter filter) {
            this();
            this.filter = filter;
        }
        
        @Override
        public boolean hasNext() {
            if (next != null) return true;
            while (delegate.hasNext()) {
                var e = delegate.next();
                if (!isEntityRoomActive(e.getId())) {
                    continue;
                }
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
