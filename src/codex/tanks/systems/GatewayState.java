/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.Activated;
import codex.tanks.components.Door;
import codex.tanks.components.EntityTransform;
import codex.tanks.components.Gateway;
import codex.tanks.components.Lock;
import codex.tanks.components.Player;
import codex.tanks.components.Visual;
import codex.tanks.es.ESAppState;
import com.jme3.app.Application;
import com.jme3.bounding.BoundingBox;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class GatewayState extends ESAppState {

    private EntitySet entities;
    private EntitySet players;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(Visual.class, EntityTransform.class, Gateway.class, Lock.class);
        players = ed.getEntities(Visual.class, EntityTransform.class, Player.class);
    }
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        var master = getDungeonMaster();
        if (master == null) return;
        entities.applyChanges();
        players.applyChanges();
        for (var e : entities) {
            if (e.get(Lock.class).isLocked() || !isEntityRoomActive(e.getId())) {
                continue;
            }
            boolean activated = !players.isEmpty();
            boolean open = false;
            var gateBound = visuals.getSpatial(e.getId()).getWorldBound();
            for (var p : players) {
                if (activated && !gateBound.contains(p.get(EntityTransform.class).getTranslation())) {
                    activated = false;
                }
                var d = e.get(Gateway.class).getDistance();
                if (!open && e.get(EntityTransform.class).getTranslation().distanceSquared(p.get(EntityTransform.class).getTranslation()) < d*d) {
                    open = true;
                }
            }
            if (activated) {
                if (!e.get(Gateway.class).isReady()) {
                    e.set(e.get(Gateway.class).setIsReady(true));
                    ed.setComponent(e.getId(), new Activated(true));
                }
            }
            else if (e.get(Gateway.class).isReady()) {
                e.set(e.get(Gateway.class).setIsReady(false));
            }
            openDoors(e, open);
        }
    }
    
    private void openDoors(Entity e, boolean open) {
        for (var d : e.get(Gateway.class).getDoors()) {
            ed.setComponent(d, ed.getComponent(d, Door.class).open(open));
        }
    }
    
}
