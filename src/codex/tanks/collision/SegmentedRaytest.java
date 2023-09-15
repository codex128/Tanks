/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.collision;

import codex.tanks.systems.ContactState;
import codex.tanks.systems.VisualState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityId;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author codex
 */
public class SegmentedRaytest implements Iterable<CollisionResult> {
    
    private final ContactState collisionState;
    private Ray ray;
    private EntityId origin;
    private ShapeFilter filter;
    private ShapeFilter firstCastFilter;
    private float distance;
    private int maxConsecutiveBounces = 10;
    private boolean debug = false;
    
    public SegmentedRaytest(ContactState collisionState) {
        this.collisionState = collisionState;
    }

    public void setRay(Ray ray) {
        this.ray = ray;
    }
    public void setOriginEntity(EntityId origin) {
        this.origin = origin;
    }
    public void setFilter(ShapeFilter filter) {
        this.filter = filter;
    }
    public void setFirstCastFilter(ShapeFilter filter) {
        this.firstCastFilter = filter;
    }
    public void setDistance(float distance) {
        if (distance < 0) this.distance = -1f;
        else this.distance = distance;
    }
    public void setMaxConsecutiveBounces(int maxConsecutiveBounces) {
        this.maxConsecutiveBounces = maxConsecutiveBounces;
    }
    public void enableDebug() {
        debug = true;
    }
    
    @Override
    public SegmentIterator iterator() {
        return new SegmentIterator(this);
    }
    
    public class SegmentIterator implements Iterator<CollisionResult> {
        
        private final Ray ray = new Ray();
        private EntityId origin;
        private float distance;
        private final CollisionResults results = new CollisionResults();
        private final LinkedList<CollisionResult> path = new LinkedList<>();
        private int hits = 0;
        
        private SegmentIterator(SegmentedRaytest parent) {
            ray.set(parent.ray);
            origin = parent.origin;
            distance = parent.distance;
        }
        
        @Override
        public boolean hasNext() {
            return distance != 0 && hits < maxConsecutiveBounces;
        }
        @Override
        public CollisionResult next() {
            results.clear();
            var f = ShapeFilter.and(ShapeFilter.nullProtection(filter), ShapeFilter.nullProtection(firstCastFilter), ShapeFilter.notId(origin));
            Raytest.raycast(collisionState, ray, f, results, debug);
            if (debug) System.out.println("collisions: "+results.size());
            if (results.size() > 0) {
                var closest = results.getClosestCollision();
                if (distance > 0 && closest.getDistance() > distance) {
                    results.clear();
                }
                else {
                    path.addLast(closest);
                    origin = VisualState.fetchId(closest.getGeometry(), -1);
                    ray.origin.set(closest.getContactPoint());
                    if (distance > 0) {
                        distance = Math.max(distance-closest.getDistance(), 0f);
                    }
                    if (debug) System.out.println("distance travelled: "+closest.getDistance());
                    hits++;
                    return closest;
                }
            }
            ray.origin.addLocal(ray.direction.mult(distance));
            origin = null;
            distance = 0;
            return null;
        }
        
        /**
         * Sets the next raytest direction.
         * <p>This should be called at the end of each iteration where
         * the ray ricochets.
         * @param direction 
         */
        public void setNextDirection(Vector3f direction) {
            ray.setDirection(direction);
        }
        
        public Vector3f getDirection() {
            return ray.direction.clone();
        }
        public EntityId getCollisionEntity() {
            return origin;
        }
        public Vector3f getPosition() {
            return ray.origin.clone();
        }
        public int getHitsMade() {
            return hits;
        }
        public LinkedList<CollisionResult> getCollisions() {
            return path;
        }
        
    }
    
}
