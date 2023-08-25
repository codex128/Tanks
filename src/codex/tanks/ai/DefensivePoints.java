/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.tanks.util.GameUtils;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityId;

/**
 *
 * @author gary
 */
public class DefensivePoints extends RandomPoints {
    
    private final float pointAngle;
    
    public DefensivePoints(float maxPointDist, float pointAngle) {
        super(maxPointDist);
        this.pointAngle = pointAngle;
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
//        EntityId id = update.getCollisionState().raycast(update.getTank().getAimRay(), update.getTank().getEntity().getId(), 0);
//        if (id == update.getPlayerTank().getEntity().getId() && stack.size() >= 2) {
//            stack.removeLast();
//        }
        return true;
    }
    @Override
    protected Vector3f getNextDirection(Vector3f dirToTarget) {
        float angleToTarget = dirToTarget.angleBetween(Vector3f.UNIT_Z);
        int turn = FastMath.rand.nextBoolean() ? 1 : -1;
        float angle = GameUtils.gaussian(angleToTarget+FastMath.HALF_PI*turn, FastMath.PI*pointAngle);
        Quaternion q = new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y);
        return q.mult(Vector3f.UNIT_Z);
    }
    
}
