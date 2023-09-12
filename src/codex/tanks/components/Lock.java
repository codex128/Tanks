/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class Lock implements EntityComponent {
    
    private final boolean locked;
    private final int value;
    
    public Lock(boolean locked) {
        this(locked, AccessKey.BASIC);
    }
    public Lock(boolean locked, int value) {
        this.locked = locked;
        this.value = value;
    }

    public boolean isLocked() {
        return locked;
    }
    public int getKeyValue() {
        return value;
    }
    public boolean acceptsKey(AccessKey key) {
        return (key.getKey() >= value && value >= 0) || (key.getKey() == value && value < 0);
    }
    @Override
    public String toString() {
        return "Locked{" + "locked=" + locked + ", value=" + value + '}';
    }
    
    public Lock unlock(AccessKey key) {
        if (acceptsKey(key)) {
            return new Lock(false, value);
        }
        else {
            return this;
        }
    }
    public Lock forceUnlock() {
        return new Lock(false, value);
    }
    public Lock lock() {
        return new Lock(true, value);
    }
    
}
