/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.collision.PaddedRaytest;
import codex.tanks.collision.ShapeFilter;
import codex.tanks.components.Forward;
import codex.tanks.components.ProbeLocation;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;

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
    public void update(AlgorithmUpdate update) {}
    @Override
    public boolean move(AlgorithmUpdate update) {
        Vector3f current = update.getComponent(Forward.class).getForward();
        Vector3f decision = new Vector3f();
        var collisions = new Direction[RAYCAST_DIRECTIONS.length];
        for (int i = 0; i < collisions.length; i++) {
            collisions[i] = new Direction(update, i);
            collisions[i].weight = 0f;
        }
        for (int i = 0; i < collisions.length; i++) {
            var direction = collisions[i];
            direction.weight += (current.dot(direction.vector)+1)*(directionFactor/2);
            direction.weight += FastMath.nextRandomFloat()*randomFactor;
            direction.weight += direction.collision.getDistance()*distanceFactor;
            direction.weight += (direction.vector.dot(update.getDirectionToPlayer())+1)/2*aggressiveFactor;
            if (direction.collision.getDistance() < wallAversion) {
                var opposite = collisions[getOppositeDirectionIndex(i)];
                if (opposite.weight >= 0f) opposite.weight += (1f/direction.collision.getDistance())*100;
                direction.weight = -1f;
            }
        }
        Direction strongest = null;
        for (int i = 0; i < collisions.length; i++) {
            var direction = collisions[i];
            if (direction.weight >= 0f) {
                decision.addLocal(direction.vector.mult(direction.weight));
            }
            if (strongest == null || collisions[i].weight > strongest.weight) {
                strongest = collisions[i];
            }
        }
        decision.normalizeLocal();
        decision.set(FastMath.interpolateLinear(decisive, decision, strongest.vector)).normalizeLocal();
        var left = decision.cross(Vector3f.UNIT_Y);
        turnDir = turnSpeed*FastMath.sign(current.dot(left));
        regulatedTurn = FastMath.interpolateLinear(.1f, regulatedTurn, turnDir);
        update.drive(update.rotate(regulatedTurn));
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
    
    private static int getOppositeDirectionIndex(int dir) {
        if (dir < Wander.RAYCAST_DIRECTIONS.length/2) {
            return dir+Wander.RAYCAST_DIRECTIONS.length/2;
        }
        else {
            return dir-Wander.RAYCAST_DIRECTIONS.length/2;
        }
    }
    
    private static class Direction {
        float weight;
        Vector3f vector;
        CollisionResult collision;
        //private Direction() {}
        private Direction(AlgorithmUpdate update, int direction) {
            raycast(update, direction);
        }
        private void raycast(AlgorithmUpdate update, int direction) {
            vector = RAYCAST_DIRECTIONS[direction];
            var filter = ShapeFilter.notId(update.getAgentId());
            var test = new PaddedRaytest(new Ray(update.getComponent(ProbeLocation.class).getLocation(), vector), filter, 1f, filter, new CollisionResults());
            test.setResultMergingEnabled(true);
            test.cast(update.getCollisionState());
            collision = test.getCollision();
        }
    }
    
}
