/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.collision;

import codex.tanks.components.Bounces;
import codex.tanks.components.CollisionShape;
import codex.tanks.components.EntityRaytest;
import codex.tanks.es.EntityAccess;
import codex.tanks.systems.ContactState;
import codex.tanks.util.debug.ShapeFilterDebugger;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * System (not state) for performing raytests outside the main update thread.
 * 
 * @author codex
 */
public class RaytestSystem extends Thread {
    
    private static final long REFRESH_MILLIS = 1000;
    
    private final EntitySet entities;
    private final ConcurrentHashMap<EntityId, SegmentedRaytest.SegmentIterator> results = new ConcurrentHashMap<>();
    private final ContactState contactState;
    private boolean stop = false;
    
    public RaytestSystem(ContactState contactState) {
        this.contactState = contactState;
        entities = contactState.getEntityData().getEntities(EntityRaytest.class, Bounces.class);
    }
    
    @Override
    public void run() {
        while (!stop) {
            if (entities.applyChanges()) {
                try {
                    sleep(REFRESH_MILLIS);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RaytestSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
                entities.getAddedEntities().forEach(e -> update(e));
                entities.getChangedEntities().forEach(e -> update(e));
                for (var e : entities.getRemovedEntities()) {
                    results.remove(e.getId());
                }
            }
        }
        entities.release();
    }
    private void update(Entity e) {
        var data = e.get(EntityRaytest.class);
        var raytest = new SegmentedRaytest(contactState);
        raytest.setOriginEntity(data.getOrigin());
        raytest.setRay(data.getRay());
        raytest.setFilter(data.getMainFilter());
        raytest.setFirstCastFilter(data.getFirstCastFilter());
        raytest.setDistance(data.getDistance());
        raytest.setMaxConsecutiveBounces(e.get(Bounces.class).getRemaining());
        //raytest.setDebugEnabled(true);
        var it = raytest.iterator();
        if (it.hasNext()) {
            it.next();
        }
        results.put(e.getId(), it);
    }
    public void stopThread() {
        stop = true;
    }
    
    public SegmentedRaytest.SegmentIterator get(EntityId id) {
        return results.get(id);
    }
    
    
}
