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
public class Forward implements EntityComponent {
    
    private final Vector3f forward = new Vector3f(0f, 0f, 1f);
    
    public Forward() {}
    public Forward(Vector3f forward) {
        this.forward.set(forward);
    }

    public Vector3f getForward() {
        return forward;
    }
    @Override
    public String toString() {
        return "Forward{" + "forward=" + forward + '}';
    }
    
}
