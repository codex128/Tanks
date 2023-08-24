/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.CollisionShape;
import codex.tanks.util.GameUtils;
import com.jme3.math.FastMath;

/**
 *
 * @author gary
 */
public class Sniper implements TankAlgorithm {
    
    private final J3map source;
    private float barrelSpeed;
    private float distance = 0f;
    
    public Sniper(J3map source) {
        this.source = source;
        fetchComponents();
    }
    
    private void fetchComponents() {
        barrelSpeed = source.getFloat("barrelSpeed", FastMath.PI*0.01f);
    }
    
    @Override
    public void updateTank(AlgorithmUpdate update) {}
    @Override
    public void moveTank(AlgorithmUpdate update) {}
    @Override
    public void aimTank(AlgorithmUpdate update) {
        if (update.isConsumed()) return;
        if (FastMath.abs(distance) < barrelSpeed) {
            distance = GameUtils.random(-FastMath.TWO_PI, FastMath.TWO_PI);
        }
        float angle = barrelSpeed*FastMath.sign(distance);
        update.getTank().rotateAim(angle);
        distance -= angle;
        CollisionShape shape = GameUtils.target(update.getGame().getCollisionShapes(), update.getTank().getAimRay(), update.getTank(), update.getTank().getModel().getMaxBounces());
        if (shape != null && shape == update.getGame().getPlayerState().getTank()) {
            update.getGame().addBullet(update.getTank().shoot());
        }
        update.consume();
    }
    @Override
    public void mineTank(AlgorithmUpdate update) {}
    
}
