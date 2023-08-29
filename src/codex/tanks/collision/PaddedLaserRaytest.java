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
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public class PaddedLaserRaytest extends LaserRaytest {
    
    private float padding;
    private ShapeFilter paddingFilter;
    private boolean impeded = false;
    
    public PaddedLaserRaytest(Ray ray, EntityId ignore, float padding, ShapeFilter paddingFilter, int maxBounces) {
        super(ray, ignore, maxBounces);
        this.padding = padding;
        this.paddingFilter = paddingFilter;
    }
    public PaddedLaserRaytest(Ray ray, OriginFilter mainFilter, float padding, ShapeFilter paddingFilter, int maxBounces) {
        super(ray, mainFilter, maxBounces);
        this.padding = padding;
        this.paddingFilter = paddingFilter;
    }
    
    @Override
    public void cast(CollisionState state) {
        Ray r = new Ray(), pad1 = new Ray(), pad2 = new Ray();
        r.set(this.ray);
        Vector3f left = new Vector3f();
        var results = new CollisionResults();
        var originalId = filter.getRoot();
        var infinite = false;
        result = null;
        collisionEntity = null;
        impeded = false;
        do {
            new Quaternion().lookAt(r.getDirection(), Vector3f.UNIT_Y).getRotationColumn(0, left);
            r.getOrigin().add(left, pad1.origin);
            r.getOrigin().subtract(left, pad2.origin);
            pad1.setDirection(r.getDirection());
            pad2.setDirection(r.getDirection());
            Raytest.raycast(state, r, filter, results);
            CollisionResult closest = null;
            if (results.size() > 0) {
                closest = results.getClosestCollision();
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
                    //break;
                }
            }
            else infinite = true;
            //else break;
            if (!impeded) {
                results.clear();
                Raytest.raycast(state, pad1, paddingFilter, results);
                Raytest.raycast(state, pad2, paddingFilter, results);
                //impeded = results.size() > 0;
                if (results.size() > 0) {
                    if (closest != null) {
                        impeded = results.getClosestCollision().getDistance() < closest.getDistance();
                    }
                    else {
                        impeded = true;
                    }
                }
            }
            if (result != null || infinite) {
                break;
            }
            results.clear();
        } while (maxBounces-- > 0);
        filter.setRoot(originalId);
    }
    
    public boolean isImpeded() {
        return impeded;
    }
    
}
