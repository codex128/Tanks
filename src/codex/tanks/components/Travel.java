/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.jme3.math.Vector3f;
import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class Travel implements EntityComponent {
    
    private final Vector3f destination = new Vector3f();
    
    public Travel(Vector3f destination) {
        this.destination.set(destination);
    }

    public Vector3f getDestination() {
        return destination;
    }
    @Override
    public String toString() {
        return "Travel{" + "destination=" + destination + '}';
    }
    
}
