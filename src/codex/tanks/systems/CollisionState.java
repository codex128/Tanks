/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.Bullet;
import codex.tanks.components.ContactReaction;
import codex.tanks.components.CollisionShape;
import codex.tanks.components.Visual;
import codex.tanks.util.ESAppState;
import codex.tanks.util.GameUtils;
import com.jme3.app.Application;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.scene.Spatial;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.Iterator;

/**
 *
 * @author codex
 */
public class CollisionState extends ESAppState implements Iterable<Spatial> {
    
    private EntitySet shapes;
    private VisualState visuals;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        shapes = ed.getEntities(Visual.class, CollisionShape.class);
        visuals = getState(VisualState.class, true);
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
        return new RawShapeIterator();
    }
    public Iterator<Spatial> iterator(EntityId ignore) {
        return new RawShapeIterator(ignore);
    }
    
    public void raycast(Ray ray, EntityId ignore, CollisionResults results) {
        for (Iterator<Spatial> i = iterator(ignore); i.hasNext();) {
            var s = i.next();
            s.collideWith(ray, results);
        }
    }
    public CollisionResults raycast(Ray ray, EntityId ignore) {
        CollisionResults results = new CollisionResults();
        raycast(ray, ignore, results);
        return results;
    }
    public EntityId raycast(Ray ray, EntityId ignore, int maxBounces) {
        CollisionResults results = new CollisionResults();
        do {
            raycast(ray, ignore, results);
            if (results.size() > 0) {
                var closest = results.getClosestCollision();
                var id = VisualState.fetchId(closest.getGeometry(), -1);
                var r = ed.getComponent(id, ContactReaction.class);
                if (r != null && maxBounces > 0 && r.ricochet()) {
                    ray = new Ray(closest.getContactPoint(), GameUtils.ricochet(ray.getDirection(), closest.getContactNormal()));
                    ignore = id;
                }
                else return id;
            }
            else break;
            results.clear();
        } while (maxBounces-- > 0);
        return null;
    }
    
    public void bulletCollision(EntityId target, Bullet bullet, CollisionResult collision) {
        var r = ed.getComponent(target, ContactReaction.class);
        if (r != null) r.react(ed, target, bullet, collision);
    }
    
    private class RawShapeIterator implements Iterator<Spatial> {
        
        EntityId ignore;
        Iterator<Entity> delegate = shapes.iterator();
        Spatial next;
        
        RawShapeIterator() {}
        RawShapeIterator(EntityId ignore) {
            this.ignore = ignore;
        }
        
        @Override
        public boolean hasNext() {
            if (next != null) return true;
            while (delegate.hasNext()) {
                var e = delegate.next();
                if (ignore != null && e.getId().equals(ignore)) {
                    continue;
                }
                var shape = e.get(CollisionShape.class).getShape();
                if (shape == null) {
                    next = visuals.getSpatial(e.getId());
                }
                else {
                    next = GameUtils.getChild(visuals.getSpatial(e.getId()), shape);
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
