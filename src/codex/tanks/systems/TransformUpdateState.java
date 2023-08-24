/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.EntityTransform;
import codex.tanks.components.SpatialTransform;
import codex.tanks.util.ESAppState;
import com.jme3.app.Application;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class TransformUpdateState extends ESAppState {
    
    private EntitySet entities;
    private VisualState visuals;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(Visual.class, EntityTransform.class, SpatialTransform.class);
        visuals = getState(VisualState.class, true);
    }
    @Override
    protected void cleanup(Application app) {
        entities.release();
        visuals = null;
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (entities.applyChanges()) {
            entities.getChangedEntities().forEach(e -> update(e));
        }
    }
    
    private void update(Entity e) {
        var spatial = visuals.getSpatial(e.getId());
        var transform = e.get(EntityTransform.class);
        var enable = e.get(SpatialTransform.class);
        if (enable.translation) spatial.setLocalTranslation(transform.getLocation());
        if (enable.rotation)    spatial.setLocalRotation(transform.getRotation());
        if (enable.scale)       spatial.setLocalScale(transform.getScale());
    }
    
}
