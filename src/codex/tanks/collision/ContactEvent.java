/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.collision;

import com.jme3.collision.CollisionResult;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public class ContactEvent {
    
    public final EntityId target;
    public final Entity projectile;
    public final CollisionResult collision;

    public ContactEvent(EntityId target, Entity projectile, CollisionResult collision) {
        this.target = target;
        this.projectile = projectile;
        this.collision = collision;
    }

    public EntityId getTarget() {
        return target;
    }
    public Entity getProjectile() {
        return projectile;
    }
    public CollisionResult getCollision() {
        return collision;
    }
    
}
