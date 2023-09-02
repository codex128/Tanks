/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import codex.tanks.util.GameUtils;
import com.jme3.collision.CollisionResult;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public class ContactReaction implements EntityComponent {
    
    /**
     * Kills projectiles.
     */
    public static final ContactReaction
    SIMPLE = new ContactReaction() {
        @Override
        public void react(EntityData ed, EntityId target, Entity bullet, CollisionResult collision) {
            bullet.set(new Alive(false));
        }
    };
    
    /**
     * Ricochets projectiles.
     */
    public static final ContactReaction
    RICOCHET = new ContactReaction() {
        @Override
        public void react(EntityData ed, EntityId target, Entity bullet, CollisionResult collision) {
            var b = bullet.get(Bounces.class);
            if (!b.isExhausted()) {
                var v = bullet.get(Velocity.class);
                bullet.set(new Velocity(GameUtils.ricochet(v.getDirection(), collision.getContactNormal()).multLocal(v.getSpeed())));
                bullet.set(b.increment());
            }
            else {
                bullet.set(new Alive(false));
            }
        }
        @Override
        public boolean ricochet() {
            return true;
        }
    };
    
    /**
     * Kills this entity and the projectile entity.
     */
    public static final ContactReaction
    DIE = new ContactReaction() {
        @Override
        public void react(EntityData ed, EntityId target, Entity bullet, CollisionResult collision) {
            bullet.set(new Alive(false));
            ed.setComponent(target, new Alive(false));
        }
    };
    
    private ContactReaction delegate;
    
    public ContactReaction() {}
    public ContactReaction(ContactReaction delegate) {
        this.delegate = delegate;
    }
    
    public void react(EntityData ed, EntityId target, Entity bullet, CollisionResult collision) {
        if (delegate != null) {
            delegate.react(ed, target, bullet, collision);
        }
    }
    public boolean ricochet() {
        if (delegate == null) return false;
        else return delegate.ricochet();
    }
    @Override
    public String toString() {
        return "ContactReaction{" + "delegate=" + delegate + '}';
    }
    
}
