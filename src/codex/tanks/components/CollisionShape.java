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
public class CollisionShape implements EntityComponent {
    
    private final String shape;
    
    public CollisionShape() {
        this(null);
    }
    public CollisionShape(String shape) {
        this.shape = shape;
    }

    public String getShape() {
        return shape;
    }
    @Override
    public String toString() {
        return "CollisionShape{" + "shape=" + shape + '}';
    }
    
}
