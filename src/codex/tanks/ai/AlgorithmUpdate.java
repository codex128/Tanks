/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.tanks.Bullet;
import codex.tanks.PlayerAppState;
import codex.tanks.Tank;
import codex.tanks.components.Bounces;
import codex.tanks.systems.AIManager;
import codex.tanks.systems.BulletState;
import codex.tanks.systems.CollisionState;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityData;
import java.util.Collection;

/**
 *
 * @author gary
 */
public class AlgorithmUpdate {
    
    private final AIManager manager;
    private final Tank tank;
    private final float tpf;
    private final boolean satisfied;
    private CollisionState collision;
    private Tank playerTank;
    private BulletState bulletState;
    private Vector3f dirToPlayer;
    private boolean playerInView;
    private boolean playerInBounce;
    
    public AlgorithmUpdate(AIManager manager, Tank tank, float tpf) {
        this.manager = manager;
        this.tank = tank;
        this.tpf = tpf;
        satisfied = initialize();
    }
    
    private boolean initialize() {
        if (tank == null) return false;
        collision = manager.getState(CollisionState.class);
        playerTank = manager.getState(PlayerAppState.class).getTank();
        if (playerTank == null) return false;
        bulletState = manager.getState(BulletState.class);
        dirToPlayer = playerTank.getPosition().subtract(tank.getPosition()).normalizeLocal();
        playerInView = calculatePlayerInView();
        playerInBounce = calculatePlayerInBounce();
        return true;
    }
    private boolean calculatePlayerInView() {
        return playerTank.getEntity().getId().equals(collision.raycast(
                new Ray(tank.getProbeLocation(), dirToPlayer), tank.getEntity().getId(), 0));
    }
    private boolean calculatePlayerInBounce() {
        return playerTank.getEntity().getId().equals(collision.raycast(
                tank.getAimRay(), tank.getEntity().getId(), tank.getEntity().get(Bounces.class).getRemaining()));
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
    public boolean isInfoSatisfied() {
        return satisfied;
    }
    
    public EntityData getEntityData() {
        return manager.getEntityData();
    }
    public CollisionState getCollisionState() {
        return collision;
    }
    public Tank getPlayerTank() {
        return playerTank;
    }
    public Collection<Bullet> getBullets() {
        return bulletState.getBullets();
    }
    public Vector3f getDirectionToPlayer() {
        return dirToPlayer;
    }
    public boolean isPlayerInView() {
        return playerInView;
    }
    public boolean isPlayerInBounce() {
        return playerInBounce;
    }
        
}
