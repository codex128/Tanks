/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.ai.AlgorithmUpdate;
import codex.tanks.components.Brain;
import codex.tanks.components.GameObject;
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
    private TankState tanks;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(
                GameObject.filter("tank"),
                GameObject.class, Brain.class);
        tanks = getState(TankState.class, true);
    }
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        entities.applyChanges();
        for (var e : entities) {
            update(e, tpf);
        }
    }
    
    private void update(Entity e, float tpf) {
        boolean move = true,
                aim = true,
                shoot = true,
                mine = true;
        var update = new AlgorithmUpdate(this, tanks.getTank(e.getId()), tpf);
        if (!update.isInfoSatisfied()) {
            return;
        }
        for (var alg : e.get(Brain.class).getAlgorithms()) {
            alg.initialize(update);
            if (move)  move  = !alg.move(update);
            if (aim)   aim   = !alg.aim(update);
            if (shoot) shoot = !alg.shoot(update);
            if (mine)  mine  = !alg.mine(update);
            alg.cleanup(update);
        }
    }
    
}
