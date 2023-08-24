/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import codex.tanks.Bullet;
import com.jme3.collision.CollisionResult;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public abstract class BulletReaction implements EntityComponent {
    
    public static final BulletReaction
    SIMPLE = new BulletReaction() {
        @Override
        public void react(EntityData ed, EntityId target, Bullet bullet, CollisionResult collision) {
            bullet.getEntity().set(new Alive(false));
        }
    },
    RICOCHET = new BulletReaction() {
        @Override
        public void react(EntityData ed, EntityId target, Bullet bullet, CollisionResult collision) {
            bullet.ricochet(collision.getContactNormal());
        }
    },
    DIE = new BulletReaction() {
        @Override
        public void react(EntityData ed, EntityId target, Bullet bullet, CollisionResult collision) {
            bullet.getEntity().set(new Alive(false));
            ed.setComponent(target, new Alive(false));
        }
    };
    
    public BulletReaction() {}
    
    public abstract void react(EntityData ed, EntityId target, Bullet bullet, CollisionResult collision);
    @Override
    public String toString() {
        return "BulletReaction{" + '}';
    }
    
}
