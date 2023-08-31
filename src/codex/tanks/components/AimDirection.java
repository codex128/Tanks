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
public class AimDirection implements EntityComponent {
    
    private final Vector3f aim = new Vector3f();
    
    public AimDirection(Vector3f aim) {
        this.aim.set(aim);
    }

    public Vector3f getAim() {
        return aim;
    }
    @Override
    public String toString() {
        return "AimDirection{" + "aim=" + aim + '}';
    }
    
}
