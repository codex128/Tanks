/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import com.jme3.collision.CollisionResult;
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
    protected float maxPointDistance;
    protected boolean updateOccured = false;
    
    public RandomPoints(float maxPointDistance) {
        this.maxPointDistance = maxPointDistance;
    }
    
    @Override
    public void initialize(AlgorithmUpdate update) {
        updateOccured = false;
    }    
    @Override
    public boolean move(AlgorithmUpdate update) {
        Vector3f position = update.getTank().getPosition().setY(0f);
        if (stack.isEmpty() || position.distanceSquared(stack.getLast()) < radius*radius) {
            stack.addLast(getNextPoint(update, getNextDirection(update.getDirectionToPlayer()), .1f, maxPointDistance, radius, 5));
            stack.getLast().setY(0f);
        }
        if (stack.size() > stacksize) {
            stack.removeFirst();
        }
        update.getTank().move(stack.getLast().subtract(position).normalizeLocal());
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
    public void cleanup(AlgorithmUpdate update) {
        if (!updateOccured) {
            stack.clear();
        }
    }
    
    protected Vector3f getNextDirection(Vector3f dirToTarget) {
        Quaternion q = new Quaternion().fromAngleAxis(FastMath.rand.nextFloat()*FastMath.TWO_PI, Vector3f.UNIT_Y);
        return q.mult(Vector3f.UNIT_Z);
    }
    protected Vector3f getNextPoint(AlgorithmUpdate update, Vector3f direction,
            float minDist, float maxDist, float radius, int attempts) {
        while (attempts-- > 0) {
            var ray = new Ray(update.getTank().getProbeLocation(), direction);
            var results = update.getCollisionState().raycast(ray, update.getTank().getEntity().getId());
            if (results.size() > 0) {
                var closest = results.getClosestCollision();
                if (closest.getDistance()-radius < minDist) {
                    continue;
                }
                else if (closest.getDistance()-radius < maxDist) {
                    return closest.getContactPoint().add(closest.getContactNormal().mult(radius));
                }
            }
            return ray.getOrigin().add(ray.getDirection().mult(maxDist));
        }
        return update.getTank().getPosition();
    }
    
}
