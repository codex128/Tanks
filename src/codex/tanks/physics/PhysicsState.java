/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.physics;

import codex.tanks.components.EntityTransform;
import codex.tanks.components.Physics;
import codex.tanks.components.TransformMode;
import codex.tanks.components.Visual;
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

/**
 *
 * @author codex
 */
public class PhysicsState extends ESAppState implements PhysicsTickListener {
    
    private EntitySet basicSet;
    private EntitySet advSet;
    private HashMap<EntityId, RigidBodyControl> physics = new HashMap<>();
    private BulletAppState bulletapp;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        basicSet = ed.getEntities(Physics.class);
        advSet = ed.getEntities(
                new FunctionFilter<>(TransformMode.class, c -> c.anyMatch(m -> TransformMode.isPhysics(m))),
                Physics.class, EntityTransform.class, TransformMode.class);
        bulletapp = getState(BulletAppState.class, true);
        getPhysicsSpace().setGravity(new Vector3f(0f, -100f, 0f));
    }
    @Override
    protected void cleanup(Application app) {}
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
    }    
    @Override
    public void prePhysicsTick(PhysicsSpace space, float timeStep) {
        advSet.applyChanges();
        for (var e : advSet) {
            var object = physics.get(e.getId());
            if (object == null) continue;
            var transform = e.get(EntityTransform.class);
            var enable = e.get(TransformMode.class);
            if (TransformMode.isPhysics(enable.getTranslation())) {
                object.setPhysicsLocation(transform.getTranslation());
            }
            if (TransformMode.isPhysics(enable.getRotation())) {
                object.setPhysicsRotation(transform.getRotation());
            }
        }
    }
    @Override
    public void physicsTick(PhysicsSpace space, float timeStep) {
        for (var e : advSet) {
            var object = physics.get(e.getId());
            if (object == null) continue;
            // get physical attributes
        }
    }
    
    private void create(Entity e) {
        float mass = e.get(Physics.class).getMass();
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
        if (ed.getComponent(id, Physics.class) == null) {
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
    
    public PhysicsSpace getPhysicsSpace() {
        return bulletapp.getPhysicsSpace();
    }
    
}
