/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.tanks.components.Bounces;

/**
 *
 * @author codex
 */
public class BasicShooting implements Algorithm {

    @Override
    public void initialize(AlgorithmUpdate update) {}
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
        var id = update.getCollisionState().raycast(update.getTank().getAimRay(),
                update.getTank().getEntity().getId(), update.getTank().getEntity().get(Bounces.class).getRemaining());
        if (id.equals(update.getPlayerTank().getEntity().getId())) {
            
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
