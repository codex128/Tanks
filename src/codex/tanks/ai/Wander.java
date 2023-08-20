/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author gary
 */
public class Wander implements TankAlgorithm {
    
    private final J3map source;
    private float turnSpeed;
    private float wander;
    private float previousTurnBias;
    private float directional;
    private int pturn = 0;
    private float skew = 0f;
    
    public Wander(J3map source) {
        this.source = source;
        fetchComponents();
    }
    
    private void fetchComponents() {
        previousTurnBias = source.getFloat("previousTurnBias", 2.0f);
        turnSpeed = source.getFloat("turnSpeed", FastMath.PI*0.01f);
        wander = source.getFloat("wander", 1f);
        directional = source.getFloat("directional", 1f);
    }
    
    @Override
    public void updateTank(AlgorithmUpdate update) {}
    @Override
    public void moveTank(AlgorithmUpdate update) {
        if (update.isConsumed()) return;
        Vector3f directionToTarget = update.getGame().getPlayerState().getTank().getPosition().subtract(update.getTank().getPosition()).setY(0f).normalizeLocal();
        Vector3f current = update.getTank().getMoveDirection();
        //float distance = GameUtils.getDistance(game.getCollisionShapes(), new Ray(getProbeLocation(), current), this);
        Quaternion q = new Quaternion().lookAt(current, Vector3f.UNIT_Y);
        skew = (float)FastMath.rand.nextGaussian()*FastMath.clamp(1f-directional, 0f, 1f)*FastMath.PI+skew/2;
        q.mult(new Quaternion().fromAngleAxis(skew, Vector3f.UNIT_Y));
        Vector3f left = q.getRotationColumn(0);
        float similarity = left.dot(directionToTarget)*directional;
        float bias = previousTurnBias*pturn;
        pturn = (int)FastMath.sign(similarity+bias+(float)FastMath.rand.nextGaussian());
        update.getTank().rotate(turnSpeed*wander*pturn);
        update.getTank().move(1);
        update.consume();
    }
    @Override
    public void aimTank(AlgorithmUpdate update) {}
    @Override
    public void mineTank(AlgorithmUpdate update) {}
    
}
