/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.tanks.components.Bounces;

/**
 *
 * @author gary
 */
public class DirectShot implements Algorithm {

    private final float exposureThreshold;
    private float exposure = 0f;
    
    public DirectShot(float exposureThreshold) {
        this.exposureThreshold = exposureThreshold;
    }
    
    @Override
    public void initialize(AlgorithmUpdate update) {}
    @Override
    public boolean move(AlgorithmUpdate update) {
        return false;
    }
    @Override
    public boolean aim(AlgorithmUpdate update) {
        update.getTank().aimAt(update.getPlayerTank().getPosition());
        return true;
    }
    @Override
    public boolean shoot(AlgorithmUpdate update) {
        var id = update.getCollisionState().raycast(update.getTank().getAimRay(), update.getTank().getEntity().getId(), update.getTank().getEntity().get(Bounces.class).getRemaining());
        if (id == update.getPlayerTank().getEntity().getId()) {
            if ((exposure += update.getTpf()) > exposureThreshold) {
                update.getTank().shoot(update.getManager().getEntityData());
                exposure = exposureThreshold;
            }
        }
        else {
            exposure = Math.max(exposure-update.getTpf(), 0f);
        }
        return true;
    }
    @Override
    public boolean mine(AlgorithmUpdate update) {
        return false;
    }
    @Override
    public void cleanup(AlgorithmUpdate update) {}
    
}
