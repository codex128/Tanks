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
public class RoomStatus implements EntityComponent {
    
    /**
     * Sleeping: no activity.
     */
    public static final int SLEEPING = -2;
    /**
     * Moving towards sleeping.
     */
    public static final int DOWN = -1;
    /**
     * Waking up.
     */
    public static final int UP = 1;
    /**
     * Active.
     */
    public static final int ACTIVE = 2;
    
    private final int state;

    public RoomStatus() {
        this(ACTIVE);
    }
    public RoomStatus(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
    public boolean isBetween() {
        return state == DOWN || state == UP;
    }
    public boolean isUpper() {
        return state >= UP;
    }
    public boolean isLower() {
        return state >= DOWN;
    }
    @Override
    public String toString() {
        return "RoomStatus{" + "state=" + state + '}';
    }
    
}
