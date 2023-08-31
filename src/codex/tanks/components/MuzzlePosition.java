/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.jme3.math.Vector3f;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public class MuzzlePosition implements EntityComponent {
    
    private final Vector3f position = new Vector3f();
    
    public MuzzlePosition(Vector3f position) {
        this.position.set(position);
    }

    public Vector3f getPosition() {
        return position;
    }
    @Override
    public String toString() {
        return "MuzzlePosition{" + "position=" + position + '}';
    }
    
}
