/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import codex.tanks.ai.Algorithm;
import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class Brain implements EntityComponent {
    
    private final Algorithm[] algorithms;

    public Brain(Algorithm... algorithms) {
        this.algorithms = algorithms;
    }
    
    public Algorithm[] getAlgorithms() {
        return algorithms;
    }

    @Override
    public String toString() {
        return "Brain{" + "algorithms=" + algorithms.length + '}';
    }
    
    
}
