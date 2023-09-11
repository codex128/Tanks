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
public class RoomCondition implements EntityComponent {
    
    public static final int SLEEPING = 0, DOWN = 1, UP = 2, ACTIVE = 3;
    
    private final int condition;

    public RoomCondition(int condition) {
        this.condition = condition;
    }

    public int getCondition() {
        return condition;
    }
    public boolean isBetween() {
        return condition == DOWN || condition == UP;
    }
    public boolean isUpper() {
        return condition >= UP;
    }
    public boolean isLower() {
        return condition >= DOWN;
    }
    @Override
    public String toString() {
        return "RoomCondition{" + "condition=" + condition + '}';
    }
    
}
