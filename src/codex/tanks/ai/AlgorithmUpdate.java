/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.tanks.GameState;
import codex.tanks.Tank;

/**
 *
 * @author gary
 */
public class AlgorithmUpdate {
    
    private GameState game;
    private AITank tank;
    private float tpf;
    private boolean consumed = false;
    
    public AlgorithmUpdate(GameState game, AITank tank, float tpf) {
        this.game = game;
        this.tank = tank;
        this.tpf = tpf;
    }
    
    public void consume() {
        consumed = true;
    }
    
    public GameState getGame() {
        return game;
    }
    public AITank getTank() {
        return tank;
    }
    public float getTpf() {
        return tpf;
    }
    public boolean isConsumed() {
        return consumed;
    }
    
    public Tank getPlayerTank() {
        return game.getPlayerState().getTank();
    }
        
}
