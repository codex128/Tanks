/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.EntityTransform;
import codex.tanks.components.FaceVelocity;
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
public class FaceVelocityState extends ESAppState {
    
    private static final float THRESHOLD = .99f;
    private EntitySet entities;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(new FunctionFilter<>(FaceVelocity.class, c -> c.isFacing()),
                EntityTransform.class, Velocity.class, FaceVelocity.class);
    }
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        Vector3f face = new Vector3f();
        Vector3f current = new Vector3f();
        if (entities.applyChanges()) for (var e : entities.getChangedEntities()) {
            face.set(e.get(Velocity.class).getVelocity()).normalizeLocal();
            e.get(EntityTransform.class).getRotation().mult(Vector3f.UNIT_Z, current);
            if (face.dot(current) < THRESHOLD) {
                e.set(new EntityTransform(e.get(EntityTransform.class)).setRotation(face, Vector3f.UNIT_Y));
            }
        }
    }
    
}
