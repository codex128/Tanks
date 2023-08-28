/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
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
        Vector3f position = update.getTank().getPosition().setY(0f);
        if (stack.isEmpty() || position.distanceSquared(stack.getLast()) < radius*radius) {
            stack.addLast(getNextPoint(update, .1f, maxPointDistance, radius, 5));
            stack.getLast().setY(0f);
        }
        if (stack.size() > stacksize) {
            stack.removeFirst();
        }
        update.getTank().drive(stack.getLast().subtract(position).normalizeLocal());
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
    protected Vector3f getNextPoint(AlgorithmUpdate update, float minDist, float maxDist, float radius, int attempts) {
        while (attempts-- > 0) {
            var direction = getNextDirection(update.getDirectionToPlayer());
            var left = new Quaternion().lookAt(direction, Vector3f.UNIT_Y).getRotationColumn(0);
            var ray1 = new Ray(update.getTank().getProbeLocation().add(left), direction);
            var ray2 = new Ray(update.getTank().getProbeLocation().subtract(left), direction);
            var results = update.getCollisionState().raycast(ray1, update.getTankId());
            update.getCollisionState().raycast(ray2, update.getTankId(), results);
            if (results.size() > 0) {
                var closest = results.getClosestCollision();
                if (closest.getDistance()-radius < minDist) {
                    continue;
                }
                else if (closest.getDistance()-radius < maxDist) {
                    return closest.getContactPoint().add(closest.getContactNormal().mult(radius));
                }
            }
            return ray1.getOrigin().add(ray1.getDirection().mult(maxDist));
        }
        return update.getTank().getPosition();
    }
    
}
