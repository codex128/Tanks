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
public class Sniper implements Algorithm {

    public Sniper() {}
    public Sniper(J3map source) {}
    
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
        if (update.calculatePlayerInBounce()) {
            var filter = new OriginFilter(update.getTankId(), ShapeFilter.or(ShapeFilter.byId(update.getTankId()), ShapeFilter.none(ShapeFilter.byGameObject("tank"))));
            var test = new LaserRaytest(update.getTank().getAimRay(), filter, update.getTank().getEntity().get(Bounces.class).getRemaining());
            test.cast(update.getCollisionState());
            if (update.getTankId().equals(test.getCollisionEntity())) {
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
    public void cleanup(AlgorithmUpdate update) {}
    
}
