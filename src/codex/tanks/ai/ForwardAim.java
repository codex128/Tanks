/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

/**
 *
 * @author codex
 */
public class ForwardAim implements Algorithm {

    private float distance = 15f;
    
    @Override
    public void update(AlgorithmUpdate update) {}
    @Override
    public boolean move(AlgorithmUpdate update) {
        return false;
    }
    @Override
    public boolean aim(AlgorithmUpdate update) {
        if (update.getDistanceToPlayer() > distance && !update.isPlayerInView()) {
            update.getTank().aimAtDirection(update.getTank().getForwardDirection());
            return true;
        }
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
