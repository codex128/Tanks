/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.blueprints;

import codex.tanks.collision.ContactEvent;
import codex.tanks.components.Alive;
import codex.tanks.components.Bounces;
import codex.tanks.components.ContactResponse;
import codex.tanks.components.Damage;
import codex.tanks.components.Velocity;
import codex.tanks.util.GameUtils;
import com.jme3.math.FastMath;
import com.simsilica.es.EntityData;

/**
 * Tells entities what to do in certain contact situations.
 * 
 * @author codex
 */
public class ContactMethods {
    
    /**
     * Kills the target entity.
     */
    public static final String DIE = "contact:die";    
    /**
     * Kills the projectile.
     */
    public static final String KILL_PROJECTILE = "contact:kill-bullet";    
    /**
     * Ricochets the projectile if it has bounces remaining.
     * <p>Otherwise, kills the projectile.
     */
    public static final String RICOCHET = "contact:ricochet";
    /**
     * Ricochets the projectile, even if it has no bounces remaining.
     * <p>Does not affect the number of bounces remaining.
     */
    public static final String FORCE_RICOCHET = "contact:force-ricochet";
    
    private final EntityData ed;
    
    public ContactMethods(EntityData ed) {
        this.ed = ed;
    }
    
    public void respond(ContactResponse response, ContactEvent event) {
        for (var r : response.getPipelines()) {
            respond(r, event);
        }
    }
    private void respond(String response, ContactEvent event) {
        switch (response) {
            case DIE             -> killTarget(event);
            case KILL_PROJECTILE -> killProjectile(event);
            case RICOCHET        -> ricochetProjectile(event);
            case FORCE_RICOCHET  -> forceRicochetProjectile(event);
        }
    }
    
    public void killTarget(ContactEvent event) {
        if (event.projectile.get(Damage.class).getDamage() > 0) {
            ed.setComponent(event.target, new Alive(false));
        }
    }
    public void killProjectile(ContactEvent event) {
        event.projectile.set(new Alive(false));
    }
    public void ricochetProjectile(ContactEvent event) {
        var b = event.projectile.get(Bounces.class);
        if (!b.isExhausted()) {
            var v = event.projectile.get(Velocity.class);
            event.projectile.set(v.setDirection(GameUtils.ricochet(v.getDirection(), event.collision.getContactNormal())));
            event.projectile.set(b.increment());
        }
        else {
            event.projectile.set(new Alive(false));
        }
    }
    public void forceRicochetProjectile(ContactEvent event) {
        var v = event.projectile.get(Velocity.class);
        event.projectile.set(v.setDirection(GameUtils.ricochet(v.getDirection(), event.collision.getContactNormal())));
    }
    
}
