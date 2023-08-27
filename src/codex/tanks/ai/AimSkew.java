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
 * @author codex
 */
public class AimSkew implements Algorithm {
    
    private float maxBarrelWander = .3f;
    private float skew = 0f;
    
    @Override
    public void update(AlgorithmUpdate update) {
        var forward = update.getTank().getAimDirection();
        var q = new Quaternion().fromAngleAxis(FastMath.sin(skew)*maxBarrelWander, Vector3f.UNIT_Y);
        update.getTank().aimAtDirection(q.mult(forward));
        skew += update.getTpf() * 5f;
    }
    @Override
    public boolean move(AlgorithmUpdate update) {
        return false;
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
