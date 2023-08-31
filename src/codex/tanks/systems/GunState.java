/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.AimDirection;
import codex.tanks.components.Bounces;
import codex.tanks.components.MuzzlePosition;
import codex.tanks.components.Power;
import codex.tanks.components.Team;
import codex.tanks.components.Trigger;
import codex.tanks.components.Visual;
import codex.tanks.util.ESAppState;
import com.jme3.app.Application;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class GunState extends ESAppState {

    private EntitySet entities;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(Power.class, Bounces.class, Team.class, MuzzlePosition.class, AimDirection.class, Trigger.class);
    }
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (entities.applyChanges()) {
            for (var e : entities.getChangedEntities()) {
                int trigger = e.get(Trigger.class).getValue();
                if (trigger > 0) {
                    shoot(e);
                }
                if (trigger != 0) {
                    e.set(new Trigger());
                }
            }
        }
    }
    
    private void shoot(Entity e) {
        factory.getEntityFactory().createBullet(
            e.getId(),
            e.get(MuzzlePosition.class).getPosition(),
            e.get(AimDirection.class).getAim().mult(e.get(Power.class).getPower()),
            e.get(Bounces.class).getRemaining());
    }
    
}
