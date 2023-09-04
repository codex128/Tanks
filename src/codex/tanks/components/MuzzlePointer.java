/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityId;

/**
 * Points to another entity representing the muzzle location and direction for shooting.
 * 
 * @author codex
 */
public class MuzzlePointer implements EntityComponent {
    
    private final EntityId muzzle;
    
    public MuzzlePointer(EntityId muzzle) {
        this.muzzle = muzzle;
    }

    public EntityId getId() {
        return muzzle;
    }
    @Override
    public String toString() {
        return "MuzzlePointer{" + "muzzle=" + muzzle + '}';
    }
    
}
