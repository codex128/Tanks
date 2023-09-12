/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.Activated;
import codex.tanks.components.Door;
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
        entities = ed.getEntities(Visual.class, Gateway.class, Lock.class);
        players = ed.getEntities(Visual.class, Player.class);
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
            if (e.get(Lock.class).isLocked() || isEntityRoomActive(e.getId())) {
                continue;
            }
            // this method is flawed: it relies on the scene graph
            boolean activated = true;
            var gateBound = (BoundingBox)visuals.getSpatial(e.getId()).getWorldBound();
            for (var p : players) {
                var playerBound = (BoundingBox)visuals.getSpatial(p.getId()).getWorldBound();
                if (!gateBound.intersectsBoundingBox(playerBound)) {
                    activated = false;
                    break;
                }
            }
            if (activated) {
                if (e.get(Gateway.class).isReady()) {
                    e.set(new Gateway(false));
                    ed.setComponent(e.getId(), new Activated(true));
                    openDoors(e, true);
                }
            }
            else if (!e.get(Gateway.class).isReady()) {
                e.set(new Gateway(true));
                openDoors(e, false);
            }
        }
    }
    
    private void openDoors(Entity e, boolean open) {
        for (var d : e.get(Gateway.class).getDoors()) {
            ed.setComponent(d, ed.getComponent(d, Door.class).open(open));
        }
    }
    
}
