/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.Physics;
import codex.tanks.util.ESAppState;
import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.HashMap;

/**
 *
 * @author codex
 */
public class PhysicsRegistry extends ESAppState {
    
    private EntitySet entities;
    private HashMap<EntityId, Object> physics = new HashMap<>();
    private BulletAppState bulletapp;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(Physics.class);
        bulletapp = getState(BulletAppState.class, true);
    }
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (entities.applyChanges()) for (var e : entities.getRemovedEntities()) {
            unlink(e.getId());
        }
    }
    
    public boolean link(EntityId id, Object object) {
        if (ed.getComponent(id, Physics.class) == null) return false;
        if (physics.putIfAbsent(id, object) == null) {
            getPhysicsSpace().add(object);
            return true;
        }
        return false;
    }
    public Object unlink(EntityId id) {
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
