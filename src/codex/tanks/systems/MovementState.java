/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.EntityTransform;
import codex.tanks.components.Velocity;
import codex.tanks.util.ESAppState;
import codex.tanks.util.FunctionFilter;
import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class MovementState extends ESAppState {

    private EntitySet entities;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(
                new FunctionFilter<>(Velocity.class, c -> !c.getVelocity().equals(Vector3f.ZERO)),
                EntityTransform.class, Velocity.class);
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
            e.set(new EntityTransform(e.get(EntityTransform.class)).move(e.get(Velocity.class).getVelocity().mult(tpf)));
        }
    }
    
}
