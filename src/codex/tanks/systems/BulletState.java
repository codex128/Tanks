/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.collision.CollisionState;
import codex.tanks.Bullet;
import codex.tanks.Missile;
import codex.tanks.components.*;
import codex.tanks.factory.SpatialFactory;
import codex.tanks.util.ESAppState;
import com.jme3.app.Application;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author codex
 */
public class BulletState extends ESAppState {
    
    public static final float MISSILE_QUALIFIER = 15f;
    
    private EntitySet set;
    private final HashMap<EntityId, Bullet> bullets = new HashMap<>();
    private VisualState visuals;
    private CollisionState collision;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        set = ed.getEntities(Visual.class, EntityTransform.class, Velocity.class, Bounces.class, Owner.class, Alive.class);
        visuals = getState(VisualState.class, true);
        collision = getState(CollisionState.class, true);
    }
    @Override
    protected void cleanup(Application app) {
        set.release();
        bullets.clear();
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (set.applyChanges()) {
            set.getAddedEntities().forEach(e -> createBullet(e));
            set.getRemovedEntities().forEach(e -> destroyBullet(e));
        }
        for (var e : set) {
            update(e, tpf);
        }
    }
    
    private void createBullet(Entity e) {
        Bullet bullet;
        if (e.get(Velocity.class).getSpeed() < MISSILE_QUALIFIER) {
            bullet = new Bullet(visuals.getSpatial(e.getId()), e);
        }
        else {
            bullet = new Missile(visuals.getSpatial(e.getId()), e);
        }
        bullets.putIfAbsent(e.getId(), bullet);
    }
    private void destroyBullet(Entity e) {
        bullets.remove(e.getId());
    }    
    private void update(Entity e, float tpf) {
        var bullet = bullets.get(e.getId());
        bullet.update(collision, tpf);
    }
    
    public Collection<Bullet> getBullets() {
        return bullets.values();
    }
    
    public static boolean isMissile(float speed) {
        return speed >= MISSILE_QUALIFIER;
    }
    public static String getBulletModelId(float speed) {
        if (speed < MISSILE_QUALIFIER) return SpatialFactory.BULLET;
        else return SpatialFactory.MISSILE;
    }
    
}
