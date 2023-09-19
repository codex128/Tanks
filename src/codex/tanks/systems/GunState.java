/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.boost.Listenable;
import codex.tanks.components.Bounces;
import codex.tanks.components.BulletCapacity;
import codex.tanks.components.EntityTransform;
import codex.tanks.components.Firerate;
import codex.tanks.components.MuzzlePointer;
import codex.tanks.components.RemoveOnSleep;
import codex.tanks.components.Power;
import codex.tanks.components.RoomStatus;
import codex.tanks.components.Team;
import codex.tanks.components.RoomIndex;
import codex.tanks.components.SpawnAssignment;
import codex.tanks.dungeon.DungeonMaster;
import codex.tanks.es.ESAppState;
import codex.tanks.weapons.ShootEventListener;
import com.jme3.app.Application;
import com.jme3.math.Vector3f;
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

    public static final boolean SHOOTING_ENABLED = true;
    
    private EntitySet entities;
    private EntitySet firerate;
    private OwnerState owners;
    private LinkedList<ShootEventListener> listeners = new LinkedList<>();
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(RoomIndex.class, Power.class, Bounces.class,
                Team.class, MuzzlePointer.class, SpawnAssignment.class);
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
        return SHOOTING_ENABLED && isEntityRoomActive(id) && (rate == null || rate.isReady())
                && owners.isBelowCapacity(id, BulletCapacity.class, "bullet");
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
        //if (e == null) System.out.println("does not exist");
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
        var muzzle = e.get(MuzzlePointer.class);
        var transform = ed.getComponent(muzzle.getId(), EntityTransform.class);
        if (transform == null) {
            throw new NullPointerException("MuzzlePointer must have transform!");
        }
        var id = factory.getEntityFactory().createProjectile(
            e.getId(),
            transform.getTranslation(),
            transform.getRotation().mult(Vector3f.UNIT_Z),
            e.get(Power.class).getValue(),
            e.get(Bounces.class).getRemaining()
        );
        var assign = e.get(SpawnAssignment.class).getAssignment();
        RoomIndex index;
        if (assign.equals(SpawnAssignment.TO_ACTIVE)) {
            index = getState(DungeonMaster.class).getActiveRoomIndex();
        }
        else {
            index = e.get(RoomIndex.class);
        }
        ed.setComponents(id,
            index,
            new RoomStatus(RoomStatus.ACTIVE),
            new RemoveOnSleep()
        );
        factory.getEntityFactory().createMuzzleflash(muzzle.getId(), .7f);
        return true;
    }
    private boolean queryEventListeners(EntityId id) {
        for (var f : listeners) {
            if (!f.approveShootEvent(id)) return false;
        }
        return true;
    }
    
}
