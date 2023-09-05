/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.collision;

import codex.tanks.systems.ContactState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;

/**
 *
 * @author codex
 */
public class PaddedRaytest extends BasicRaytest {
    
    private final ShapeFilter paddingFilter;
    private final float padding;
    private boolean merge = false;
    private boolean impeded = false;
    
    public PaddedRaytest(Ray ray, ShapeFilter mainFilter, float padding, ShapeFilter paddingFilter, CollisionResults results) {
        super(ray, mainFilter, results);
        this.padding = padding;
        this.paddingFilter = paddingFilter;
    }
    
    @Override
    public void cast(ContactState state) {
        Ray pad1 = new Ray(), pad2 = new Ray();
        var left = new Vector3f();
        new Quaternion().lookAt(ray.getDirection(), Vector3f.UNIT_Y).getRotationColumn(0, left);
        left.multLocal(padding);
        ray.getOrigin().add(left, pad1.origin);
        ray.getOrigin().subtract(left, pad2.origin);
        pad1.setDirection(ray.getDirection());
        pad2.setDirection(ray.getDirection());
        results.clear();
        Raytest.raycast(state, ray, filter, results);
        CollisionResult closest = null;
        if (results.size() > 0) {
            closest = results.getClosestCollision();
        }
        if (!impeded) {
            var padResults = new CollisionResults();
            if (merge) padResults = results;
            Raytest.raycast(state, pad1, paddingFilter, padResults);
            Raytest.raycast(state, pad2, paddingFilter, padResults);
            if (!merge && results.size() > 0) {
                if (closest != null) {
                    impeded = results.getClosestCollision().getDistance() < closest.getDistance();
                }
                else {
                    impeded = true;
                }
            }
        }
    }
    
    public void setResultMergingEnabled(boolean merge) {
        this.merge = merge;
    }
    
    public boolean isResultMergingEnabled() {
        return merge;
    }
    public boolean isImpeded() {
        return false;
    }
    
}
