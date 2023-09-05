/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.collision;

import codex.tanks.systems.CollisionState;
import codex.tanks.systems.VisualState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityId;
import java.util.Iterator;

/**
 *
 * @author codex
 */
public class SegmentedRaytest implements Raytest, Iterable<CollisionResult> {
    
    private final CollisionState collisionState;
    private Ray ray;
    private EntityId origin;
    private ShapeFilter filter;
    private ShapeFilter paddingFilter;
    private float distance; // >0 = distanced, <=0 = not distanced
    private float padding; // >0 = padding, <=0 = no padding
    private int bounces; // >=0 = bounce constrained, <0 = not bounce constrained
    private boolean mergeResults = true;
    
    public SegmentedRaytest(CollisionState collisionState, Ray ray, EntityId origin, ShapeFilter filter,
            ShapeFilter paddingFilter, float distance, float padding, int bounces) {
        this.collisionState = collisionState;
        this.ray = ray;
        this.origin = origin;
        this.filter = filter;
        this.paddingFilter = paddingFilter;
        this.distance = distance;
        this.padding = padding;
        this.bounces = bounces;
    }
    
    @Override
    public void cast(CollisionState state) {}
    @Override
    public Iterator<CollisionResult> iterator() {
        return new SegmentIterator(this);
    }
    
    public class SegmentIterator implements Iterator<CollisionResult> {

        private static final int LEFT = -1, MIDDLE = 0, RIGHT = 1;
        
        private Ray ray = new Ray();
        private EntityId origin;
        private float distance;
        private int bounces;
        private int alignment = MIDDLE;
        private CollisionResults results;
        
        private SegmentIterator(SegmentedRaytest parent) {
            ray.set(parent.ray);
            origin = parent.origin;
            distance = parent.distance;
            bounces = parent.bounces;
        }
        
        @Override
        public boolean hasNext() {
            return bounces >= 0 && distance > 0;
        }
        @Override
        public CollisionResult next() {
            results = new CollisionResults();
            var across = ray.getDirection().cross(Vector3f.UNIT_Y).normalizeLocal().multLocal(padding); // this might point left instead of right
            var padRight = new Ray(Vector3f.ZERO, ray.direction);
            ray.getOrigin().add(across.mult(alignment-RIGHT), padRight.origin);
            var padLeft = new Ray(Vector3f.ZERO, ray.direction);
            ray.getOrigin().add(across.mult(alignment-LEFT), padLeft.origin);
            Raytest.raycast(collisionState, padRight, new OriginFilter(origin, paddingFilter), results);
            Raytest.raycast(collisionState, padLeft, new OriginFilter(origin, paddingFilter), results);
            ray.origin.addLocal(across.mult(alignment));
            Raytest.raycast(collisionState, ray, new OriginFilter(origin, filter), results);
            if (results.size() > 0) {
                var closest = results.getClosestCollision();
                if (closest.getDistance() > distance) {
                    results.clear();
                    return null;
                }
                else {
                    // configure next alignment
                    var dotMiddle = ray.direction.dot(closest.getContactPoint().subtract(ray.origin).normalizeLocal());
                    var dotRight = padRight.direction.dot(closest.getContactPoint().subtract(padRight.origin).normalizeLocal());
                    var dotLeft = padLeft.direction.dot(closest.getContactPoint().subtract(padLeft.origin).normalizeLocal());
                    if (dotMiddle >= dotRight && dotMiddle >= dotLeft) alignment = MIDDLE;
                    else if (dotRight >= dotLeft) alignment = RIGHT;
                    else alignment = LEFT;
                    // fetch collision entity
                    origin = VisualState.fetchId(closest.getGeometry(), -1);
                    // close distance
                    distance -= closest.getDistance();
                    bounces--;
                    return closest;
                }
            }
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
        
    }
    
}
