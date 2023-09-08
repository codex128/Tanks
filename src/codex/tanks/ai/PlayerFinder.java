/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.components.EntityTransform;
import codex.tanks.components.Team;
import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class PlayerFinder implements Algorithm {

    private EntitySet players;
    
    public PlayerFinder() {}
    public PlayerFinder(J3map source) {}
    
    @Override
    public void initialize(Application app) {
        players = getEntityData(app).getEntities(Team.class, EntityTransform.class);
    }
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
        return false;
    }
    @Override
    public boolean mine(AlgorithmUpdate update) {
        return false;
    }
    @Override
    public void endUpdate(AlgorithmUpdate update) {}
    @Override
    public EntityId fetchTargetId(AlgorithmUpdate update) {
        players.applyChanges();
        Vector3f here = update.getComponent(EntityTransform.class).getTranslation();
        Vector3f there = new Vector3f();
        float minDist = -1;
        Entity player = null;
        for (var e : players) {
            if (e.get(Team.class).getTeam() != update.getComponent(Team.class).getTeam()) {
                there.set(e.get(EntityTransform.class).getTranslation());
                var d = here.distanceSquared(there);
                if (player == null || d < minDist) {
                    minDist = d;
                    player = e;
                }
            }
        }
        if (player == null) return null;
        return player.getId();
    }
    
}
