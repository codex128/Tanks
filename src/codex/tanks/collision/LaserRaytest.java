/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.collision;

import codex.tanks.components.ContactReaction;
import codex.tanks.systems.VisualState;
import codex.tanks.util.GameUtils;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.simsilica.es.EntityId;

/**
 * Raytest implementation that accounts for ricocheting.
 * 
 * @author codex
 */
public class LaserRaytest implements Raytest {
    
    // input
    protected Ray ray = new Ray();
    protected OriginFilter filter;
    protected int maxBounces;
    
    // output
    protected EntityId collisionEntity;
    protected CollisionResult result;
    
    public LaserRaytest(Ray ray, EntityId ignore, int maxBounces) {
        this(ray, new OriginFilter(ignore, null), maxBounces);
    }
    public LaserRaytest(Ray ray, OriginFilter filter, int maxBounces) {
        this.ray = ray;
        this.filter = filter;
        this.maxBounces = maxBounces;
    }
    
    @Override
    public void cast(CollisionState state) {
        var r = new Ray();
        r.set(this.ray);
        var results = new CollisionResults();
        var originalId = filter.getRoot();
        result = null;
        collisionEntity = null;
        do {
            Raytest.raycast(state, r, filter, results);
            if (results.size() > 0) {
                var closest = results.getClosestCollision();
                var id = VisualState.fetchId(closest.getGeometry(), -1);
                var reaction = state.getEntityData().getComponent(id, ContactReaction.class);
                if (reaction != null && maxBounces > 0 && reaction.ricochet()) {
                    r.setOrigin(closest.getContactPoint());
                    r.setDirection(GameUtils.ricochet(r.getDirection(), closest.getContactNormal()));
                    filter.setRoot(id);
                }
                else {
                    collisionEntity = id;
                    result = closest;
                    break;
                }
            }
            else break;
            results.clear();
        } while (maxBounces-- > 0);
        filter.setRoot(originalId);
    }
    @Override
    public EntityId getCollisionEntity() {
        return collisionEntity;
    }
    @Override
    public CollisionResult getCollision() {
        return result;
    }

    public void setRay(Ray ray) {
        this.ray = ray;
    }
    public void setFilter(OriginFilter filter) {
        this.filter = filter;
    }
    public void setMaxBounces(int maxBounces) {
        this.maxBounces = maxBounces;
    }
    
}
