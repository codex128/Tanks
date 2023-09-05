/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.collision.SegmentedRaytest;
import codex.tanks.collision.ShapeFilter;
import codex.tanks.components.EntityTransform;
import codex.tanks.components.Team;
import com.jme3.math.Ray;

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
        var e = update.createRaycastEntity(update.getAimRay());
        SegmentedRaytest raytest = new SegmentedRaytest(update.getCollisionState());
        raytest.setRay(update.getAimRay());
        //raytest.setMarginFilter(margin);
        raytest.setDistance(-1f);
        //raytest.setMargin(AlgorithmUpdate.SAFETY_MARGIN);
        //raytest.setFirstCastFilter(ShapeFilter.notId(update.getAgentId()));
        raytest.setOriginEntity(update.getAgentId());
        var iterator = update.getProjectileState().raytest(e, raytest);
        update.getEntityData().removeEntity(e.getId());
        if (iterator.getCollisionEntity() != null) {
            if (iterator.getCollisionEntity().equals(update.getAgentId())) {
                return false;
            }
            var team = update.getEntityData().getComponent(iterator.getCollisionEntity(), Team.class);
            if (team == null || team.getTeam() == update.getComponent(Team.class).getTeam()) {
                return false;
            }
        }
        else {
            return false;
        }
        update.shoot();
        return true;
    }
    @Override
    public boolean mine(AlgorithmUpdate update) {
        return false;
    }
    @Override
    public void endUpdate(AlgorithmUpdate update) {}
    
}
