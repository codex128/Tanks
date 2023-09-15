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
public class SpawnAssignment implements EntityComponent {
    
    public static final String
            TO_ACTIVE = "spawn:to-active",
            TO_OWN = "spawn:to-own";
    
    private final String assignment;

    public SpawnAssignment(String assignment) {
        this.assignment = assignment;
    }

    public String getAssignment() {
        return assignment;
    }
    @Override
    public String toString() {
        return "SpawnAssignment{" + "assignment=" + assignment + '}';
    }
    
}
