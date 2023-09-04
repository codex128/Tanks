/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import codex.tanks.systems.TankState;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityComponent;

/**
 * Output component from {@link TankState} which represents
 * a tank's probe location.
 * 
 * @author codex
 */
public class ProbeLocation implements EntityComponent {
    
    private final Vector3f location = new Vector3f();
    
    public ProbeLocation(Vector3f location) {
        this.location.set(location);
    }

    public Vector3f getLocation() {
        return location;
    }
    @Override
    public String toString() {
        return "ProbeLocation{" + "location=" + location + '}';
    }
    
}
