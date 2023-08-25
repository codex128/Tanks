/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

/**
 *
 * @author gary
 */
public class DirectShot implements Algorithm {
    
    private final float minExposure;
    private final float maxBarrelOffset;
    private float exposure = 0f;
    
    public DirectShot(float minExposure, float maxBarrelOffset) {
        this.minExposure = minExposure;
        this.maxBarrelOffset = maxBarrelOffset;
    }
    
    @Override
    public void initialize(AlgorithmUpdate update) {
        if (update.isPlayerInView()) {
            exposure = Math.min(exposure+update.getTpf(), minExposure);
        }
        else {
            exposure = Math.max(exposure-update.getTpf(), 0f);
        }
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
            if (exposure >= minExposure-0.01f) {
                if (update.getDirectionToPlayer().dot(update.getTank().getAimDirection()) <= maxBarrelOffset) {
                    update.getTank().shoot(update.getManager().getEntityData());
                    return true;
                }
                exposure = minExposure;
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
