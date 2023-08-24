/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.Alive;
import codex.tanks.util.ESAppState;
import com.jme3.app.Application;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class LifeState extends ESAppState {
    
    private EntitySet alive;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        alive = ed.getEntities(Alive.class);
    }
    @Override
    protected void cleanup(Application app) {
        alive.release();
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (alive.applyChanges()) for (var e : alive.getChangedEntities()) {
            if (!e.get(Alive.class).isAlive()) {
                ed.removeEntity(e.getId());
            }
        }
    }
    
}
