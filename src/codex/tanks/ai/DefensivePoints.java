/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.components.EntityTransform;
import codex.tanks.util.GameUtils;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author gary
 */
public class DefensivePoints extends RandomPoints {
    
    private float pointAngle = 0.25f;
    
    public DefensivePoints() {
        super();
        stacksize = 2;
    }
    public DefensivePoints(J3map source) {
        super(source);
        pointAngle = source.getFloat("pointAngle", pointAngle);
        stacksize = 2;
    }
    
    @Override
    public boolean move(AlgorithmUpdate update) {
        updateOccured = true;
        Vector3f position = update.getComponent(EntityTransform.class).getTranslation().clone().setY(0f);
        if (stack.isEmpty() || position.distanceSquared(stack.getLast()) < radius*radius) {
            stack.addLast(getNextPoint(update, .1f, maxPointDistance, radius, 5));
            stack.getLast().setY(0f);
        }
        if (stack.size() > stacksize) {
            stack.removeFirst();
        }
        update.drive(stack.getLast().subtract(position).normalizeLocal());
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
