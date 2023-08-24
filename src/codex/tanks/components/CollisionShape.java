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
    private final boolean ricochet;
    
    public CollisionShape() {
        this(null, false);
    }
    public CollisionShape(boolean ricochet) {
        this(null, ricochet);
    }
    public CollisionShape(String shape) {
        this(shape, false);
    }
    public CollisionShape(String shape, boolean ricochet) {
        this.shape = shape;
        this.ricochet = ricochet;
    }

    public String getShape() {
        return shape;
    }
    public boolean ricochet() {
        return ricochet;
    }
    @Override
    public String toString() {
        return "CollisionShape{" + "shape=" + shape + ", ricochet=" + ricochet + '}';
    }
    
}
