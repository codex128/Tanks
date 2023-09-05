/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.*;
import codex.tanks.util.ESAppState;
import codex.tanks.weapons.Tank;
import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.HashMap;

/**
 *
 * @author codex
 */
public class TankState extends ESAppState {
    
    private EntitySet entities;
    private final HashMap<EntityId, Tank> tanks = new HashMap<>();
    private PhysicsState physics;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(GameObject.filter("tank"),
                GameObject.class, Visual.class, RigidBody.class, MoveVelocity.class, TurnSpeed.class,
                Forward.class, LinearVelocity.class, ProbeLocation.class, MuzzlePointer.class, AimDirection.class,
                Alive.class);
        physics = getState(PhysicsState.class, true);
    }
    @Override
    protected void cleanup(Application app) {
        entities.release();
        tanks.clear();
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (entities.applyChanges()) {
            entities.getAddedEntities().forEach(e -> createTank(e));
            entities.getRemovedEntities().forEach(e -> destroyTank(e));
        }
        for (var e : entities) {
            var t = tanks.get(e.getId());
            tanks.get(e.getId()).update(tpf);
            ed.setComponent(e.get(MuzzlePointer.class).getId(), new EntityTransform()
                    .setTranslation(t.getMuzzleLocation())
                    .setRotation(e.get(AimDirection.class).getAim(), Vector3f.UNIT_Y));
            t.getMatChanges().applyChanges(ed, e.getId());
        }
    }
    
    private void createTank(Entity e) {
        var tank = new Tank(visuals.getSpatial(e.getId()), e);
        tanks.put(e.getId(), tank);
        physics.link(e.getId(), tank.getPhysics());
    }
    private void destroyTank(Entity e) {
        tanks.remove(e.getId());
        physics.unlink(e.getId());
    }
    
    public Tank getTank(EntityId id) {
        return tanks.get(id);
    }
    
}
