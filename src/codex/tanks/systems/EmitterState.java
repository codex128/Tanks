/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.Alive;
import codex.tanks.components.EmissionsPerSecond;
import codex.tanks.components.RemoveOnSleep;
import codex.tanks.components.Particles;
import codex.tanks.components.SingleEmission;
import codex.tanks.components.Visual;
import codex.tanks.es.ESAppState;
import com.epagagames.particles.Emitter;
import com.jme3.app.Application;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class EmitterState extends ESAppState {
    
    private EntitySet singleEmission;
    private EntitySet emissionsPerSec;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        singleEmission = ed.getEntities(Visual.class, Particles.class, SingleEmission.class);
        emissionsPerSec = ed.getEntities(Visual.class, Particles.class, EmissionsPerSecond.class);
    }
    @Override
    protected void cleanup(Application app) {
        singleEmission.release();
        emissionsPerSec.release();
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (singleEmission.applyChanges()) {
            singleEmission.getAddedEntities().forEach(e -> emitOnce(e));
            singleEmission.getChangedEntities().forEach(e -> emitOnce(e));
        }
        if (emissionsPerSec.applyChanges()) {
            emissionsPerSec.getAddedEntities().forEach(e -> setEmissionsPerSecond(e));
            emissionsPerSec.getChangedEntities().forEach(e -> setEmissionsPerSecond(e));
        }
    }
    
    private void emitOnce(Entity e) {
        var emitter = visuals.getSpatial(e.getId(), Emitter.class);
        emitter.setEmissionsPerSecond(0f);
        emitter.emitAllParticles();
        ed.removeComponent(e.getId(), SingleEmission.class);
    }
    private void setEmissionsPerSecond(Entity e) {
        var emitter = visuals.getSpatial(e.getId(), Emitter.class);
        emitter.setEmissionsPerSecond(e.get(EmissionsPerSecond.class).getEps());
    }
    
}
