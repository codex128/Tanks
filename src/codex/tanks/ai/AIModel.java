/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author codex
 */
public class AIModel {
    
    private LinkedList<Algorithm> stack = new LinkedList<>();
    
    public void add(Algorithm alg) {
        stack.addLast(alg);
    }
    public Collection<Algorithm> getCommandStack() {
        return stack;
    }
    
}
