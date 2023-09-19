/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.collision.PaddedRaytest;
import codex.tanks.collision.Raytest;
import codex.tanks.collision.ShapeFilter;
import codex.tanks.components.Bounces;
import codex.tanks.components.EntityRaytest;
import codex.tanks.components.Forward;
import codex.tanks.components.ProbeLocation;
import codex.tanks.systems.EntityState;
import com.jme3.app.Application;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityId;

/**
 *
 * @author gary
 */
public class Wander implements Algorithm {
    
    /**
     * Stores raycast directions and perpendicular vectors.
     */
    private static final Vector3f[] RAYCAST_DIRECTIONS = {
        Vector3f.UNIT_Z,
        new Vector3f(1f, 0f, -1f).normalizeLocal(),
        Vector3f.UNIT_X.negate(),
        new Vector3f(-1f, 0f, -1f).normalizeLocal(),
        Vector3f.UNIT_Z.negate(),
        new Vector3f(-1f, 0f, 1f).normalizeLocal(),
        Vector3f.UNIT_X,
        new Vector3f(1f, 0f, 1f).normalizeLocal(),
    };
    
    private float turnSpeed = 0.1f;
    private float directionFactor = 10f;
    private float distanceFactor = 1f;
    private float randomFactor = 0.1f;
    private float aggressiveFactor = 1f;
    private float decisive = .8f;
    private float wallAversion = 3f;
    private final DirectionProbe[] probes = new DirectionProbe[RAYCAST_DIRECTIONS.length];
    
    private float turnDir = 0f;
    private float regulatedTurn = 0f;
    
    public Wander() {}
    public Wander(J3map source) {
        turnSpeed = source.getFloat("turnSpeed", turnSpeed);
        directionFactor = source.getFloat("directionFactor", directionFactor);
        distanceFactor = source.getFloat("distanceFactor", distanceFactor);
        randomFactor = source.getFloat("randomFactor", randomFactor);
        aggressiveFactor = source.getFloat("aggressiveFactor", aggressiveFactor);
        decisive = source.getFloat("decisive", decisive);
        wallAversion = source.getFloat("wallAversion", wallAversion);
    }
    
    public Wander setTurnSpeed(float speed) {
        turnSpeed = speed;
        return this;
    }
    public Wander setCurrentDirectionFactor(float cdf) {
        directionFactor = cdf;
        return this;
    }
    public Wander setDistanceFactor(float df) {
        distanceFactor = df;
        return this;
    }
    public Wander setRandomFactor(float rf) {
        randomFactor = rf;
        return this;
    }
    public Wander setAggressiveFactor(float af) {
        aggressiveFactor = af;
        return this;
    }
    public Wander setDecisive(float d) {
        decisive = d;
        return this;
    }
    
    @Override
    public void initialize(Application app) {
        var ed = app.getStateManager().getState(EntityState.class).getEntityData();
        for (int i = 0; i < probes.length; i++) {
            var id = ed.createEntity();
            ed.setComponent(id, new Bounces(0));
            probes[i] = new DirectionProbe(RAYCAST_DIRECTIONS[i], id);
        }
    }
    @Override
    public void update(AlgorithmUpdate update) {}
    @Override
    public boolean move(AlgorithmUpdate update) {
        Vector3f forward = update.getComponent(Forward.class).getForward();
        Vector3f decision = new Vector3f();
        for (int i = 0; i < probes.length; i++) {
            probes[i].weight = 0f;
            update.getEntityData().setComponent(probes[i].entity, new EntityRaytest(update.getAgentId(),
                    new Ray(update.getComponent(ProbeLocation.class).getLocation(), RAYCAST_DIRECTIONS[i]), -1f));
        }
        for (int i = 0; i < probes.length; i++) {
            var probe = probes[i];
            var result = update.getRaytestResult(probe.entity);
            if (result == null || result.getCollisions().isEmpty()) {
                continue;
            }
            // direction
            probe.weight += (forward.dot(probe.vector)+1)*directionFactor/2;
            // random
            probe.weight += FastMath.nextRandomFloat()*randomFactor;
            // distance
            probe.weight += result.getCollisions().getFirst().getDistance()*distanceFactor;
            // aggression
            probe.weight += (probe.vector.dot(update.getDirectionToTarget())+1)/2*aggressiveFactor;
            // wall
            if (result.getCollisions().getFirst().getDistance() < wallAversion) {
                var opposite = probes[getOppositeDirectionIndex(i)];
                if (opposite.weight >= 0f) {
                    opposite.weight += 100f/result.getCollisions().getLast().getDistance();
                }
                probe.weight = -1f;
            }
        }
        DirectionProbe strongest = null;
        for (int i = 0; i < probes.length; i++) {
            var probe = probes[i];
            if (probe.weight >= 0f) {
                decision.addLocal(probe.vector.mult(probe.weight));
            }
            if (strongest == null || probe.weight > strongest.weight) {
                strongest = probes[i];
            }
        }
        if (strongest == null) {
            throw new NullPointerException("Internal Error");
        }
        decision.normalizeLocal();
        decision.set(FastMath.interpolateLinear(decisive, decision, strongest.vector)).normalizeLocal();
        var left = decision.cross(Vector3f.UNIT_Y);
        turnDir = turnSpeed*FastMath.sign(forward.dot(left));
        regulatedTurn = FastMath.interpolateLinear(.1f, regulatedTurn, turnDir);
        update.drive(update.rotate(regulatedTurn));
        //update.drive(decision);
        return true;
    }
    @Override
    public boolean aim(AlgorithmUpdate update) {
        return false;
    }
    @Override
    public boolean shoot(AlgorithmUpdate update) {
        return false;
    }
    @Override
    public boolean mine(AlgorithmUpdate update) {
        return false;
    }
    @Override
    public void endUpdate(AlgorithmUpdate update) {}
    @Override
    public void cleanup(Application app) {
        var ed = app.getStateManager().getState(EntityState.class).getEntityData();
        for (int i = 0; i < probes.length; i++) {
            ed.removeEntity(probes[i].entity);
        }
    }
    
    private static int getOppositeDirectionIndex(int dir) {
        if (dir < Wander.RAYCAST_DIRECTIONS.length/2) {
            return dir+Wander.RAYCAST_DIRECTIONS.length/2;
        }
        else {
            return dir-Wander.RAYCAST_DIRECTIONS.length/2;
        }
    }
    
    private static class DirectionProbe {
        float weight;
        final Vector3f vector;
        final EntityId entity;
        DirectionProbe(Vector3f vector, EntityId entity) {
            this.vector = vector;
            this.entity = entity;
        }
    }
    
}
