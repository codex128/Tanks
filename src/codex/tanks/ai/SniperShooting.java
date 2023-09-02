/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.collision.OriginFilter;
import codex.tanks.collision.LaserRaytest;
import codex.tanks.collision.ShapeFilter;
import codex.tanks.components.Bounces;

/**
 *
 * @author codex
 */
public class SniperShooting implements Algorithm {

    public SniperShooting() {}
    public SniperShooting(J3map source) {}
    
    @Override
    public void update(AlgorithmUpdate update) {}
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
        if (update.isTargetInBounce()) {
            var filter = new OriginFilter(update.getAgentId(), ShapeFilter.or(ShapeFilter.byId(update.getAgentId()), ShapeFilter.none(ShapeFilter.byGameObject("tank"))));
            var test = new LaserRaytest(update.getAimRay(), filter, update.getComponent(Bounces.class).getRemaining());
            test.cast(update.getCollisionState());
            if (update.getAgentId().equals(test.getCollisionEntity())) {
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
