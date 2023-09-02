/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.physics;

import codex.tanks.components.EntityTransform;
import codex.tanks.components.LinearVelocity;
import codex.tanks.components.RigidBody;
import codex.tanks.components.TransformMode;
import codex.tanks.util.ESAppState;
import codex.tanks.util.FunctionFilter;
import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.control.Control;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.HashMap;
import java.util.function.BiConsumer;

/**
 *
 * @author codex
 */
public class PhysicsState extends ESAppState implements PhysicsTickListener {
    
    private EntitySet basicSet;
    private EntitySet transform, linearVelocity;
    private final HashMap<EntityId, RigidBodyControl> physics = new HashMap<>();
    private BulletAppState bulletapp;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        basicSet = ed.getEntities(RigidBody.class);
        transform = ed.getEntities(
                new FunctionFilter<>(TransformMode.class, c -> c.anyMatch(m -> TransformMode.isPhysics(m))),
                RigidBody.class, EntityTransform.class, TransformMode.class);
        linearVelocity = ed.getEntities(RigidBody.class, LinearVelocity.class);
        bulletapp = getState(BulletAppState.class, true);
        //getPhysicsSpace().addTickListener(this);
        getPhysicsSpace().setGravity(new Vector3f(0f, -100f, 0f));
    }
    @Override
    protected void cleanup(Application app) {
//        if (getPhysicsSpace() != null) {
//            getPhysicsSpace().removeTickListener(this);
//        }
        basicSet.release();
        transform.release();
        linearVelocity.release();
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (basicSet.applyChanges()) {
            basicSet.getAddedEntities().forEach(e -> create(e));
            basicSet.getRemovedEntities().forEach(e -> destroy(e));
        }
        updateEntityToBody();
        updateBodyToEntity();
    }
    @Override
    public void prePhysicsTick(PhysicsSpace space, float timeStep) {
        // I might need to do something here later
    }
    @Override
    public void physicsTick(PhysicsSpace space, float timeStep) {
        // I might need to do something here later
    }
    
    private void create(Entity e) {
        float mass = e.get(RigidBody.class).getMass();
        if (mass >= 0f) {
            var spatial = visuals.getSpatial(e.getId());
            if (spatial != null) {
                var rigidbody = new RigidBodyControl(mass);
                spatial.addControl(rigidbody);
                link(e.getId(), rigidbody);
            }
        }
    }
    private void destroy(Entity e) {
        var object = unlink(e.getId());
        if (object instanceof Control) {
            var spatial = visuals.getSpatial(e.getId());
            if (spatial != null) {
                spatial.removeControl((Control)object);
            }
        }
    }
    
    public boolean link(EntityId id, RigidBodyControl object) {
        if (ed.getComponent(id, RigidBody.class) == null) {
            return false;
        }
        if (physics.putIfAbsent(id, object) == null) {
            getPhysicsSpace().add(object);
            return true;
        }
        return false;
    }
    public RigidBodyControl unlink(EntityId id) {
        var object = physics.remove(id);
        if (object != null) {
            getPhysicsSpace().remove(object);
        }
        return object;
    }    
    public Object get(EntityId id) {
        return physics.get(id);
    }
    public <T> T get(Class<T> type, EntityId id) {
        var object = physics.get(id);
        if (object != null && type.isAssignableFrom(object.getClass())) {
            return (T)object;
        }
        return null;
    }
    
    private void updateEntityToBody() {
        if (transform.applyChanges()) for (var entity : transform.getChangedEntities()) {
            update(entity, (e, b) -> updateBodyTransform(e, b));
        }
        if (linearVelocity.applyChanges()) for (var entity : linearVelocity.getChangedEntities()) {
            update(entity, (e, b) -> updateBodyLinearVelocity(e, b));
        }
    }
    private void updateBodyToEntity() {
        // If a changes are made to the entities between update patterns, then
        // we need to apply them now. Otherwise important changes could be erased.
        //updateEntityToBody();
        for (var entity : transform) {
            update(entity, (e, b) -> updateEntityTransform(e, b));
        }
        for (var entity : linearVelocity) {
            update(entity, (e, b) -> updateEntityLinearVelocity(e, b));
        }
        // Apply changes to the entity set now, so we don't detect the changes made above.
        transform.applyChanges();
        linearVelocity.applyChanges();
    }
    private void update(Entity e, BiConsumer<Entity, RigidBodyControl> update) {
        var object = physics.get(e.getId());
        if (object != null) update.accept(e, object);
    }
    private void updateBodyTransform(Entity e, RigidBodyControl object) {
        var t = e.get(EntityTransform.class);
        var enable = e.get(TransformMode.class);
        if (TransformMode.isPhysics(enable.getTranslation())) {
            object.setPhysicsLocation(t.getTranslation());
        }
        if (TransformMode.isPhysics(enable.getRotation())) {
            object.setPhysicsRotation(t.getRotation());
        }
    }
    private void updateEntityTransform(Entity e, RigidBodyControl object) {
        var t = e.get(EntityTransform.class).toJmeTransform();
        var enable = e.get(TransformMode.class);
        boolean changed = false;
        if (TransformMode.isPhysics(enable.getTranslation())) {
            t.setTranslation(object.getPhysicsLocation());
            changed = true;
        }
        if (TransformMode.isPhysics(enable.getRotation())) {
            t.setRotation(object.getPhysicsRotation());
            changed = true;
        }
        if (changed) {
            e.set(new EntityTransform(t));
        }
    }
    private void updateBodyLinearVelocity(Entity e, RigidBodyControl object) {
        object.setLinearVelocity(e.get(LinearVelocity.class).getVelocity());
        if (object.getLinearVelocity().lengthSquared() > 0) {
            object.activate();
        }
    }
    private void updateEntityLinearVelocity(Entity e, RigidBodyControl object) {
        e.set(new LinearVelocity(object.getLinearVelocity()));
    }
    
    public PhysicsSpace getPhysicsSpace() {
        if (bulletapp == null) return null;
        return bulletapp.getPhysicsSpace();
    }
    
}
