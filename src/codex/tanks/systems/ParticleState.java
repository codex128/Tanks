/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.ColorScheme;
import codex.tanks.components.EmitterComponent;
import codex.tanks.components.ParticleMode;
import codex.tanks.util.ESAppState;
import com.epagagames.particles.Emitter;
import com.epagagames.particles.influencers.ColorInfluencer;
import com.jme3.app.Application;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.HashMap;

/**
 *
 * @author codex
 */
public class ParticleState extends ESAppState {
    
    private EntitySet entities;
    private EntitySet sunset;
    private final HashMap<EntityId, Emitter> emitters = new HashMap<>();
    private VisualState visuals;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(EmitterComponent.class, ParticleMode.class, ColorScheme.class);
        visuals = getState(VisualState.class, true);
    }
    @Override
    protected void cleanup(Application app) {
        entities.release();
        emitters.clear();
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (entities.applyChanges()) {
            entities.getAddedEntities().forEach(e -> create(e));
            entities.getChangedEntities().forEach(e -> initSunset(e));
            entities.getRemovedEntities().forEach(e -> unlink(e.getId()));
        }
        for (var e : entities) {
            if (e.get(ParticleMode.class).getMode().equals(ParticleMode.SUNSET.getMode())) {
                var emitter = get(e.getId());
                if (emitter.getActiveParticleCount() == 0) {
                    //ed.removeEntity(e.getId());
                }
            }
        }
    }
    
    private void create(Entity e) {
        var emitter = factory.getSpatialFactory().createEmitter(e.get(EmitterComponent.class).getModel());
        var scheme = e.get(ColorScheme.class);
        scheme.verifySize(2);
        var color = new ColorInfluencer();
        color.setStartEndColor(scheme.getPallete()[0], scheme.getPallete()[1]);
        emitter.addInfluencer(color);
        //emitter.emitAllParticles();
        rootNode.attachChild(emitter);
        link(e.getId(), emitter);
        visuals.link(e.getId(), emitter);
    }
    private void initSunset(Entity e) {
        var emitter = get(e.getId());
        emitter.setEmissionsPerSecond(0);
        emitter.setParticlesPerEmission(0);
    }
    
    public boolean link(EntityId id, Emitter emitter) {
        return emitters.putIfAbsent(id, emitter) == null;
    }
    public Emitter unlink(EntityId id) {
        return emitters.remove(id);
    }
    public Emitter get(EntityId id) {
        return emitters.get(id);
    }
    
}
