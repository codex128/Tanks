/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.Door;
import codex.tanks.components.EntityTransform;
import codex.tanks.components.Travel;
import codex.tanks.es.ESAppState;
import com.jme3.app.Application;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class DoorState extends ESAppState {

    private EntitySet entities;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(EntityTransform.class, Door.class);
    }
    @Override
    protected void cleanup(Application app) {
        entities.release();
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (entities.applyChanges()) {
            entities.getAddedEntities().forEach(e -> update(e));
            entities.getChangedEntities().forEach(e -> update(e));
        }
    }
    
    private void update(Entity e) {
        var d = e.get(Door.class);
        if (d.isOpen() != d.getNextState()) {
            open(e, d.getNextState());
        }
    }
    private void open(Entity e, boolean open) {
        ed.setComponent(e.getId(), new Travel(e.get(EntityTransform.class).getTranslation().add(e.get(Door.class).getOffset().mult(open ? 1 : -1))));
        e.set(e.get(Door.class).setCurrentState(open));
    }
    
}
