/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.ai.AIModel;
import codex.tanks.ai.AlgorithmUpdate;
import codex.tanks.components.Brain;
import codex.tanks.factory.AIFactory;
import codex.tanks.util.ESAppState;
import com.jme3.app.Application;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.HashMap;

/**
 *
 * @author codex
 */
public class AIManager extends ESAppState {
    
    private EntitySet entities;
    private HashMap<EntityId, AIModel> models = new HashMap<>();
    private TankState tanks;
    private AIFactory factory;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(Brain.class);
        tanks = getState(TankState.class, true);
        factory = new AIFactory();
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
            entities.getAddedEntities().forEach(e -> createModel(e));
            entities.getRemovedEntities().forEach(e -> destroyModel(e));
        }
        for (var e : entities) {
            update(e, tpf);
        }
    }
    
    private void createModel(Entity e) {
        models.put(e.getId(), factory.create(e.get(Brain.class).getModel()));
    }
    private void destroyModel(Entity e) {
        models.remove(e.getId());
    }
    private void update(Entity e, float tpf) {
        boolean move = true,
                aim = true,
                shoot = true,
                mine = true;
        var update = new AlgorithmUpdate(this, tanks.getTank(e.getId()), tpf);
        if (update.getTank() == null || update.getPlayerTank() == null) {
            return;
        }
        for (var alg : models.get(e.getId()).getCommandStack()) {
            alg.initialize(update);
            if (move)  move  = !alg.move  (update);
            if (aim)   aim   = !alg.aim   (update);
            if (shoot) shoot = !alg.shoot (update);
            if (mine)  mine  = !alg.mine  (update);
            alg.cleanup(update);
        }
    }
    
}
