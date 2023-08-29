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
    private final int group;
    
    public CollisionShape() {
        this(null, 0);
    }
    public CollisionShape(String shape) {
        this(shape, 0);
    }
    public CollisionShape(int group) {
        this(null, group);
    }
    public CollisionShape(String shape, int group) {
        this.shape = shape;
        this.group = group;
    }

    public String getShape() {
        return shape;
    }
    public int getGroup() {
        return group;
    }
    @Override
    public String toString() {
        return "CollisionShape{" + "shape=" + shape + ", group=" + group + '}';
    }
    
}
