/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.boost.Timer;
import codex.tanks.collision.BasicRaytest;
import codex.tanks.collision.CollisionState;
import codex.tanks.collision.ShapeFilter;
import codex.tanks.components.*;
import codex.tanks.effects.MatChange;
import codex.tanks.factory.SpatialFactory;
import codex.tanks.util.ESAppState;
import com.jme3.app.Application;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.shader.VarType;
import com.simsilica.es.Entity;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class BulletState extends ESAppState {
    
    public static final float MISSILE_QUALIFIER = 15f;
    
    private EntitySet entities;
    private Timer flameUpdate = new Timer(0.03f);
    private CollisionState collision;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(Visual.class, EntityTransform.class, Velocity.class, Bounces.class, Owner.class, Alive.class);
        visuals = getState(VisualState.class, true);
        collision = getState(CollisionState.class, true);
        flameUpdate.setCycleMode(Timer.CycleMode.ONCE);
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
        flameUpdate.update(tpf);
        entities.applyChanges();
        for (var e : entities) {
            update(e, tpf);
        }
        if (flameUpdate.isComplete()) {
            flameUpdate.reset();
            flameUpdate.start();
        }
    }
    
    private void update(Entity e, float tpf) {
        //var bullet = bullets.get(e.getId());
        //bullet.update(collision, tpf);
        raytest(e, tpf);        
        if (flameUpdate.isComplete() && isMissile(e)) {
            ed.setComponent(e.getId(), new MaterialUpdate("flame", new MatChange("Seed", VarType.Float, FastMath.nextRandomFloat()*97.43f)));
        }
    }
    private void raytest(Entity e, float tpf) {
        var test = new BasicRaytest(
                new Ray(e.get(EntityTransform.class).getTranslation(), e.get(Velocity.class).getDirection()),
                ShapeFilter.notId(e.getId()));
        test.cast(collision);
        var closest = test.getCollision();
        if (closest != null) {
            if (closest.getDistance() < e.get(Velocity.class).getSpeed()*tpf*2) {
                var id = test.getCollisionEntity();
                if (id != null) {
                    collision.bulletCollision(id, e, closest);
                }
            }
        }
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
