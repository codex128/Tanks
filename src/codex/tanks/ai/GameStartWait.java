/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

/**
 *
 * @author gary
 */
public class GameStartWait implements TankAlgorithm {
    
    private final float duration;
    private float time = 0f;

    public GameStartWait(float duration) {
        this.duration = duration;
    }
    
    @Override
    public void updateTank(AlgorithmUpdate update) {
        time += update.getTpf();
        if (time > duration) time = duration+1;
    }
    @Override
    public void moveTank(AlgorithmUpdate update) {}
    @Override
    public void aimTank(AlgorithmUpdate update) {
        if (time < duration) update.consume();
    }
    @Override
    public void mineTank(AlgorithmUpdate update) {        
        if (time < duration) update.consume();
    }
    
}
