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
    
    private final float minExposure;
    private final float maxBarrelOffset;
    private final float bounceFactor;
    private float exposure = 0f;
    private float makeBounce = 0f;
    
    public BasicShooting(float minExposure, float maxBarrelOffset) {
        this(minExposure, maxBarrelOffset, 1f);
    }
    public BasicShooting(float minExposure, float maxBarrelOffset, float bounceFactor) {
        this.minExposure = minExposure;
        this.maxBarrelOffset = maxBarrelOffset;
        this.bounceFactor = bounceFactor;
    }
    
    @Override
    public void initialize(AlgorithmUpdate update) {
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
                if (update.getDirectionToPlayer().dot(update.getTank().getAimDirection()) <= maxBarrelOffset) {
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
