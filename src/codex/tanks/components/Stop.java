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
public class Stop implements EntityComponent {
    
    private final Class[] protect;

    public Stop(Class... protect) {
        this.protect = protect;
    }

    public Class[] getProtectedClasses() {
        return protect;
    }
    @Override
    public String toString() {
        return "Stop{" + "protect=" + protect.length + '}';
    }
    
}
