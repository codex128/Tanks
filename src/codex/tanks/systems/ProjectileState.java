/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.boost.Timer;
import codex.tanks.collision.SegmentedRaytest;
import codex.tanks.collision.ShapeFilter;
import codex.tanks.components.*;
import codex.tanks.effects.MatChange;
import codex.tanks.blueprints.SpatialFactory;
import codex.tanks.collision.ContactEvent;
import codex.tanks.es.ESAppState;
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
public class ProjectileState extends ESAppState {
    
    public static final float MISSILE_QUALIFIER = 20f;
    public static final Class[] COMPONENT_TYPES = {
        EntityTransform.class, Velocity.class, Owner.class, Damage.class, Bounces.class, Alive.class};
    
    private EntitySet entities;
    private final Timer flameRefreshCycle = new Timer(0.03f);
    private ContactState collision;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(COMPONENT_TYPES);
        collision = getState(ContactState.class, true);
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
            if (!isEntityRoomActive(e.getId())) {
                continue;
            }
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
    public SegmentedRaytest.SegmentIterator raytest(Entity e, float tpf) {
        return raytest(e, ShapeFilter.notId(e.getId()), tpf);
    }
    public SegmentedRaytest.SegmentIterator raytest(Entity e, ShapeFilter filter, float tpf) {
        var v = e.get(Velocity.class);
        var raytest = new SegmentedRaytest(collision);
        raytest.setRay(new Ray(e.get(EntityTransform.class).getTranslation(), e.get(Velocity.class).getDirection()));
        raytest.setFilter(filter);
        raytest.setDistance(v.getSpeed()*tpf);
        raytest.setOriginEntity(e.getId());
        return raytest(e, raytest);
    }
    public SegmentedRaytest.SegmentIterator raytest(Entity e, SegmentedRaytest raytest) {
        var iterator = raytest.iterator();
        while (iterator.hasNext()) {
            // step the raytest
            //System.out.println(FastMath.rand.nextInt(100)+"  "+getAgentId()+" is active: "+getProjectileState().isEntityRoomActive(getAgentId()));
            var closest = iterator.next();
            // fetch step results
            var id = iterator.getCollisionEntity();
            if (closest != null) {
                // collision occured, do something
                collision.triggerContactEvent(new ContactEvent(id, e, closest));
            }
            if (!e.get(Alive.class).isAlive()) {
                break;
            }
            // Set the next raytest direction.
            // Changes to the direction in the collision reaction are propagated through the entity.
            iterator.setNextDirection(e.get(Velocity.class).getDirection());
        }
        // set projectile translation
        e.set(new EntityTransform(e.get(EntityTransform.class)).setTranslation(iterator.getPosition()));
        return iterator;
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
    public static boolean isLaser(Entity e) {
        return isLaser(e.get(Velocity.class).getSpeed());
    }
    public static boolean isLaser(float speed) {
        return speed < 0;
    }
    public static String getBulletModelId(float speed) {
        if (speed < MISSILE_QUALIFIER) return SpatialFactory.BULLET;
        else return SpatialFactory.MISSILE;
    }
    
}
