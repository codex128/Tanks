/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.Alive;
import codex.tanks.components.EntityTransform;
import codex.tanks.components.GameObject;
import codex.tanks.es.ESAppState;
import codex.tanks.es.FunctionFilter;
import com.jme3.app.Application;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class ExplosionState extends ESAppState {
    
    private EntitySet entities;
    private EntitySet alive;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(GameObject.filter("explosion"),
                GameObject.class, EntityTransform.class);
        alive = ed.getEntities(EntityTransform.class, Alive.class);
    }
    @Override
    protected void cleanup(Application app) {
        entities.release();
        alive.release();
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        entities.applyChanges();
        alive.applyChanges();
        for (var e : entities) {
            var t1 = e.get(EntityTransform.class);
            for (var a : alive) {
                if (a.getId().equals(e.getId())) {
                    continue;
                }
                var object = ed.getComponent(a.getId(), GameObject.class);
                if (object != null && object.getType().equals("explosion")) {
                    continue;
                }
                var t2 = a.get(EntityTransform.class);
                if (t1.getTranslation().distance(t2.getTranslation()) < t1.getAverageScale()) {
                    a.set(a.get(Alive.class).kill(Alive.NORMAL));
                }
            }
        }
    }
    
}
