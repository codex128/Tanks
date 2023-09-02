/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.weapons;

import codex.boost.Listenable;
import codex.tanks.components.AimDirection;
import codex.tanks.components.Bounces;
import codex.tanks.components.BulletCapacity;
import codex.tanks.components.Firerate;
import codex.tanks.components.MuzzlePosition;
import codex.tanks.components.Power;
import codex.tanks.components.Team;
import codex.tanks.components.Trigger;
import codex.tanks.systems.OwnerState;
import codex.tanks.util.ESAppState;
import com.jme3.app.Application;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author codex
 */
public class GunState extends ESAppState implements Listenable<ShootEventListener>, ShootEventListener {

    private EntitySet entities;
    private EntitySet firerate;
    private OwnerState owners;
    private LinkedList<ShootEventListener> listeners = new LinkedList<>();
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(Power.class, Bounces.class, Team.class, MuzzlePosition.class, AimDirection.class);
        firerate = ed.getEntities(Firerate.class);
        owners = getState(OwnerState.class, true);
        addListener(this);
    }
    @Override
    protected void cleanup(Application app) {
        clearAllListeners();
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {   
        firerate.applyChanges();
        for (var e : firerate) {
            var rate = e.get(Firerate.class);
            if (!rate.isReady()) {
                e.set(rate.increment(tpf));
            }
        }
    }
    @Override
    public Collection<ShootEventListener> getListeners() {
        return listeners;
    }    
    @Override
    public boolean approveShootEvent(EntityId id) {
        var rate = ed.getComponent(id, Firerate.class);
        return (rate == null || rate.isReady()) && owners.isBelowCapacity(id, BulletCapacity.class, "bullet");
    }
    @Override
    public void onShootEventPassed(EntityId id) {
        var rate = ed.getComponent(id, Firerate.class);
        if (rate != null) {
            ed.setComponent(id, rate.shoot());
        }
    }
    
    public boolean shoot(EntityId id) {
        entities.applyChanges();
        var e = entities.getEntity(id);
        if (e == null || !queryEventListeners(id)) {
            return false;
        }
        if (shoot(e)) {
            notifyListeners(l -> l.onShootEventPassed(id));
            return true;
        }
        return false;
    }
    private boolean shoot(Entity e) {
        factory.getEntityFactory().createBullet(
            e.getId(),
            e.get(MuzzlePosition.class).getPosition(),
            e.get(AimDirection.class).getAim().mult(e.get(Power.class).getPower()),
            e.get(Bounces.class).getRemaining());
        return true;
    }
    private boolean queryEventListeners(EntityId id) {
        for (var f : listeners) {
            if (!f.approveShootEvent(id)) return false;
        }
        return true;
    }
    
}
