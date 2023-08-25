/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.Tank;
import codex.tanks.components.Alive;
import codex.tanks.components.Bounces;
import codex.tanks.components.Firerate;
import codex.tanks.components.BulletCapacity;
import codex.tanks.components.ColorScheme;
import codex.tanks.components.GameObject;
import codex.tanks.components.MineCapacity;
import codex.tanks.components.Physics;
import codex.tanks.components.ShootForce;
import codex.tanks.components.Speed;
import codex.tanks.components.Visual;
import codex.tanks.util.ESAppState;
import com.jme3.app.Application;
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
    private VisualState visuals;
    private PhysicsState physics;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(
                GameObject.filter("tank"),
                GameObject.class, Visual.class, Physics.class,
                Speed.class, Firerate.class, BulletCapacity.class,
                Bounces.class, ShootForce.class, MineCapacity.class,
                ColorScheme.class, Alive.class);
        visuals = getState(VisualState.class, true);
        physics = getState(PhysicsState.class, true);
    }
    @Override
    protected void cleanup(Application app) {
        entities.release();
        for (var t : tanks.values()) {
            t.cleanup();
        }
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
        for (var t : tanks.values()) {
            t.update(tpf);
        }
    }
    
    private void createTank(Entity e) {
        var tank = new Tank(visuals.getSpatial(e.getId()), e, ed);
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
