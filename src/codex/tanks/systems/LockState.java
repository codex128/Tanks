/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.Copy;
import codex.tanks.components.Lock;
import codex.tanks.components.LockStatusBucket;
import codex.tanks.es.ESAppState;
import com.jme3.app.Application;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class LockState extends ESAppState {

    private EntitySet entities;
    private EntitySet copies;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(Lock.class, LockStatusBucket.class);
        copies = ed.getEntities(Copy.filter(Copy.LOCK), Copy.class, Lock.class);
    }
    @Override
    protected void cleanup(Application app) {
        entities.release();
        copies.release();
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (entities.applyChanges()) {
            entities.getAddedEntities().forEach(e -> update(e));
            entities.getChangedEntities().forEach(e -> update(e));
        }
        copies.applyChanges();
        for (var e : copies) {
            var lock = ed.getComponent(e.get(Copy.class).getCopy(), Lock.class);
            if (lock != null) {
                e.set(new Lock(lock.isLocked()));
            }
        }
    }
    
    private void update(Entity e) {
        var bucket = e.get(LockStatusBucket.class);
        if (bucket.getStatus() >= 0 && e.get(Lock.class).isLocked()) {
            LockStatusBucket.add(ed, e.getId(), bucket.getLock());
            LockStatusBucket.remove(ed, e.getId(), bucket.getUnlock());
            e.set(bucket.setStatus(-1));
        }
        else if (bucket.getStatus() <= 0 && !e.get(Lock.class).isLocked()) {
            LockStatusBucket.add(ed, e.getId(), bucket.getUnlock());
            LockStatusBucket.remove(ed, e.getId(), bucket.getLock());
            e.set(bucket.setStatus(1));
        }
    }
    
}
