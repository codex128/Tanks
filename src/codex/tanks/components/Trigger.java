/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.simsilica.es.EntityComponent;

/**
 * Used by {@link GunState} to query if another system wants to shoot or wants to block shooting.
 * 
 * @author codex
 */
public class Trigger implements EntityComponent {
    
    private final int value;
    
    public Trigger() {
        this(0);
    }
    public Trigger(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    public Trigger pull() {
        return new Trigger(value >= 0 ? 1 : -1);
    }
    public Trigger terminate() {
        return new Trigger(-1);
    }
    @Override
    public String toString() {
        return "Trigger{" + "value=" + value + '}';
    }
    
}
