/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import com.jme3.math.Vector3f;
import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class Offset implements EntityComponent {
    
    private final Vector3f offset = new Vector3f();
    
    public Offset(Vector3f offset) {
        this.offset.set(offset);
    }

    public Vector3f getOffset() {
        return offset;
    }
    @Override
    public String toString() {
        return "Offset{" + "offset=" + offset + '}';
    }
    
}
