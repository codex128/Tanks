/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.tanks.components.Bounces;
import codex.tanks.util.GameUtils;
import com.jme3.math.FastMath;

/**
 *
 * @author gary
 */
public class Sniper implements Algorithm {
    
    private final float barrelSpeed;
    private float distance = 0f;
    
    public Sniper(float barrelSpeed) {
        this.barrelSpeed = barrelSpeed;
    }
    
    @Override
    public void initialize(AlgorithmUpdate update) {}
    @Override
    public boolean move(AlgorithmUpdate update) {
        return false;
    }
    @Override
    public boolean aim(AlgorithmUpdate update) {
        if (FastMath.abs(distance) < barrelSpeed) {
            distance = GameUtils.random(-FastMath.TWO_PI, FastMath.TWO_PI);
        }
        float angle = barrelSpeed*FastMath.sign(distance);
        update.getTank().rotateAim(angle);
        distance -= angle;
        var id = update.getCollisionState().raycast(update.getTank().getAimRay(), update.getTank().getEntity().getId(), update.getTank().getEntity().get(Bounces.class).getRemaining());
        if (id == update.getPlayerTank().getEntity().getId()) {
            update.getTank().shoot(update.getManager().getEntityData());
        }
        return true;
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
