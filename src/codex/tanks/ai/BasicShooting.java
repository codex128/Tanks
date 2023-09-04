/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.collision.PaddedLaserRaytest;
import codex.tanks.components.Bounces;
import codex.tanks.components.Team;
import codex.tanks.collision.ShapeFilter;
import codex.tanks.components.AimDirection;

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
    public BasicShooting(J3map source) {
        minExposure = source.getFloat("minExposure", minExposure);
        maxBarrelOffset = source.getFloat("maxBarrelOffset", maxBarrelOffset);
        bounceFactor = source.getFloat("bounceFactor", bounceFactor);
    }
    
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
        if (update.isTargetInView()) {
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
        if (update.isTargetInView() && exposure > minExposure-0.01f
                && update.getDirectionToTarget().dot(update.getMuzzleDirection()) >= 1f-maxBarrelOffset) {
            var raytest = new PaddedLaserRaytest(
                    update.getAimRay(), update.getAgentId(), 1f,
                    ShapeFilter.and(ShapeFilter.byTeam(update.getComponent(Team.class).getTeam()), ShapeFilter.notId(update.getAgentId())),
                    update.getComponent(Bounces.class).getRemaining());
            raytest.cast(update.getCollisionState());
            if (raytest.isImpeded()) {
                return false;
            }
            //update.getTank().shoot(update.getEntityData(), update.getVisualState());
            update.shoot();
            return true;
        }
        return false;
    }
    @Override
    public boolean mine(AlgorithmUpdate update) {
        return false;
    }
    @Override
    public void endUpdate(AlgorithmUpdate update) {}
    
}
