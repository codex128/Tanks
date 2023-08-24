/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.CollisionShape;
import codex.tanks.util.GameUtils;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author gary
 */
public class DefensivePoints extends RandomPoints {
    
    private float pointAngle;
    
    public DefensivePoints(J3map source) {
        super(source);
        fetchComponents();
    }
    
    private void fetchComponents() {
        pointAngle = source.getFloat("pointAngle", .25f);
    }
    
    @Override
    public void moveTank(AlgorithmUpdate update) {
        if (update.isConsumed()) {
            stack.clear();
            return;
        }
        Vector3f position = update.getTank().getPosition().setY(0f);
        if (stack.isEmpty() || position.distanceSquared(stack.getLast()) < radius*radius) {
            stack.addLast(getNextPoint(update, getNextDirection(getDirectionToTarget(update)), .1f, maxPointDistance, radius, 5));
            stack.getLast().setY(0f);
        }
        if (stack.size() > stacksize) {
            stack.removeFirst();
        }
        update.getTank().move(stack.getLast().subtract(position).normalizeLocal());
        CollisionShape shape = GameUtils.target(update.getGame().getCollisionShapes(), update.getTank().getAimRay(), update.getTank(), 0);
        if (shape == update.getPlayerTank() && stack.size() >= 2) {
            stack.removeLast();
        }
        update.consume();
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
