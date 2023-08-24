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
public class FaceVelocity implements EntityComponent {
    
    private final boolean facing;

    public FaceVelocity() {
        this(true);
    }
    public FaceVelocity(boolean facing) {
        this.facing = facing;
    }

    public boolean isFacing() {
        return facing;
    }
    @Override
    public String toString() {
        return "FaceVelocity{" + "facing=" + facing + '}';
    }
    
}
