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
import codex.tanks.collision.CollisionState;
import codex.tanks.collision.ShapeFilter;
import codex.tanks.collision.BasicRaytest;
import codex.tanks.collision.LaserRaytest;
import codex.tanks.collision.OriginFilter;
import codex.tanks.systems.VisualState;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
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
    private VisualState visuals;
    private CollisionState collision;
    private Tank playerTank;
    private BulletState bulletState;
    private Vector3f dirToPlayer;
    private float distToPlayer;
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
        playerTank = manager.getState(PlayerAppState.class).getTank();
        if (playerTank == null) return false;
        visuals = manager.getState(VisualState.class);
        collision = manager.getState(CollisionState.class);
        bulletState = manager.getState(BulletState.class);
        dirToPlayer = playerTank.getProbeLocation().subtract(tank.getProbeLocation()).normalizeLocal();
        distToPlayer = playerTank.getPosition().distance(tank.getPosition());
        playerInView = calculatePlayerInView();
        playerInBounce = calculatePlayerInBounce();
        return true;
    }
    public boolean calculatePlayerInView() {
        var raytest = new BasicRaytest(new Ray(tank.getProbeLocation(), dirToPlayer), ShapeFilter.none(ShapeFilter.byId(getTankId())));
        raytest.cast(collision);
        return playerTank.getEntity().getId().equals(raytest.getCollisionEntity());
    }
    public boolean calculatePlayerInBounce() {
        var raytest = new LaserRaytest(tank.getAimRay(), new OriginFilter(getTankId(), null), tank.getEntity().get(Bounces.class).getRemaining());
        raytest.cast(collision);
        return playerTank.getEntity().getId().equals(raytest.getCollisionEntity());
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
    
    public EntityId getTankId() {
        return tank.getEntity().getId();
    }
    public EntityData getEntityData() {
        return manager.getEntityData();
    }
    public VisualState getVisualState() {
        return visuals;
    }
    public CollisionState getCollisionState() {
        return collision;
    }
    public Tank getPlayerTank() {
        return playerTank;
    }
    public EntityId getPlayerId() {
        return playerTank.getEntity().getId();
    }
    public Collection<Bullet> getBullets() {
        return bulletState.getBullets();
    }
    public Vector3f getDirectionToPlayer() {
        return dirToPlayer;
    }
    public float getDistanceToPlayer() {
        return distToPlayer;
    }
    public boolean isPlayerInView() {
        return playerInView;
    }
    public boolean isPlayerInBounce() {
        return playerInBounce;
    }
        
}
