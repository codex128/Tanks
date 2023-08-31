/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.GameObject;
import codex.tanks.components.OrphanBucket;
import codex.tanks.util.ESAppState;
import com.jme3.app.Application;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class OrphanState extends ESAppState {
    
    private EntitySet entities;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(OrphanBucket.class);
    }
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        entities.applyChanges();
        for (var e : entities) {
            if (ed.getComponent(e.get(OrphanBucket.class).getParent(), GameObject.class) == null) {
                e.get(OrphanBucket.class).apply(ed, e.getId());
                ed.removeComponent(e.getId(), OrphanBucket.class);
            }
        }
    }
    
}
