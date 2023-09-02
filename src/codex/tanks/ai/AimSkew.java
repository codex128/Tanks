/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.components.AimDirection;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author codex
 */
public class AimSkew implements Algorithm {
    
    private float maxBarrelSkew = .3f;
    private float speed = 5f;
    
    private float skew = 0f;
    
    public AimSkew() {}
    public AimSkew(J3map source) {
        maxBarrelSkew = source.getFloat("maxBarrelSkew", maxBarrelSkew);
        speed = source.getFloat("speed", speed);
    }
    
    @Override
    public void update(AlgorithmUpdate update) {
        var forward = update.getComponent(AimDirection.class).getAim();
        var q = new Quaternion().fromAngleAxis(FastMath.sin(skew)*maxBarrelSkew, Vector3f.UNIT_Y);
        //update.getTank().aimAtDirection(q.mult(forward));
        update.setComponent(new AimDirection(q.mult(forward)));
        skew += update.getTpf()*speed;
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
