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
public class Door implements EntityComponent {
    
    private final boolean open;
    private final boolean next;
    private final Vector3f offset = new Vector3f();
    
    public Door(Vector3f offset) {
        this(false, false, offset);
    }
    private Door(boolean open, boolean next, Vector3f offset) {
        this.open = open;
        this.next = next;
        this.offset.set(offset);
    }

    public boolean isOpen() {
        return open;
    }
    public boolean getNextState() {
        return next;
    }
    public Vector3f getOffset() {
        return offset;
    }
    @Override
    public String toString() {
        return "Door{" + "open=" + open + ", offset=" + offset + '}';
    }
    
    public Door open(boolean open) {
        return new Door(this.open, open, offset);
    }
    public Door setCurrentState(boolean open) {
        return new Door(open, next, offset);
    }
    
}
