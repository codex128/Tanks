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
public class Player implements EntityComponent {
    
    private final int num;
    
    public Player(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
    @Override
    public String toString() {
        return "Player{" + "num=" + num + '}';
    }
    
}
