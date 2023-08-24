/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.tanks.Bullet;
import codex.tanks.PlayerAppState;
import codex.tanks.Tank;
import codex.tanks.systems.AIManager;
import codex.tanks.systems.BulletState;
import codex.tanks.systems.CollisionState;
import com.jme3.math.Vector3f;
import java.util.Collection;

/**
 *
 * @author gary
 */
public class AlgorithmUpdate {
    
    private AIManager manager;
    private Tank tank;
    private float tpf;
    
    public AlgorithmUpdate(AIManager manager, Tank tank, float tpf) {
        this.manager = manager;
        this.tank = tank;
        this.tpf = tpf;
    }
    
    public AIManager getManager() {
        return manager;
    }
    public Tank getTank() {
        return tank;
    }
    public float getTpf() {
        return tpf;
    }
    
    public CollisionState getCollisionState() {
        return manager.getState(CollisionState.class);
    }
    public Tank getPlayerTank() {
        return manager.getState(PlayerAppState.class).getTank();
    }
    public Collection<Bullet> getBullets() {
        return manager.getState(BulletState.class).getBullets();
    }
    public Vector3f getDirectionToPlayer() {
        return getPlayerTank().getPosition().subtract(tank.getPosition()).normalizeLocal();
    }
        
}
