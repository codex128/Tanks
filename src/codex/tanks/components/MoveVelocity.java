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
public class MoveVelocity implements EntityComponent {
    
    private final Vector3f move = new Vector3f();
    
    public MoveVelocity(Vector3f move) {
        this.move.set(move);
    }
    
    public Vector3f getMove() {
        return move;
    }
    @Override
    public String toString() {
        return "MoveVelocity{" + "move=" + move + '}';
    }
    
}
