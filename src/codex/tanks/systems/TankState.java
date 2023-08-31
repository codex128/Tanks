/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.Tank;
import codex.tanks.components.*;
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
    private PhysicsState physics;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        // for tanks: GameObject (tank), Visual, Physics, MoveVelocity, MuzzlePosition
        // for ai: Bounces, Team
        // for shooting: Power, Bounces, Team
        // for shooting limitation: BulletCapacity, Firerate
        // for mines: MineCapacity
        // for materials: ColorScheme
        entities = ed.getEntities(GameObject.filter("tank"),
                GameObject.class, Visual.class, Physics.class, MoveVelocity.class, MuzzlePosition.class, AimDirection.class);
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
    
    public EntityId shoot(EntityId id) {
        var tank = getTank(id);
        var aim = tank.getAimRay();
        var bullet = factory.getEntityFactory().createBullet(id, aim.getOrigin(),
                aim.getDirection().multLocal(tank.getEntity().get(Power.class).getPower()),
                tank.getEntity().get(Bounces.class).getRemaining());
        var flash = factory.getEntityFactory().createMuzzleflash(.7f);
        ed.setComponent(flash, new Visual());
        var flashSpatial = factory.getSpatialFactory().createMuzzleflash();
        tank.getMuzzleNode().attachChild(flashSpatial);
        visuals.link(flash, flashSpatial);
        return bullet;
    }
    
}
