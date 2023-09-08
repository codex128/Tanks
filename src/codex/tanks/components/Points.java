/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.jme3.math.Vector3f;
import com.simsilica.es.EntityComponent;
import java.util.Collection;

/**
 *
 * @author codex
 */
public class Points implements EntityComponent {
    
    public static final String
            BULLET = "bullet",
            LASER = "laser",
            PHYSICAL = "physical";
    
    private final Vector3f[] points;

    public Points(Vector3f... points) {
        this.points = points;
    }
    public Points(Collection<Vector3f> points) {
        this.points = points.toArray(Vector3f[]::new);
    }

    public Vector3f[] getPoints() {
        return points;
    }
    @Override
    public String toString() {
        return "LaserPoints{" + "points=" + points.length + '}';
    }
    
}
