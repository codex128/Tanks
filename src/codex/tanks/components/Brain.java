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
public class Brain implements EntityComponent {
    
    private final int n;

    public Brain(int n) {
        this.n = n;
    }

    public int getNumAlgorithmTypes() {
        return n;
    }
    @Override
    public String toString() {
        return "Brain{" + "n=" + n + '}';
    }
    
}
