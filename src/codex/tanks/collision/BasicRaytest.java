/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.collision;

import codex.tanks.systems.VisualState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public class BasicRaytest implements Raytest {
    
    protected Ray ray;
    protected ShapeFilter filter;
    protected CollisionResults results;
    
    public BasicRaytest(Ray ray) {
        this(ray, null, new CollisionResults());
    }
    public BasicRaytest(Ray ray, ShapeFilter filter) {
        this(ray, filter, new CollisionResults());
    }
    public BasicRaytest(Ray ray, ShapeFilter filter, CollisionResults results) {
        this.ray = ray;
        this.filter = filter;
        this.results = results;
    }

    @Override
    public void cast(CollisionState state) {
        Raytest.raycast(state, ray, filter, results);
    }
    @Override
    public CollisionResult getCollision() {
        if (results.size() == 0) return null;
        return results.getClosestCollision();
    }
    @Override
    public EntityId getCollisionEntity() {
        var closest = getCollision();
        if (closest != null) {
            return VisualState.fetchId(closest.getGeometry(), -1);
        }
        return null;
    }

    public Ray getRay() {
        return ray;
    }
    public void setRay(Ray ray) {
        this.ray = ray;
    }
    public ShapeFilter getFilter() {
        return filter;
    }
    public void setFilter(ShapeFilter filter) {
        this.filter = filter;
    }
    
    public CollisionResults getResults() {
        return results;
    }
    
}
