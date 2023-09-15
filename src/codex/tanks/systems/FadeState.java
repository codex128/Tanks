/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.PositionalFade;
import codex.tanks.effects.MatChange;
import codex.tanks.effects.MaterialChangeBucket;
import codex.tanks.es.ESAppState;
import com.jme3.app.Application;
import com.jme3.shader.VarType;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class FadeState extends ESAppState {
    
    private EntitySet entities;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(PositionalFade.class);
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
            entities.getAddedEntities().forEach(e -> setup(e));
            entities.getChangedEntities().forEach(e -> setup(e));
        }
        for (var e : entities) {
            var fade = e.get(PositionalFade.class);
            MaterialChangeBucket.addChanges(ed, e.getId(), new MatChange("FadeStart", VarType.Float, fade.getStart()));
            if (fade.isComplete()) {
                ed.removeComponent(e.getId(), PositionalFade.class);
            }
            else {
                e.set(fade.increment(tpf));
            }
        }
    }
    
    private void setup(Entity e) {
        var fade = e.get(PositionalFade.class);
        MaterialChangeBucket.addChanges(ed, e.getId(),
            new MatChange("FadeOrigin", VarType.Vector3, fade.getOrigin()),
            new MatChange("FadeAxis", VarType.Vector3, fade.getAxis()),
            new MatChange("FadeLength", VarType.Float, fade.getLength())
        );
    }
    
}
