/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.CollisionShape;
import codex.tanks.util.GameUtils;

/**
 *
 * @author gary
 */
public class DirectShot implements TankAlgorithm {

    private final J3map source;
    private float exposureThreshold;
    private float exposure = 0f;
    
    public DirectShot(J3map source) {
        this.source = source;
        fetchComponents();
    }
    
    private void fetchComponents() {
        exposureThreshold = source.getFloat("exposureThreshold", .5f);
    }
    
    @Override
    public void updateTank(AlgorithmUpdate update) {}
    @Override
    public void moveTank(AlgorithmUpdate update) {}
    @Override
    public void aimTank(AlgorithmUpdate update) {
        if (update.isConsumed()) return;
        update.getTank().aimAt(update.getGame().getPlayerState().getTank().getPosition());
        CollisionShape shape = GameUtils.target(update.getGame().getCollisionShapes(), update.getTank().getAimRay(), update.getTank(), update.getTank().getModel().getMaxBounces());
        if (shape == update.getGame().getPlayerState().getTank()) {
            if ((exposure += update.getTpf()) > exposureThreshold) {
                update.getGame().addMissile(update.getTank().shoot());
                exposure = exposureThreshold;
            }
        }
        else {
            exposure = Math.max(exposure-update.getTpf(), 0f);
        }
        update.consume();
    }
    @Override
    public void mineTank(AlgorithmUpdate update) {}
    
}
