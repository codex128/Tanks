/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.j3map.J3map;
import codex.tanks.ai.Algorithm;
import codex.tanks.ai.AlgorithmUpdate;
import codex.tanks.components.AimDirection;
import codex.tanks.components.Bounces;
import codex.tanks.components.Brain;
import codex.tanks.components.EntityTransform;
import codex.tanks.components.Forward;
import codex.tanks.components.MaxSpeed;
import codex.tanks.components.MuzzlePointer;
import codex.tanks.components.ProbeLocation;
import codex.tanks.components.PropertySource;
import codex.tanks.components.RoomStatus;
import codex.tanks.components.Team;
import codex.tanks.es.ESAppState;
import codex.tanks.es.FunctionFilter;
import com.jme3.app.Application;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.HashMap;

/**
 *
 * @author codex
 */
public class AIManager extends ESAppState {
    
    private EntitySet entities;
    private final HashMap<EntityId, Algorithm[]> brains = new HashMap<>();
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(new FunctionFilter<>(RoomStatus.class, c -> c.getState() == RoomStatus.ACTIVE),
                RoomStatus.class, Brain.class, EntityTransform.class, AimDirection.class, MuzzlePointer.class,
                Forward.class, ProbeLocation.class, Bounces.class, MaxSpeed.class, Team.class, PropertySource.class);
        //tanks = getState(TankState.class, true);
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
            entities.getAddedEntities().forEach(e -> create(e));
            entities.getRemovedEntities().forEach(e -> destroy(e));
        }
        for (var e : entities) {
            if (!isEntityRoomActive(e.getId())) {
                continue;
            }
            update(e, tpf);
        }
    }
    
    private Algorithm[] create(Entity e) {
        var src = e.get(PropertySource.class).load(assetManager);
        var algorithms = new Algorithm[e.get(Brain.class).getNumAlgorithmTypes()];
        int i = 0;
        for (var p : src.getOrderedPropertyList()) { 
            if (i >= algorithms.length) {
                break;
            }
            if (!(p.property instanceof J3map)) {
                continue;
            }
            var clazz = Algorithm.classes.get(p.key);
            if (clazz != null) {
                var a = Algorithm.createFromClass((J3map)p.property, clazz);
                if (a != null) {
                    a.initialize(app);
                    algorithms[i++] = a;
                }
            }
        }
        brains.put(e.getId(), algorithms);
        return algorithms;
    }
    private Algorithm[] destroy(Entity e) {
        var algorithms = brains.remove(e.getId());
        if (algorithms != null) for (var a : algorithms) {
            a.cleanup(app);
        }
        return algorithms;
    }
    private void update(Entity e, float tpf) {
        boolean move = true, aim = true, shoot = true, mine = true;
        var update = new AlgorithmUpdate(this, e, tpf);
        // check if the update object has enough info to make an update
        if (!update.isInfoSatisfied()) return;
        // fetch the target entity
        for (var a : brains.get(e.getId())) {
            var target = a.fetchTargetId(update);
            if (target != null) {
                update.setTarget(target);
                break;
            }
        }
        if (update.getTarget() == null) return;
        // update the AI
        for (var a : brains.get(e.getId())) {
            a.update(update);
            if (move)  move  = !a.move(update);
            if (aim)   aim   = !a.aim(update);
            if (shoot) shoot = !a.shoot(update);
            if (mine)  mine  = !a.mine(update);
            a.endUpdate(update);
        }
    }
    
    public Algorithm[] getAlgorithmArray(EntityId id) {
        return brains.get(id);
    }
    
}
