/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.tanks.GameState;
import codex.tanks.Tank;
import codex.tanks.TankModel;
import com.jme3.scene.Node;
import java.util.LinkedList;
import java.util.function.BiConsumer;

/**
 *
 * @author gary
 */
public class AITank extends Tank {
    
    private GameState game;
    private LinkedList<TankAlgorithm> algorithms = new LinkedList<>();
    
    public AITank(Node root, TankModel model, int team, GameState game) {
        super(root, model, team);
        this.game = game;
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        update(new AlgorithmUpdate(game, this, tpf), (a, u) -> a.updateTank(u));
        update(new AlgorithmUpdate(game, this, tpf), (a, u) -> a.moveTank(u));
        update(new AlgorithmUpdate(game, this, tpf), (a, u) -> a.aimTank(u));
        update(new AlgorithmUpdate(game, this, tpf), (a, u) -> a.mineTank(u));
    }
    private void update(AlgorithmUpdate u, BiConsumer<TankAlgorithm, AlgorithmUpdate> update) {
        for (TankAlgorithm a : algorithms) {
            update.accept(a, u);
        }
    }
    
    public void addAlgorithm(TankAlgorithm a) {
        algorithms.addLast(a);
    }
    public boolean removeAlgorithm(TankAlgorithm a) {
        return algorithms.remove(a);
    }
    public void clearAlgorithms() {
        algorithms.clear();
    }
    
}
