/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import codex.tanks.components.Alive;
import codex.tanks.components.Bounces;
import codex.tanks.components.EntityTransform;
import codex.tanks.components.Velocity;
import codex.tanks.systems.CollisionState;
import codex.tanks.systems.VisualState;
import codex.tanks.util.GameUtils;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.simsilica.es.Entity;

/**
 *
 * @author gary
 */
public class Bullet {
    
    private final Spatial spatial;
    private final Entity entity;
    private int bouncesMade = 0;
    
    public Bullet(Spatial spatial, Entity entity) {
        this.spatial = spatial;
        this.entity = entity;
        initialize();
    }
    
    private void initialize() {
        GameUtils.getChild(spatial, "hitbox").setCullHint(Spatial.CullHint.Always);
    }
    
    public void update(CollisionState collision, float tpf) {
        var results = collision.raycast(getMovementRay(), entity.getId());
        if (results.size() > 0) {
            var closest = results.getClosestCollision();
            if (closest.getDistance() < entity.get(Velocity.class).getSpeed()*tpf*2) {
                var id = VisualState.fetchId(closest.getGeometry(), -1);
                if (id != null) {
                    collision.bulletCollision(id, this, closest);
                }
            }
        }
    }
    public void ricochet(Vector3f normal) {
        // bullet state
        var b = entity.get(Bounces.class);
        if (!b.isExhausted()) {
            var v = entity.get(Velocity.class);
            entity.set(new Velocity(GameUtils.ricochet(v.getDirection(), normal).multLocal(v.getSpeed())));
            entity.set(b.increment());
            bouncesMade++;
        }
        else {
            entity.set(new Alive(false));
        }
    }
    
    public Spatial getSpatial() {
        return spatial;
    }
    public Entity getEntity() {
        return entity;
    }
    public Vector3f getPosition() {
        return entity.get(EntityTransform.class).getTranslation();
    }
    public Ray getMovementRay() {
        return new Ray(getPosition(), entity.get(Velocity.class).getDirection());
    }
    public int getBouncesMade() {
        return bouncesMade;
    }
    
}
