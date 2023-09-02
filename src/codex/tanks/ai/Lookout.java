/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.components.AimDirection;
import codex.tanks.util.GameUtils;
import com.jme3.math.FastMath;

/**
 *
 * @author gary
 */
public class Lookout implements Algorithm {
    
    private float speed = 0.01f;
    private float distance = 0f;
    
    public Lookout() {
        speed *= FastMath.PI;
    }
    public Lookout(J3map source) {
        speed = source.getFloat("speed", speed)*FastMath.PI;
    }
    
    @Override
    public void update(AlgorithmUpdate update) {}
    @Override
    public boolean move(AlgorithmUpdate update) {
        return false;
    }
    @Override
    public boolean aim(AlgorithmUpdate update) {
        if (FastMath.abs(distance) < speed) {
            distance = GameUtils.random(-FastMath.TWO_PI, FastMath.TWO_PI);
        }
        float angle = speed*FastMath.sign(distance);
        update.setComponent(new AimDirection(update.rotateAim(angle)));
        distance -= angle;
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
    public void endUpdate(AlgorithmUpdate update) {}
    
}
