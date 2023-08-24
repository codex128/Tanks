/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author gary
 */
public class Wander implements Algorithm {
    
    private final float turnSpeed;
    private final float wander;
    private final float previousTurnBias;
    private final float directional;
    private int pturn = 0;
    private float skew = 0f;
    
    public Wander(float previousTurnBias, float turnSpeed, float wander, float directional) {
        this.previousTurnBias = previousTurnBias;
        this.turnSpeed = turnSpeed;
        this.wander = wander;
        this.directional = directional;
    }
    
    @Override
    public void initialize(AlgorithmUpdate update) {}
    @Override
    public boolean move(AlgorithmUpdate update) {
        Vector3f current = update.getTank().getMoveDirection();
        Quaternion q = new Quaternion().lookAt(current, Vector3f.UNIT_Y);
        skew = (float)FastMath.rand.nextGaussian()*FastMath.clamp(1f-directional, 0f, 1f)*FastMath.PI+skew/2;
        q.mult(new Quaternion().fromAngleAxis(skew, Vector3f.UNIT_Y));
        Vector3f left = q.getRotationColumn(0);
        float similarity = left.dot(update.getDirectionToPlayer())*directional;
        float bias = previousTurnBias*pturn;
        pturn = (int)FastMath.sign(similarity+bias+(float)FastMath.rand.nextGaussian());
        update.getTank().rotate(turnSpeed*wander*pturn);
        update.getTank().move(1);
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
    public void cleanup(AlgorithmUpdate update) {}
    
}
