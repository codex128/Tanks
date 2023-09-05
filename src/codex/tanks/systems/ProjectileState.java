/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.boost.Timer;
import codex.tanks.collision.ContactEvent;
import codex.tanks.collision.ContactEventPipeline;
import codex.tanks.collision.PaddedRaytest;
import codex.tanks.collision.ShapeFilter;
import codex.tanks.components.*;
import codex.tanks.effects.MatChange;
import codex.tanks.factory.SpatialFactory;
import codex.tanks.util.ESAppState;
import codex.tanks.util.GameUtils;
import com.jme3.app.Application;
import com.jme3.collision.CollisionResults;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.shader.VarType;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class ProjectileState extends ESAppState {
    
    public static final float MISSILE_QUALIFIER = 15f;
    
    private EntitySet entities;
    private final Timer flameRefreshCycle = new Timer(0.03f);
    private CollisionState collision;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(EntityTransform.class, Velocity.class, Bounces.class, Alive.class);
        visuals = getState(VisualState.class, true);
        collision = getState(CollisionState.class, true);
        flameRefreshCycle.setCycleMode(Timer.CycleMode.ONCE);
        flameRefreshCycle.start();
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
        flameRefreshCycle.update(tpf);
        entities.applyChanges();
        for (var e : entities) {
            update(e, tpf);
        }
        if (flameRefreshCycle.isComplete()) {
            flameRefreshCycle.reset();
            flameRefreshCycle.start();
        }
    }
    
    private void update(Entity e, float tpf) {
        raytest(e, tpf);        
        if (flameRefreshCycle.isComplete() && isMissile(e)) {
            ed.setComponent(e.getId(), new MaterialUpdate("flame", new MatChange("Seed", VarType.Float, FastMath.nextRandomFloat()*97.43f)));
        }
    }
    public void raytest(Entity e, float tpf) {
        var filter = ShapeFilter.notId(e.getId());
        var test = new PaddedRaytest(
                new Ray(e.get(EntityTransform.class).getTranslation(), e.get(Velocity.class).getDirection()),
                filter, e.get(EntityTransform.class).getScale().x, filter, new CollisionResults());
        test.setResultMergingEnabled(true);
        test.cast(collision);
        var closest = test.getCollision();
        if (closest != null) {
            if (closest.getDistance() < e.get(Velocity.class).getSpeed()*tpf*2) {
                var id = test.getCollisionEntity();
                if (id != null) {
                    collision.bulletContact(id, e, closest);
                }
            }
        }
    }
    public void raytest(Entity e, ShapeFilter filter, float tpf) {
        
    }
    
    public EntitySet getBullets() {
        return entities;
    }
    
    public static boolean isMissile(Entity e) {
        return isMissile(e.get(Velocity.class).getSpeed());
    }
    public static boolean isMissile(float speed) {
        return speed >= MISSILE_QUALIFIER;
    }
    public static String getBulletModelId(float speed) {
        if (speed < MISSILE_QUALIFIER) return SpatialFactory.BULLET;
        else return SpatialFactory.MISSILE;
    }
    
}
