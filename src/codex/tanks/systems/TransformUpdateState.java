/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.Copy;
import codex.tanks.components.EntityTransform;
import codex.tanks.components.TransformMode;
import codex.tanks.components.Visual;
import codex.tanks.util.ESAppState;
import codex.tanks.util.FunctionFilter;
import com.jme3.app.Application;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class TransformUpdateState extends ESAppState {
    
    private EntitySet spatialUpdate;
    private EntitySet entityCopy;
    private VisualState visuals;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        spatialUpdate = ed.getEntities(Visual.class, EntityTransform.class, TransformMode.class);
        entityCopy = ed.getEntities(
                new FunctionFilter<>(Copy.class, c -> c.supports(Copy.TRANSFORM)),
                EntityTransform.class, TransformMode.class, Copy.class);
        visuals = getState(VisualState.class, true);
    }
    @Override
    protected void cleanup(Application app) {
        spatialUpdate.release();
        entityCopy.release();
        visuals = null;
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        spatialUpdate.applyChanges();
        entityCopy.applyChanges();
        for (var e : spatialUpdate) {
            updateSpatial(e);
        }
        for (var e : entityCopy) {
            updateCopy(e);
        }
    }
    
    private void updateSpatial(Entity e) {
        var spatial = visuals.getSpatial(e.getId());
        var transform = e.get(EntityTransform.class);
        var enable = e.get(TransformMode.class);
        boolean change = false;
        switch (enable.getTranslationState()) {
            case TransformMode.LOCAL_TO_ENTITY -> {
                transform.setTranslation(spatial.getLocalTranslation());
                change = true;
            }
            case TransformMode.WORLD_TO_ENTITY -> {
                transform.setTranslation(spatial.getWorldTranslation());
                change = true;
            }
            default ->
                spatial.setLocalTranslation(transform.getTranslation());
        }
        switch (enable.getRotationState()) {
            case TransformMode.LOCAL_TO_ENTITY -> {
                transform.setRotation(spatial.getLocalRotation());
                change = true;
            }
            case TransformMode.WORLD_TO_ENTITY -> {
                transform.setRotation(spatial.getWorldRotation());
                change = true;
            }
            default ->
                spatial.setLocalRotation(transform.getRotation());
        }
        switch (enable.getScaleState()) {
            case TransformMode.LOCAL_TO_ENTITY -> {
                transform.setScale(spatial.getLocalScale());
                change = true;
            }
            case TransformMode.WORLD_TO_ENTITY -> {
                transform.setScale(spatial.getWorldScale());
                change = true;
            }
            default ->
                spatial.setLocalScale(transform.getScale());
        }
        if (change) {
            e.set(new EntityTransform(transform));
        }
    }
    private void updateCopy(Entity e) {
        var copy = ed.getComponent(e.get(Copy.class).getCopy(), EntityTransform.class);
        var transform = e.get(EntityTransform.class);
        var enable = e.get(TransformMode.class);
        if (enable.useTranslation() || enable.useRotation() || enable.useScale()) {
            if (enable.useTranslation()) transform.setTranslation(copy.getTranslation());
            if (enable.useRotation()) transform.setRotation(copy.getRotation());
            if (enable.useScale()) transform.setScale(copy.getScale());
            e.set(new EntityTransform(transform));
        }
    }
    
}
