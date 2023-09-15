/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.util.debug;

import codex.tanks.es.ESAppState;
import com.jme3.app.Application;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class DebugState extends ESAppState {

    private EntitySet entities;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(ComponentWatcher.class);
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
            System.out.println("Watching "+e.getId());
            for (var t : e.get(ComponentWatcher.class).getTypes()) {
                System.out.println("  "+t.getSimpleName()+" = "+ed.getComponent(e.getId(), t));
            }
        }
    }
    
}
