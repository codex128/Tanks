/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.collision.PaddedRaytest;
import codex.tanks.collision.ShapeFilter;
import codex.tanks.components.EntityTransform;
import codex.tanks.components.ProbeLocation;
import com.jme3.collision.CollisionResults;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import java.util.LinkedList;

/**
 *
 * @author gary
 */
public class RandomPoints implements Algorithm {
    
    protected final LinkedList<Vector3f> stack = new LinkedList<>();
    protected final float radius = 1.5f;
    protected int stacksize = 1;
    protected float maxPointDistance = 10f;
    protected boolean updateOccured = false;
    
    public RandomPoints() {}
    public RandomPoints(J3map source) {
        maxPointDistance = source.getFloat("maxPointDistance", maxPointDistance);
    }
    
    @Override
    public void update(AlgorithmUpdate update) {
        updateOccured = false;
    }    
    @Override
    public boolean move(AlgorithmUpdate update) {
        Vector3f position = update.getComponent(EntityTransform.class).getTranslation().clone().setY(0f);
        if (stack.isEmpty() || position.distanceSquared(stack.getLast()) < radius*radius) {
            stack.addLast(getNextPoint(update, .1f, maxPointDistance, radius, 5));
            stack.getLast().setY(0f);
        }
        if (stack.size() > stacksize) {
            stack.removeFirst();
        }
        update.drive(stack.getLast().subtract(position).normalizeLocal());
        updateOccured = true;
        return true;
    }
    @Override
    public boolean aim(AlgorithmUpdate update) {
        return false;
    }
    @Override
    public boolean shoot(AlgorithmUpdate update) {
        return false;
    }
    @Override
    public boolean mine(AlgorithmUpdate update) {
        return false;
    }    
    @Override
    public void endUpdate(AlgorithmUpdate update) {
        if (!updateOccured) {
            stack.clear();
        }
    }
    
    protected Vector3f getNextDirection(Vector3f dirToTarget) {
        Quaternion q = new Quaternion().fromAngleAxis(FastMath.rand.nextFloat()*FastMath.TWO_PI, Vector3f.UNIT_Y);
        return q.mult(Vector3f.UNIT_Z);
    }
    protected Vector3f getNextPoint(AlgorithmUpdate update, float minDist, float maxDist, float radius, int attempts) {
        var ray = new Ray();
        while (attempts-- > 0) {
            ray.setOrigin(update.getComponent(ProbeLocation.class).getLocation());
            ray.setDirection(getNextDirection(update.getDirectionToTarget()));
            var filter = ShapeFilter.notId(update.getAgentId());
            var test = new PaddedRaytest(ray, filter, 1f, filter, new CollisionResults());
            test.setResultMergingEnabled(true);
            test.cast(update.getCollisionState());
            var closest = test.getCollision();
            if (closest != null) {
                if (closest.getDistance()-radius < minDist) {
                    continue;
                }
                else if (closest.getDistance()-radius < maxDist) {
                    return closest.getContactPoint().add(closest.getContactNormal().mult(radius));
                }
            }
            return ray.getOrigin().add(ray.getDirection().mult(maxDist));
        }
        return update.getComponent(EntityTransform.class).getTranslation().clone();
    }
    
}
