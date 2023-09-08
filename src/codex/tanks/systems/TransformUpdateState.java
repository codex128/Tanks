/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.Copy;
import codex.tanks.components.EntityTransform;
import codex.tanks.components.Rotate;
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
    private EntitySet rotate;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        spatialUpdate = ed.getEntities(
                new FunctionFilter<>(TransformMode.class, c -> c.anyMatch(m -> !TransformMode.isPhysics(m))),
                Visual.class, EntityTransform.class, TransformMode.class);
        entityCopy = ed.getEntities(
                Copy.filter(Copy.TRANSFORM),
                EntityTransform.class, TransformMode.class, Copy.class);
        rotate = ed.getEntities(EntityTransform.class, Rotate.class);
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
        rotate.applyChanges();
        for (var e : rotate) {
            updateRotation(e);
        }
        spatialUpdate.applyChanges();
        for (var e : spatialUpdate) {
            updateSpatial(e);
        }
        entityCopy.applyChanges();
        for (var e : entityCopy) {
            updateCopy(e);
        }
        if (spatialUpdate.applyChanges()) for (var e : spatialUpdate) {
            updateSpatial(e);
        }
    }
    
    private void updateSpatial(Entity e) {
        var spatial = visuals.getSpatial(e.getId());
        var transform = e.get(EntityTransform.class);
        var enable = e.get(TransformMode.class);
        var newTrans = transform.toJmeTransform();
        boolean change = false;
        if (!TransformMode.isPhysics(enable.getTranslation())) switch (enable.getTranslation()) {
            case TransformMode.LOCAL_TO_ENTITY -> {
                newTrans.setTranslation(spatial.getLocalTranslation());
                change = true;
            }
            case TransformMode.WORLD_TO_ENTITY -> {
                newTrans.setTranslation(spatial.getWorldTranslation());
                change = true;
            }
            default ->
                spatial.setLocalTranslation(transform.getTranslation());
        }
        if (!TransformMode.isPhysics(enable.getRotation())) switch (enable.getRotation()) {
            case TransformMode.LOCAL_TO_ENTITY -> {
                newTrans.setRotation(spatial.getLocalRotation());
                change = true;
            }
            case TransformMode.WORLD_TO_ENTITY -> {
                newTrans.setRotation(spatial.getWorldRotation());
                change = true;
            }
            default ->
                spatial.setLocalRotation(transform.getRotation());
        }
        if (!TransformMode.isPhysics(enable.getScale())) switch (enable.getScale()) {
            case TransformMode.LOCAL_TO_ENTITY -> {
                newTrans.setScale(spatial.getLocalScale());
                change = true;
            }
            case TransformMode.WORLD_TO_ENTITY -> {
                newTrans.setScale(spatial.getWorldScale());
                change = true;
            }
            default ->
                spatial.setLocalScale(transform.getScale());
        }
        if (change) {
            e.set(new EntityTransform(newTrans));
        }
    }
    private void updateCopy(Entity e) {
        var component = e.get(Copy.class);
        var copy = ed.getComponent(component.getCopy(), EntityTransform.class);
        if (copy == null) return;
        var transform = e.get(EntityTransform.class);
        var enable = e.get(TransformMode.class);
        // so. messy.
        if (!TransformMode.isNone(enable.getTranslation())
                || !TransformMode.isNone(enable.getRotation())
                || !TransformMode.isNone(enable.getScale())) {
            if (!TransformMode.isNone(enable.getTranslation())) transform.setTranslation(copy.getTranslation());
            if (!TransformMode.isNone(enable.getRotation())) transform.setRotation(copy.getRotation());
            if (!TransformMode.isNone(enable.getScale())) transform.setScale(copy.getScale());
            e.set(new EntityTransform(transform));
        }
    }
    private void updateRotation(Entity e) {
        var t = e.get(EntityTransform.class);
        e.set(new EntityTransform(t).setRotation(t.getRotation().mult(e.get(Rotate.class).getRotation())));
    }
    
}
