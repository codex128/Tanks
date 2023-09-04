/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.ai.AlgorithmUpdate;
import codex.tanks.components.AimDirection;
import codex.tanks.components.Bounces;
import codex.tanks.components.Brain;
import codex.tanks.components.EntityTransform;
import codex.tanks.components.Forward;
import codex.tanks.components.MaxSpeed;
import codex.tanks.components.MuzzlePointer;
import codex.tanks.components.ProbeLocation;
import codex.tanks.components.Team;
import codex.tanks.util.ESAppState;
import com.jme3.app.Application;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class AIManager extends ESAppState {
    
    private EntitySet entities;
    //private TankState tanks;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(
                Brain.class, EntityTransform.class, AimDirection.class, MuzzlePointer.class,
                Forward.class, ProbeLocation.class, Bounces.class, MaxSpeed.class, Team.class);
        //tanks = getState(TankState.class, true);
    }
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (entities.applyChanges()) {
            for (var e : entities.getAddedEntities()) {
                for (var a : e.get(Brain.class).getAlgorithms()) {
                    a.initialize(app);
                }
            }
        }
        for (var e : entities) {
            update(e, tpf);
        }
    }
    
    private void update(Entity e, float tpf) {
        boolean move = true,
                aim = true,
                shoot = true,
                mine = true;
        var update = new AlgorithmUpdate(this, e, tpf);
        if (!update.isInfoSatisfied()) return;
        // fetch the target entity
        for (var alg : e.get(Brain.class).getAlgorithms()) {
            var target = alg.fetchTargetId(update);
            if (target != null) {
                update.setTarget(target);
                break;
            }
        }
        if (update.getTarget() == null) return;
        // update the AI
        for (var alg : e.get(Brain.class).getAlgorithms()) {
            alg.update(update);
            if (move)  move  = !alg.move(update);
            if (aim)   aim   = !alg.aim(update);
            if (shoot) shoot = !alg.shoot(update);
            if (mine)  mine  = !alg.mine(update);
            alg.endUpdate(update);
        }
    }
    
}
