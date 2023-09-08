/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.jme3.math.Quaternion;
import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class Rotate implements EntityComponent {
    
    private final Quaternion rotation = new Quaternion();

    public Rotate(Quaternion rotation) {
        this.rotation.set(rotation);
    }

    public Quaternion getRotation() {
        return rotation;
    }
    @Override
    public String toString() {
        return "Rotate{" + "rotation=" + rotation + '}';
    }
    
}
