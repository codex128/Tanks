/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

/**
 *
 * @author gary
 */
public class GameStartWait implements Algorithm {
    
    private final float duration;
    private float time = 0f;

    public GameStartWait(float duration) {
        this.duration = duration;
    }
    
    @Override
    public void update(AlgorithmUpdate update) {
        time += update.getTpf();
        if (time > duration) time = duration+1;
    }
    @Override
    public boolean move(AlgorithmUpdate update) {
        return false;
    }
    @Override
    public boolean aim(AlgorithmUpdate update) {
        return time < duration;
    }
    @Override
    public boolean shoot(AlgorithmUpdate update) {
        return time < duration;
    }
    @Override
    public boolean mine(AlgorithmUpdate update) {        
        return time < duration;
    }
    @Override
    public void cleanup(AlgorithmUpdate update) {}
    
}
