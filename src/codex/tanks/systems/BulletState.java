/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.Bullet;
import codex.tanks.components.Alive;
import codex.tanks.components.Bounces;
import codex.tanks.components.EntityTransform;
import codex.tanks.components.Velocity;
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
    
    private EntitySet entities;
    private final HashMap<EntityId, Bullet> bullets = new HashMap<>();
    private VisualState visuals;
    private CollisionState collision;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(Visual.class, EntityTransform.class, Velocity.class, Bounces.class, Alive.class);
        visuals = getState(VisualState.class, true);
        collision = getState(CollisionState.class, true);
    }
    @Override
    protected void cleanup(Application app) {
        entities.release();
        bullets.clear();
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (entities.applyChanges()) {
            entities.getAddedEntities().forEach(e -> createBullet(e));
            entities.getRemovedEntities().forEach(e -> destroyBullet(e));
        }
        for (var e : entities) {
            update(e, tpf);
        }
    }
    
    private void createBullet(Entity e) {
        var bullet = new Bullet(visuals.getSpatial(e.getId()), e);
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
    
}
