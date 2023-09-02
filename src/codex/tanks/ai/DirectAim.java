/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.components.AimDirection;
import codex.tanks.components.EntityTransform;

/**
 *
 * @author codex
 */
public class DirectAim implements Algorithm {
    
    public DirectAim() {}
    public DirectAim(J3map source) {}
    
    @Override
    public void update(AlgorithmUpdate update) {}
    @Override
    public boolean move(AlgorithmUpdate update) {
        return false;
    }
    @Override
    public boolean aim(AlgorithmUpdate update) {
        update.setComponent(new AimDirection(update.aimAt(
                update.getTargetComponent(EntityTransform.class).getTranslation().clone())));
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
