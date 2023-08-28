/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.components.Bounces;
import codex.tanks.components.Team;

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
        if (update.calculatePlayerInView()) {
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
                var id = update.getCollisionState().raycast(update.getTank().getAimRay(), update.getTankId(), update.getTank().getEntity().get(Bounces.class).getRemaining());
                if (id != null) {
                    var team = update.getEntityData().getComponent(id, Team.class);
                    if (team != null && team.getTeam() == update.getTank().getEntity().get(Team.class).getTeam()) {
                        return false;
                    }
                }
                if (update.getDirectionToPlayer().dot(update.getTank().getAimDirection()) >= 1f-maxBarrelOffset) {
                    update.getTank().shoot(update.getEntityData());
                    return true;
                }
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
