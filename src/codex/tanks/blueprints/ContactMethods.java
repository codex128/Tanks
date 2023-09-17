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
import codex.tanks.components.Owner;
import codex.tanks.components.Team;
import codex.tanks.components.Velocity;
import codex.tanks.util.GameUtils;
import com.simsilica.es.EntityData;

/**
 * Tells entities what to do in certain contact situations.
 * 
 * @author codex
 */
public class ContactMethods {
    
    /**
     * Kills the target entity if the bullet entity does not belong
     * to the target entities team.
     */
    public static final String DIE_UNFRIENDLY = "contact:die-unfriendly";    
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
            case DIE_UNFRIENDLY             -> killTargetIfUnfriendly(event);
            case KILL_PROJECTILE -> killProjectile(event);
            case RICOCHET        -> ricochetProjectile(event);
            case FORCE_RICOCHET  -> forceRicochetProjectile(event);
        }
    }
    
    public void killTargetIfUnfriendly(ContactEvent event) {
        if (event.projectile.get(Damage.class).getDamage() > 0) {
            var t1 = ed.getComponent(event.target, Team.class);
            var t2 = ed.getComponent(event.projectile.get(Owner.class).getId(), Team.class);
            if (t1 == null || t2 == null || t2.getTeam() != t1.getTeam()) {
                ed.setComponent(event.target, new Alive(false));
            }
        }
    }
    public void killProjectile(ContactEvent event) {
        event.projectile.set(event.projectile.get(Alive.class).kill());
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
