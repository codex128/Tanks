/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.tanks.systems.AIManager;

/**
 *
 * @author codex
 */
public interface Algorithm {
    
    public void initialize(AlgorithmUpdate update);
    public boolean move(AlgorithmUpdate update);
    public boolean aim(AlgorithmUpdate update);
    public boolean shoot(AlgorithmUpdate update);
    public boolean mine(AlgorithmUpdate update);
    public void cleanup(AlgorithmUpdate update);
    
}
