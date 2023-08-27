/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import com.jme3.math.FastMath;

/**
 *
 * @author gary
 */
public class BasicShooting implements Algorithm {
    
    private float minExposure = 0.1f;
    private float maxBarrelOffset = 0.3f;
    private float bounceFactor = 1f;
    
    private float exposure = 0f;
    private float makeBounce = 0f;
    
    public BasicShooting() {}
    
    public BasicShooting setMinExposure(float minExposure) {
        this.minExposure = minExposure;
        return this;
    }
    public BasicShooting setMaxBarrelOffset(float maxBarrelOffset) {
        this.maxBarrelOffset = maxBarrelOffset;
        return this;
    }
    public BasicShooting setBounceFactor(float bounceFactor) {
        this.bounceFactor = bounceFactor;
        return this;
    }
    
    @Override
    public void update(AlgorithmUpdate update) {
        if (update.isPlayerInView()) {
            exposure = Math.min(exposure+update.getTpf(), minExposure);
        }
        else {
            exposure = Math.max(exposure-update.getTpf(), 0f);
        }
        makeBounce = Math.max(makeBounce-update.getTpf(), 0f);
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
        if (update.isPlayerInView()) {
            if (exposure > minExposure-0.01f) {
                if (update.getDirectionToPlayer().dot(update.getTank().getAimDirection()) >= 1f-maxBarrelOffset) {
                    update.getTank().shoot(update.getEntityData());
                    return true;
                }
                exposure = minExposure;
            }
        }
        if (makeBounce < 0.01f && update.isPlayerInBounce()) {
            makeBounce = 1f;
            if (FastMath.nextRandomFloat() < bounceFactor) {
                update.getTank().shoot(update.getEntityData());
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean mine(AlgorithmUpdate update) {
        return false;
    }
    @Override
    public void cleanup(AlgorithmUpdate update) {}
    
}
