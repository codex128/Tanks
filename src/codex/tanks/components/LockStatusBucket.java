/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public class LockStatusBucket implements EntityComponent {
    
    private int status = 0;
    private EntityComponent[] lock = new EntityComponent[0];
    private EntityComponent[] unlock = new EntityComponent[0];

    public LockStatusBucket() {}
    public LockStatusBucket(EntityComponent[] lock, EntityComponent[] unlock) {
        this.lock = lock;
        this.unlock = unlock;
    }
    private LockStatusBucket(int status, EntityComponent[] lock, EntityComponent[] unlock) {
        this.status = status;
        this.lock = lock;
        this.unlock = unlock;
    }
    

    public LockStatusBucket setLock(EntityComponent... lock) {
        this.lock = lock;
        return this;
    }
    public LockStatusBucket setUnlock(EntityComponent... unlock) {
        this.unlock = unlock;
        return this;
    }
    public LockStatusBucket setStatus(int status) {
        return new LockStatusBucket(status, lock, unlock);
    }
    
    public EntityComponent[] getLock() {
        return lock;
    }
    public EntityComponent[] getUnlock() {
        return unlock;
    }
    public int getStatus() {
        return status;
    }
    @Override
    public String toString() {
        return "LockStatusBucket{" + "lock=" + lock.length + ", unlock=" + unlock.length + '}';
    }
    
    public static void add(EntityData ed, EntityId id, EntityComponent[] bucket) {
        ed.setComponents(id, bucket);
    }
    public static void remove(EntityData ed, EntityId id, EntityComponent[] bucket) {
        for (var c : bucket) {
            ed.removeComponent(id, c.getClass());
        }
    }
    
}
