/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.GameUtils;
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
public class RandomPoints implements TankAlgorithm {
    
    protected final J3map source;
    protected final LinkedList<Vector3f> stack = new LinkedList<>();
    protected final float radius = 1.5f;
    protected int stacksize = 1;
    protected float maxPointDistance;
    
    public RandomPoints(J3map source) {
        this.source = source;
        fetchComponents();
    }
    
    private void fetchComponents() {
        maxPointDistance = source.getFloat("maxPointDistance", 10f);
    }
    
    @Override
    public void updateTank(AlgorithmUpdate update) {}    
    @Override
    public void moveTank(AlgorithmUpdate update) {
        if (update.isConsumed()) return;
        Vector3f position = update.getTank().getPosition().setY(0f);
        if (stack.isEmpty() || position.distanceSquared(stack.getLast()) < radius*radius) {
            stack.addLast(getNextPoint(update, getNextDirection(getDirectionToTarget(update)), .1f, maxPointDistance, radius, 5));
            stack.getLast().setY(0f);
        }
        if (stack.size() > stacksize) {
            stack.removeFirst();
        }
        update.getTank().move(stack.getLast().subtract(position).normalizeLocal(), update.getTpf());
        update.consume();
    }
    @Override
    public void aimTank(AlgorithmUpdate update) {}
    @Override
    public void mineTank(AlgorithmUpdate update) {}
    
    protected Vector3f getNextDirection(Vector3f dirToTarget) {
        Quaternion q = new Quaternion().fromAngleAxis(FastMath.rand.nextFloat()*FastMath.TWO_PI, Vector3f.UNIT_Y);
        return q.mult(Vector3f.UNIT_Z);
    }
    protected Vector3f getNextPoint(AlgorithmUpdate update, Vector3f direction, float minDist, float maxDist, float radius, int attempts) {
        while (attempts-- > 0) {
            Ray ray = new Ray(update.getTank().getProbeLocation(), direction);
            CollisionResults results = GameUtils.raycast(update.getGame().getCollisionShapes(), ray, update.getTank());
            if (results.size() > 0) {
                CollisionResult closest = results.getClosestCollision();
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
