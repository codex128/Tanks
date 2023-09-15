/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityId;

/**
 * 
 * @author codex
 */
public class Gateway implements EntityComponent {
    
    private final boolean ready;
    private final EntityId[] doors;
    private final float distance;
    
    public Gateway(boolean ready, EntityId... doors) {
        this(ready, 5f, doors);
    }
    public Gateway(boolean ready, float distance, EntityId... doors) {
        this.ready = ready;
        this.doors = doors;
        this.distance = distance;
    }

    public boolean isReady() {
        return ready;
    }
    public EntityId[] getDoors() {
        return doors;
    }
    public float getDistance() {
        return distance;
    }
    @Override
    public String toString() {
        return "Gateway{" + "ready=" + ready + ", doors=" + doors.length + '}';
    }
    
    public Gateway setIsReady(boolean ready) {
        return new Gateway(ready, distance, doors);
    }
    
}
