/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.Alive;
import codex.tanks.components.GameObject;
import codex.tanks.util.ESAppState;
import com.epagagames.particles.Emitter;
import com.jme3.app.Application;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Consumer;

/**
 *
 * @author codex
 */
public class ParticleState extends ESAppState {
    
    private EntitySet entities;
    private final HashMap<EntityId, Emitter> emitters = new HashMap<>();
    private final LinkedList<Emitter> dying = new LinkedList<>();
    private VisualState visuals;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(
                GameObject.filter("particle-emitter"),
                GameObject.class, Alive.class);
        visuals = getState(VisualState.class, true);
    }
    @Override
    protected void cleanup(Application app) {
        entities.release();
        Consumer<Emitter> kill = e -> e.removeFromParent();
        emitters.values().forEach(kill);
        dying.forEach(kill);
        emitters.clear();
        dying.clear();
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (entities.applyChanges()) {
            entities.getAddedEntities().forEach(e -> linkVisual(e));
            entities.getChangedEntities().forEach(e -> update(e, tpf));
            entities.getRemovedEntities().forEach(e -> unlink(e.getId()));
        }
        for (var i = dying.iterator(); i.hasNext();) {
            Emitter emitter = i.next();
            if (emitter.getActiveParticleCount() == 0) {
                emitter.removeFromParent();
                i.remove();
            }
        }
    }
    
    private void linkVisual(Entity e) {
        var spatial = visuals.getSpatial(e.getId());
        if (spatial instanceof Emitter) {
            link(e.getId(), (Emitter)spatial);
        }
    }
    private void update(Entity e, float tpf) {
        if (!e.get(Alive.class).isAlive()) {
            unlink(e.getId());
        }
    }
    
    public boolean link(EntityId id, Emitter emitter) {
        return emitters.putIfAbsent(id, emitter) == null;
    }
    public Emitter unlink(EntityId id) {
        var e = emitters.remove(id);
        if (e != null) {
            e.setEmissionsPerSecond(0);
            dying.add(e);
        }
        return e;
    }
    
}
