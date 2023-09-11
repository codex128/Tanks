/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.Alive;
import codex.tanks.components.Copy;
import codex.tanks.components.CreateOnDeath;
import codex.tanks.es.ESAppState;
import com.jme3.app.Application;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class LifeState extends ESAppState {
    
    private EntitySet alive;
    private EntitySet copy;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        alive = ed.getEntities(Alive.class);
        copy = ed.getEntities(
                Copy.filter(Copy.LIFE),
                Alive.class, Copy.class);
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
        copy.applyChanges();
        for (var e : copy) {
            var a = ed.getComponent(e.get(Copy.class).getCopy(), Alive.class);
            if (a != null && !a.isAlive()) {
                e.set(new Alive(false));
            }
        }
        if (alive.applyChanges()) {
            alive.getAddedEntities().forEach(e -> update(e));
            alive.getChangedEntities().forEach(e -> update(e));
        }
    }
    
    private void update(Entity e) {
        if (!e.get(Alive.class).isAlive()) {
            var cod = ed.getComponent(e.getId(), CreateOnDeath.class);
            if (cod != null) {
                factory.getEntityFactory().createAfterEffect(cod.getModel(), e.getId());
            }
            ed.removeEntity(e.getId());
        }
    }
    
    public EntitySet getAliveEntities() {
        return alive;
    }
    
}
